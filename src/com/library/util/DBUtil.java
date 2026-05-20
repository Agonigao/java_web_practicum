package com.library.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据库连接工具类
 */
public class DBUtil {

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/library_system?useSSL=false&serverTimezone=UTC&characterEncoding=utf8";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "1234";

    static {
        try {
            Class.forName(DRIVER);
            System.out.println("[DBUtil] MySQL 驱动加载成功");
        } catch (ClassNotFoundException e) {
            System.err.println("[DBUtil] ❌ MySQL 驱动加载失败！请检查 mysql-connector-j.jar 是否在 lib 目录中");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("[DBUtil] ✅ 数据库连接成功！URL: " + URL);
            return conn;
        } catch (SQLException e) {
            System.err.println("[DBUtil] ❌ 数据库连接失败！");
            System.err.println("[DBUtil] 可能原因：");
            System.err.println("[DBUtil]   1. MySQL 服务未启动");
            System.err.println("[DBUtil]   2. 用户名或密码错误（当前用户: " + USERNAME + "）");
            System.err.println("[DBUtil]   3. 数据库 'library_system' 不存在");
            System.err.println("[DBUtil]   4. 端口 3306 被占用或无法访问");
            System.err.println("[DBUtil] 详细错误: " + e.getMessage());
            throw e;
        }
    }

    public static void close(AutoCloseable... resources) {
        for (AutoCloseable resource : resources) {
            if (resource != null) {
                try { resource.close(); } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }

    /**
     * 测试数据库连接是否正常
     * @return true 表示连接成功，false 表示连接失败
     */
    public static boolean testConnection() {
        Connection conn = null;
        try {
            conn = getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("[DBUtil] ✅ 数据库连接测试通过！");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[DBUtil] ❌ 数据库连接测试失败！");
        } finally {
            close(conn);
        }
        return false;
    }
}
