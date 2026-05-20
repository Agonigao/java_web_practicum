package com.library.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 用户实体类 - 系统登录账号
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer id;              // 用户ID
    private String username;         // 登录账号
    private String password;         // 登录密码
    private String role;             // 角色：admin-管理员，reader-读者
    private Integer status;          // 状态：1-正常，0-禁用
    private Timestamp createTime;    // 创建时间
    
    public User() {}
    
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
    
    // Getter and Setter
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    
    public Timestamp getCreateTime() { return createTime; }
    public void setCreateTime(Timestamp createTime) { this.createTime = createTime; }
    
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
