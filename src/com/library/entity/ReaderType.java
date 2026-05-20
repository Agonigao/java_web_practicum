package com.library.entity;

import java.io.Serializable;

/**
 * 读者类型实体类
 */
public class ReaderType implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer id;               // 类型ID
    private String typeName;          // 类型名称（本科生、教师等）
    private Integer maxBorrowNum;     // 最大可借数量
    private Integer borrowLimitDays;  // 默认借阅天数
    private Integer renewLimit;       // 最大续借次数
    
    public ReaderType() {}
    
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }
    
    public Integer getMaxBorrowNum() { return maxBorrowNum; }
    public void setMaxBorrowNum(Integer maxBorrowNum) { this.maxBorrowNum = maxBorrowNum; }
    
    public Integer getBorrowLimitDays() { return borrowLimitDays; }
    public void setBorrowLimitDays(Integer borrowLimitDays) { this.borrowLimitDays = borrowLimitDays; }
    
    public Integer getRenewLimit() { return renewLimit; }
    public void setRenewLimit(Integer renewLimit) { this.renewLimit = renewLimit; }
    
    @Override
    public String toString() {
        return "ReaderType{" +
                "id=" + id +
                ", typeName='" + typeName + '\'' +
                ", maxBorrowNum=" + maxBorrowNum +
                ", borrowLimitDays=" + borrowLimitDays +
                '}';
    }
}
