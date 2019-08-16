package com.test;

import java.sql.Connection;
import java.sql.DriverManager;

public class DataBaseConn {
    
    private String databaseName = "";//数据名称
    private String useName = "";//用户登入名
    private String password ="";//登入密码
    
    public DataBaseConn(){
        this.databaseName = "INTRUST";
        this.useName = "sa";
        this.password = "000000";
    }
    
    public DataBaseConn(String databaseName, String useName, String password){
        this.databaseName = databaseName;
        this.useName = useName;
        this.password = password;
    }
    
    public Connection con = null;
    
    private String MYSQLDRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private String MYSQLURL = "jdbc:mysql://localhost:3306/" + databaseName + "?use" + useName + "&password" + password + "&useUnicode=true&characterEncoding=gb2312";
    //SQLsServer驱动
    private String MSSQLDRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private String MSSQLURL = "jdbc:sqlserver://localhost:1433;DatabaseName=INTRUST";
    
    public void createMysqlConn(){
        con = null;
        try{
            Class.forName(MYSQLDRIVER).newInstance();
            con = DriverManager.getConnection(MYSQLURL);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    //SQLsServer驱动
    public void createMssqlConn(){
        con = null;
        try{
            Class.forName(MSSQLDRIVER).newInstance();
            con = DriverManager.getConnection(MSSQLURL, "sa", "000000");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public Connection getMysqlConn(){
        createMysqlConn();
        return con;
    }
    
    public Connection getMssqlConn(){
        createMssqlConn();
        return con;
    }
    
    public void closeCon(){
        if(con != null){
            try {
                con.close();
                con = null;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}