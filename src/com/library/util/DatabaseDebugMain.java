package com.library.util;

/**
 * 数据库调试主程序 - 用于快速检查数据库状态
 * 右键运行此类的 main 方法即可查看完整的数据库信息
 */
public class DatabaseDebugMain {
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   图书馆系统 - 数据库调试工具          ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        // 1. 检查数据库整体状态
        DBDebugUtil.printDatabaseStatus();
        
        // 2. 测试常见账号
        System.out.println("开始测试常用账号...\n");
        
        DBDebugUtil.testUserLogin("admin", "admin123");
        DBDebugUtil.testUserLogin("student1", "123456");
        DBDebugUtil.testUserLogin("zhangsan", "1234");
        
        System.out.println("\n💡 提示:");
        System.out.println("   - 如果看到 '用户不存在'，请执行 SQL 脚本初始化数据库");
        System.out.println("   - 如果看到 '密码不匹配'，请检查输入的密码是否正确");
        System.out.println("   - 如果看到 '状态异常'，请检查用户的 status 字段是否为 1");
        System.out.println("   - 所有错误信息都会输出到 Tomcat 控制台\n");
    }
}
