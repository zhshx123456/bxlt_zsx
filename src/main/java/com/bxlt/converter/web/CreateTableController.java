package com.bxlt.converter.web;

import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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
        //String id=UUID.randomUUID().toString().replaceAll("-", "");
        String geo = request.getParameter("coordinate");
        sql2.append("insert into random_data111( id,") ;
        for(int i=0;i<ps.length;i++){
            sql2.append(ps[i]+", ") ;
        }
        sql2.append("geo) values('" +1+"','");
        for(int i=0;i<ps.length;i++){
            String columnValue = request.getParameter(ps[i]);
            if(i==ps.length-1){
                sql2.append(columnValue+"', ");
            }else{
                sql2.append(columnValue+"', '") ;
            }
        }
        sql2.append("ST_GeomFromText('"+geo+"'));" );

        this.createData(sql.toString());//创建表
        this.createData(sql2.toString());//添加数据
    }

    public void createData(String sql){
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/geoserver";
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
