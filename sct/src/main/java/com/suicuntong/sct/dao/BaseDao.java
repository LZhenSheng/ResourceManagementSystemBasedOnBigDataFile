package com.suicuntong.sct.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;

@Component
public class BaseDao {

    private static String url; // = "jdbc:postgresql://124.70.198.245:15432/db"; // 连接字符串
    private static String dbUserName; //= "gaussdb";                  // 用户名
    private static String dbUserPwd; // = "Secretpassword@123";                 // 密码

    @Value("${gaussdb.url}")
    public void setUrl(String url){
        this.url = url;
    }

    @Value("${gaussdb.dbUserName}")
    public void setDbUserName(String dbUserName){
        this.dbUserName = dbUserName;
    }

    @Value("${gaussdb.dbUserPwd}")
    public void setDbUserPwd(String dbUserPwd){
        this.dbUserPwd = dbUserPwd;
    }


    protected static Connection conn = null;

    public static Connection getConnection() {
        if (conn != null)
            return conn;

        try {
            // 加载驱动类
            Class.forName("org.postgresql.Driver");
            // 获取数据库连接
            conn = DriverManager.getConnection(url, dbUserName, dbUserPwd);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 封装底层数据库连接关闭方法
     * @author liuhaibing
     * @date 2019年12月18日
     * @version 1.0
     */

    public void closeConnection() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭数据库的自动事务提交功能，从自己控制事务的提交或者回滚
     * @author liuhaibing
     * @date 2019年12月18日
     * @version 1.0
     */

    public void openTransaction() {
        if (conn != null) {
            try {
                // 关闭数据库操作的自动commit功能
                conn.setAutoCommit(false);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 手动提交事务
     * @author liuhaibing
     * @date 2019年12月18日
     * @version 1.0
     */
    public void commit() {
        if (conn != null) {
            try {
                conn.commit();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }

    public void rollback() {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
