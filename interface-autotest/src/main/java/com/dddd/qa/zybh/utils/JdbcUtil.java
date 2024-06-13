package com.dddd.qa.zybh.utils;

import com.dddd.qa.zybh.Constant.Common;

import java.sql.*;

/**
 * @author zhangsc
 * @date 2022-05-06 下午3:39
 */
public class JdbcUtil {

    private static Connection conn = null;

    public static Connection getConn(){
        try{
            //获取连接对象Connection
            Class.forName(Common.driver);
            //yolo_usercenter
            //conn = DriverManager.getConnection(Common.usercenterUrl,Common.username,Common.password);
            //yololiv-test
            conn = DriverManager.getConnection(Common.yololivUrl,Common.username,Common.password);

        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (SQLException e){
            e.printStackTrace();
            close();
        }
        return conn;
    }

    public static void close(){
        try{
            conn.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


}
