package com.dddd.qa.zybh.utils;

import com.dddd.qa.zybh.Constant.Common;

import java.sql.*;

/**
 * @author zhangsc
 * @date 2025年12月11日 15:45:33
 * @packageName com.dddd.qa.zybh.utils
 * @className JDBCUtil
 * @describe
 */
public class JDBCUtil {
    private static final String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver"; // MySQL 5.x 驱动 (较旧)
    // private static final String DRIVER_CLASS_NAME = "oracle.jdbc.driver.OracleDriver"; // Oracle 示例

    // --- 静态代码块，用于加载驱动 ---
    static {
        try {
            Class.forName(DRIVER_CLASS_NAME);
            System.out.println("数据库驱动加载成功: " + DRIVER_CLASS_NAME); // 可选的日志输出
        } catch (ClassNotFoundException e) {
            System.err.println("加载数据库驱动失败 (" + DRIVER_CLASS_NAME + "): " + e.getMessage());
            // 在实际应用中，通常应该抛出一个运行时异常或记录严重错误日志
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * 获取数据库连接对象 (Connection)。
     *
     * @return Connection 对象，如果获取失败则返回 null 或抛出异常。
     * @throws SQLException 如果建立连接时发生数据库访问错误。
     */
    public static Connection getConn() throws SQLException {
        try {
            Connection connection = DriverManager.getConnection(Common.SqlUrl,Common.username,Common.password);
            System.out.println("数据库连接已建立"); // 可选的日志输出
            return connection;
        } catch (SQLException e) {
            System.err.println("获取数据库连接失败: " + e.getMessage());
            throw e; // 重新抛出异常，让调用者处理
        }
    }


    /**
     * 关闭 ResultSet 对象。
     * 提供此方法是为了方便在 finally 块中安全地关闭资源。
     *
     * @param rs 要关闭的 ResultSet 对象，可以为 null。
     */
    public static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("关闭 ResultSet 时发生错误: " + e.getMessage());
                // 通常记录日志即可，不向上抛出
            }
        }
    }

    /**
     * 关闭 Statement 对象 (包括 PreparedStatement 和 CallableStatement)。
     * 提供此方法是为了方便在 finally 块中安全地关闭资源。
     *
     * @param stmt 要关闭的 Statement 对象，可以为 null。
     */
    public static void close(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.err.println("关闭 Statement 时发生错误: " + e.getMessage());
                // 通常记录日志即可，不向上抛出
            }
        }
    }

    /**
     * 关闭 Connection 对象。
     * 提供此方法是为了方便在 finally 块中安全地关闭资源。
     * <p>
     * 注意：如果连接是从连接池获取的，请勿直接调用此方法关闭，
     * 应该调用连接池提供的归还连接的方法。
     * </p>
     *
     * @param conn 要关闭的 Connection 对象，可以为 null。
     */
    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("数据库连接已关闭"); // 可选的日志输出
            } catch (SQLException e) {
                System.err.println("关闭 Connection 时发生错误: " + e.getMessage());
                // 通常记录日志即可，不向上抛出
            }
        }
    }

    /**
     * 安全地关闭所有数据库资源 (ResultSet, Statement, Connection)。
     * 按照 ResultSet -> Statement -> Connection 的顺序关闭。
     * 这个方法可以在 finally 块中调用以确保资源被释放。
     *
     * @param rs   ResultSet 对象，可以为 null。
     * @param stmt Statement 对象，可以为 null。
     * @param conn Connection 对象，可以为 null。
     */
    public static void closeResource(ResultSet rs, Statement stmt, Connection conn) {
        // 先关闭 ResultSet
        close(rs);
        // 再关闭 Statement
        close(stmt);
        // 最后关闭 Connection
        close(conn);
    }


}
