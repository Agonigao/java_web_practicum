package com.library.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * 数据库调试工具 - 用于检查数据库状态和用户数据
 */
public class DBDebugUtil {
    
    /**
     * 打印数据库完整状态信息
     */
    public static void printDatabaseStatus() {
        System.out.println("\n========== 数据库状态检查 ==========");
        
        Connection conn = null;
        try {
            // 1. 测试连接
            System.out.println("\n【1】测试数据库连接...");
            conn = DBUtil.getConnection();
            System.out.println("✅ 数据库连接成功！");
            
            // 2. 获取数据库信息
            System.out.println("\n【2】数据库信息:");
            System.out.println("   URL: " + conn.getMetaData().getURL());
            System.out.println("   用户: " + conn.getMetaData().getUserName());
            System.out.println("   产品: " + conn.getMetaData().getDatabaseProductName());
            System.out.println("   版本: " + conn.getMetaData().getDatabaseProductVersion());
            
            // 3. 查询所有用户
            System.out.println("\n【3】用户表数据:");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, username, password, role, status FROM users ORDER BY id");
            
            int count = 0;
            while (rs.next()) {
                count++;
                System.out.println("   用户 #" + count + ":");
                System.out.println("     ID: " + rs.getInt("id"));
                System.out.println("     用户名: " + rs.getString("username"));
                System.out.println("     密码: " + rs.getString("password"));
                System.out.println("     角色: " + rs.getString("role"));
                System.out.println("     状态: " + rs.getInt("status") + (rs.getInt("status") == 1 ? " (正常)" : " (禁用)"));
                System.out.println("     ---");
            }
            
            if (count == 0) {
                System.out.println("   ⚠️  用户表为空！请执行 SQL 脚本初始化数据。");
            } else {
                System.out.println("   共 " + count + " 个用户");
            }
            
            // 4. 列出可用的测试账号
            System.out.println("\n【4】可用的测试账号:");
            rs = stmt.executeQuery("SELECT username, password, role FROM users WHERE status = 1");
            while (rs.next()) {
                System.out.println("   - " + rs.getString("role") + ": " + 
                                 rs.getString("username") + " / " + rs.getString("password"));
            }
            
            System.out.println("\n========== 检查完成 ==========\n");
            
        } catch (Exception e) {
            System.err.println("\n❌ 数据库检查失败!");
            System.err.println("错误类型: " + e.getClass().getName());
            System.err.println("错误消息: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.close(conn);
        }
    }
    
    /**
     * 测试特定用户的登录
     */
    public static void testUserLogin(String username, String password) {
        System.out.println("\n========== 用户登录测试 ==========");
        System.out.println("测试账号: " + username + " / " + password);
        
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT id, username, password, role, status FROM users WHERE username = '" + username + "'";
            
            System.out.println("\n执行SQL: " + sql);
            
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            if (rs.next()) {
                System.out.println("\n✅ 用户存在:");
                System.out.println("   ID: " + rs.getInt("id"));
                System.out.println("   用户名: " + rs.getString("username"));
                System.out.println("   数据库密码: " + rs.getString("password"));
                System.out.println("   输入密码: " + password);
                System.out.println("   密码匹配: " + rs.getString("password").equals(password));
                System.out.println("   角色: " + rs.getString("role"));
                System.out.println("   状态: " + rs.getInt("status"));
                
                if (rs.getString("password").equals(password) && rs.getInt("status") == 1) {
                    System.out.println("\n✅ 登录应该成功！");
                } else {
                    System.out.println("\n❌ 登录会失败，原因:");
                    if (!rs.getString("password").equals(password)) {
                        System.out.println("   - 密码不匹配");
                    }
                    if (rs.getInt("status") != 1) {
                        System.out.println("   - 用户状态异常（不是1）");
                    }
                }
            } else {
                System.out.println("\n❌ 用户不存在: " + username);
            }
            
        } catch (Exception e) {
            System.err.println("\n❌ 测试失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.close(conn);
        }
        
        System.out.println("========== 测试完成 ==========\n");
    }
}
