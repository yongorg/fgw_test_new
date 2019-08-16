package cn.com.wavetop.DBConn.wb;

import cn.com.wavetop.Main;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 *
 * 绿化局连接池工具类
 *
 * @Author yongz
 * @Date 2019/8/12、14:26
 *
 */
public class LhqzjConnPool {
    protected static Logger logger = Logger.getLogger(Main.class);
    private static DataSource ds;

    /**
     * 加载配置文件 ，初始化对象
     */
    static {
        // 加载配置文件
        Properties pro = new Properties();
        InputStream is = AnqzjConnPool.class.getClassLoader().getResourceAsStream("wb/lhqzj.properties");
        try {
            pro.load(is);
        } catch (IOException e) {
            logger.error("加载配置文件失败！");
            e.printStackTrace();
        }


        try {
            // 获取连接池对象
            ds = DruidDataSourceFactory.createDataSource(pro);
            logger.info("交通局前置机连接池创建成功！");
        } catch (Exception e) {
            logger.error("获取连接池对象失败！");
            e.printStackTrace();
        }

    }

    /**
     * 获取连接对象
     * @return
     */
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
    /**
     * 释放资源
     *
     */
    public static void close(Statement stmt, Connection connection, ResultSet rs) {
        if (stmt != null){
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (rs != null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static void close(Statement stmt, Connection connection) throws SQLException {
        close(stmt, connection, null);
    }

    /**
     * 获取连接池的方法
     *
     * @return
     */
    public  static DataSource getDataSources(){
        return ds;
    }

    public static void main(String[] args) {
        try {
            Connection connection = getConnection();
        } catch (SQLException e) {
            logger.error("数据库连接池失败！");
            e.printStackTrace();
        }
    }
}
