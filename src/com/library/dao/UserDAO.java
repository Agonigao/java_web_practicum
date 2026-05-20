package com.library.dao;

import com.library.entity.User;
import com.library.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户 DAO - 系统登录账号管理
 */
public class UserDAO {
    
    /**
     * 用户登录
     */
    public User login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ? AND status = 1";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            System.out.println("[UserDAO] 尝试登录 - 用户名: " + username);
            conn = DBUtil.getConnection();
            System.out.println("[UserDAO] ✅ 数据库连接成功");
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            System.out.println("[UserDAO] 执行SQL: " + sql);
            System.out.println("[UserDAO] 参数: username=" + username + ", password=" + password);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                User user = extractUser(rs);
                System.out.println("[UserDAO] ✅ 登录成功! 用户ID: " + user.getId() + ", 角色: " + user.getRole());
                return user;
            } else {
                System.out.println("[UserDAO] ❌ 登录失败: 用户名或密码错误，或用户状态异常");
                
                // 调试：检查用户名是否存在
                checkUserExists(username);
            }
        } catch (SQLException e) {
            System.err.println("[UserDAO] ❌ 数据库查询异常!");
            System.err.println("[UserDAO] 错误类型: " + e.getClass().getName());
            System.err.println("[UserDAO] 错误消息: " + e.getMessage());
            System.err.println("[UserDAO] SQL状态: " + e.getSQLState());
            System.err.println("[UserDAO] 错误代码: " + e.getErrorCode());
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, pstmt, conn);
        }
        return null;
    }
    
    /**
     * 检查用户是否存在（用于调试）
     */
    private void checkUserExists(String username) {
        String checkSql = "SELECT id, username, role, status FROM users WHERE username = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(checkSql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("[UserDAO] 调试信息 - 用户存在:");
                System.out.println("[UserDAO]   用户ID: " + rs.getInt("id"));
                System.out.println("[UserDAO]   用户名: " + rs.getString("username"));
                System.out.println("[UserDAO]   角色: " + rs.getString("role"));
                System.out.println("[UserDAO]   状态: " + rs.getInt("status") + " (1=正常, 0=禁用)");
                System.out.println("[UserDAO] ⚠️  请检查密码是否正确，或用户状态是否为1");
            } else {
                System.out.println("[UserDAO] 调试信息 - 用户不存在: " + username);
                System.out.println("[UserDAO] ⚠️  请检查用户名是否正确，或是否已注册");
            }
        } catch (SQLException e) {
            System.err.println("[UserDAO] 检查用户存在性时出错: " + e.getMessage());
        } finally {
            DBUtil.close(rs, pstmt, conn);
        }
    }
    
    /**
     * 用户注册
     */
    public boolean register(User user) {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, 'reader')";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * 根据ID查询用户
     */
    public User findById(Integer id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return extractUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 根据用户名查询用户
     */
    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return extractUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 查询所有用户
     */
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY create_time DESC";
        
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                users.add(extractUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    
    /**
     * 更新用户信息
     */
    public boolean update(User user) {
        String sql = "UPDATE users SET username = ?, password = ?, role = ?, status = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getRole());
            pstmt.setInt(4, user.getStatus());
            pstmt.setInt(5, user.getId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * 修改密码
     */
    public boolean changePassword(Integer userId, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, newPassword);
            pstmt.setInt(2, userId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * 删除用户
     */
    public boolean delete(Integer id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * 从结果集中提取用户对象
     */
    private User extractUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setRole(rs.getString("role"));
        user.setStatus(rs.getInt("status"));
        user.setCreateTime(rs.getTimestamp("create_time"));
        return user;
    }
}
