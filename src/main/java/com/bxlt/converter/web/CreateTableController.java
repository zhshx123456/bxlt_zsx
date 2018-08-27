package com.bxlt.converter.web;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
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
import java.sql.*;
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
        String params = request.getParameter("params");
        String ps[] = params.split(":");
        StringBuffer sql = new StringBuffer();
        sql.append(" create table random_data111( id varchar(32),") ;
        for(int i=0;i<ps.length;i++){
            //String columnValue = request.getParameter(ps[i]);
            sql.append(ps[i]+"  varchar(32), ") ;
        }
        sql.append("geo geometry);" );

        StringBuffer sql2 = new StringBuffer();
        String id=UUID.randomUUID().toString().replaceAll("-", "");
        String geo = request.getParameter("coordinate");
        sql2.append("insert into random_data111( id,") ;
        for(int i=0;i<ps.length;i++){
            sql2.append(ps[i]+", ") ;
        }
        sql2.append("geo) values('" +id+"','");
        for(int i=0;i<ps.length;i++){
            String columnValue = request.getParameter(ps[i]);
            if(i==ps.length-1){
                sql2.append(columnValue+"', ");
            }else{
                sql2.append(columnValue+"', '") ;
            }
        }
//        String mgs[] = geo.split(",");
//        String wgs84 ="";
//        for(int i=0;i<mgs.length;i=i+2){
//            double lon =Double.parseDouble(mgs[i]);
//            double lat =Double.parseDouble(mgs[i+1]);
//            try {
//                double wgs[]= this.convert(lon,lat,"EPSG:3857");
//                wgs84 = wgs84+wgs[0]+" "+wgs[1]+",";
//            } catch (FactoryException e) {
//                e.printStackTrace();
//            } catch (TransformException e) {
//                e.printStackTrace();
//            }
//        }
//        wgs84 = wgs84.substring(0,wgs84.length()-1);
        sql2.append("ST_GeomFromText('MULTIPOLYGON ((("+geo+")))'));" );

        this.createData(sql.toString());//创建表
        this.createData(sql2.toString());//添加数据
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
