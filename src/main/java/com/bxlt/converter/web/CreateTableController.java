package com.bxlt.converter.web;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import it.geosolutions.geoserver.rest.GeoServerRESTManager;
import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.decoder.RESTDataStore;
import it.geosolutions.geoserver.rest.decoder.RESTLayer;
import it.geosolutions.geoserver.rest.encoder.GSLayerEncoder;
import it.geosolutions.geoserver.rest.encoder.datastore.GSPostGISDatastoreEncoder;
import it.geosolutions.geoserver.rest.encoder.feature.GSFeatureTypeEncoder;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.xml.crypto.dsig.TransformException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.List;
import java.util.UUID;

/**
 * @program: converter
 * @description: 创建表
 * @author: zsx
 * @create: 2018-08-24 14:43
 **/
@EnableTransactionManagement
@RestController
@RequestMapping("/createTable")
public class CreateTableController {

    @RequestMapping(value = "/test",produces = {"application/json;charset=UTF-8"},method = RequestMethod.GET)
    public void test(HttpServletRequest request){
        //String sql = "select * from mould_type";
        //String sql = "create table random_data( id int(32),ND0 int(32),ND1 int(32),ND2 int(32),ND3 int(32),ND4 int(32),ND5 int(32),ND6 int(32) );";
        // "insert into random_data( id,ND0,ND1,ND2,ND3,ND4,ND5,ND6) values('3','2','4','1','0','3','2');";
        //ResultSet rs = statement.executeQuery(sql);
        String params = request.getParameter("params");//获取页面参数
        String ps[] = params.split(":");
        StringBuffer sql = new StringBuffer();//如果没有表则创建
        sql.append(" create table IF NOT EXISTS random_data( id varchar(32),");
        for(int i=0;i<ps.length;i++){
            sql.append(ps[i]+"  varchar(32), ");
        }
        sql.append("geo geometry);" );

        StringBuffer sql2 = new StringBuffer();//添加或修改数据
        String id = request.getParameter("id");
        String geo = request.getParameter("coordinate");
        if("".equals(id)||null==id){
            String newid=UUID.randomUUID().toString().replaceAll("-", "");
            sql2.append("insert into random_data( id,");
            for(int i=0;i<ps.length;i++){
                sql2.append(ps[i]+", ");
            }
            sql2.append("geo) values('" +newid+"','");
            for(int i=0;i<ps.length;i++){
                String columnValue = request.getParameter(ps[i]);
                if(i==ps.length-1){
                    sql2.append(columnValue+"', ");
                }else{
                    sql2.append(columnValue+"', '");
                }
            }
            String geoType = request.getParameter("geoType");
            String type = "";
            if("Point".equals(geoType)){
                type = "POINT ("+geo+")";
            }else if("LineString".equals(geoType)){
                type = "LINESTRING ("+geo+")";
            }else if("Polygon".equals(geoType)){
                type = "MULTIPOLYGON ((("+geo+")))";
            }
            sql2.append("ST_GeomFromText('"+type+"'));" );
        }else{
            sql2.append("update random_data set ");
            for(int i=0;i<ps.length;i++){
                String columnValue = request.getParameter(ps[i]);
                if("id"!=ps[i])
                    sql2.append(ps[i]+" = '"+columnValue+"',");
            }
            String geoType = request.getParameter("geoType");
            String type = "";
            if("Point".equals(geoType)){
                type = "POINT ("+geo+")";
            }else if("LineString".equals(geoType)){
                type = "LINESTRING ("+geo+")";
            }else if("Polygon".equals(geoType)){
                type = "MULTIPOLYGON ((("+geo+")))";
            }
            sql2.append(" geo=ST_GeomFromText('"+type+"') " );
            sql2.append(" where id='"+id +"'");
        }


        this.createData(sql.toString());//创建表
        this.createData(sql2.toString());//添加或修改数据

        //上面是入库
        //----------------------------分割线--------------------------------
//        //下面是发布geoserver服务
//
//        //配置数据库连接参数、geoserver连接参数，要发布的图层信息参数 start
//
//        String url = "http://localhost:8080/geoserver" ;
//        String username = "admin" ;
//        String passwd = "geoserver" ;
//
//        //postgis连接配置
////        String postgisHost = "localhost" ;
////        int postgisPort = 6666 ;
////        String postgisUser = "xxx" ;
////        String postgisPassword = "xxx" ;
////        String postgisDatabase = "xxx" ;
//        String mysqlHost = "localhost" ;
//        int mysqlPort = 3306 ;
//        String mysqlUser = "root" ;
//        String mysqlPassword = "bxlt123456" ;
//        String mysqlDatabase = "yun_dev" ;
//
//        String ws = "javaTest" ; //待创建和发布图层的workspace
//        String store_name = "javaTest" ; //数据库连接要创建的store
//        String table_name = "random_data111" ; // 数据库要发布的表名称,后面图层名称和表名保持一致
//
//        //配置数据库连接参数、geoserver连接参数，要发布的图层信息参数 end
//
//        //----------------------------分割线----------------------------
//
//        //判断workspace是否存在，不存在则创建  start
//
//        URL u = null;
//        try {
//            u = new URL(url);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//        GeoServerRESTManager manager = new GeoServerRESTManager(u, username, passwd);
//        GeoServerRESTPublisher publisher = manager.getPublisher() ;
//        //创建一个workspace
//        List<String> workspaces = manager.getReader().getWorkspaceNames();
//        if(!workspaces.contains(ws)){
//            boolean createws = publisher.createWorkspace(ws);
//            System.out.println("create ws : " + createws);
//        }else {
//            System.out.println("workspace已经存在了,ws :" + ws);
//        }
//
//        //判断workspace是否存在，不存在则创建  end
//
//        //----------------------------分割线----------------------------
//
//        //判断datastore是否已经存在，不存在则创建  start
//
//        //store 包含和workspace一致，一个ws一个连接即可
//        RESTDataStore restStore = manager.getReader().getDatastore(ws, store_name);
//
//        if(restStore == null){
//            GSPostGISDatastoreEncoder store = new GSPostGISDatastoreEncoder(store_name);
//            store.setHost(mysqlHost);//设置url
//            store.setPort(mysqlPort);//设置端口
//            store.setUser(mysqlUser);// 数据库的用户名
//            store.setPassword(mysqlPassword);// 数据库的密码
//            store.setDatabase(mysqlDatabase);// 那个数据库;
//            store.setSchema("public"); //当前先默认使用public这个schema
//            store.setConnectionTimeout(20);// 超时设置
//            //store.setName(schema);
//            store.setMaxConnections(20); // 最大连接数
//            store.setMinConnections(1);     // 最小连接数
//            store.setExposePrimaryKeys(true);
//            boolean createStore = manager.getStoreManager().create(ws, store);
//
//            System.out.println("create store : " + createStore);
//
//        } else {
//            System.out.println("数据store已经发布过了,store:" + store_name);
//        }
//
//        //判断datastore是否已经存在，不存在则创建  end
//
//        //----------------------------分割线----------------------------
//
//        //判断图层是否已经存在，不存在则创建  start
//
//        //发布
//        RESTLayer layer;
//        layer = manager.getReader().getLayer(ws, table_name);
//        if(layer == null){
//            GSFeatureTypeEncoder pds = new GSFeatureTypeEncoder();
//            pds.setTitle(table_name);
//            pds.setName(table_name);
//            pds.setSRS("EPSG:4326");
//            GSLayerEncoder layerEncoder = new GSLayerEncoder();
//            boolean publish = manager.getPublisher().publishDBLayer(ws, store_name,  pds, layerEncoder);
//            System.out.println("publish : " + publish);
//        }else {
//            System.out.println("表已经发布过了,table:" + table_name);
//        }
//
//        //判断图层是否已经存在，不存在则创建  end




    }

    public double[] convert(double lon, double lat, String strWKT)
            throws FactoryException, MismatchedDimensionException, TransformException {
        Coordinate sourceCoord = new Coordinate(lon, lat);
        GeometryFactory geoFactory = new GeometryFactory();
        Point sourcePoint = geoFactory.createPoint(sourceCoord);

        CoordinateReferenceSystem mercatroCRS = CRS.parseWKT(strWKT);
        MathTransform transform = CRS.findMathTransform(DefaultGeographicCRS.WGS84, mercatroCRS);
        Point targetPoint = null;
        try {
            targetPoint = (Point) JTS.transform(sourcePoint, transform);
        } catch (org.opengis.referencing.operation.TransformException e) {
            e.printStackTrace();
        }

        double[] targetCoord = {targetPoint.getX(), targetPoint.getY()};
        return targetCoord;
    }

    //连接数据库、操作数据
    public void createData(String sql){
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://114.242.9.143:3306/yun_dev";
        //String url = "jdbc:mysql://localhost:3306/geoserver";
        String user = "root";
        String password = "bxlt123456";
        Connection conn = null;
        Statement statement = null;
        try {
                Class.forName(driver);
                conn = (Connection) DriverManager.getConnection(url, user, password);
                if (!conn.isClosed()) {
                    //System.out.println("Succeeded connecting to the Database!");
                    statement = conn.createStatement();
                    statement.executeUpdate(sql);
                }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(!conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if(!statement.isClosed()){
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
