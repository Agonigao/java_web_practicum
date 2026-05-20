package com.library.entity;

import java.io.Serializable;
import java.sql.Date;

/**
 * 读者信息实体类
 */
public class ReaderInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer id;               // 读者ID
    private Integer userId;           // 关联用户表ID
    private Integer readerTypeId;     // 关联读者类型ID
    private String typeName;          // 读者类型名称（关联查询）
    private String realName;          // 真实姓名
    private String studentId;         // 学号/工号
    private String phone;             // 联系电话
    private String email;             // 邮箱
    private String department;        // 所在院系
    private Date registerDate;        // 注册日期
    
    public ReaderInfo() {}
    
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    
    public Integer getReaderTypeId() { return readerTypeId; }
    public void setReaderTypeId(Integer readerTypeId) { this.readerTypeId = readerTypeId; }
    
    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }
    
    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }
    
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public Date getRegisterDate() { return registerDate; }
    public void setRegisterDate(Date registerDate) { this.registerDate = registerDate; }
    
    @Override
    public String toString() {
        return "ReaderInfo{" +
                "id=" + id +
                ", realName='" + realName + '\'' +
                ", studentId='" + studentId + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}
