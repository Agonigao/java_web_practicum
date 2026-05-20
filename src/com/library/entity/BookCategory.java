package com.library.entity;

import java.io.Serializable;

/**
 * 图书类型实体类
 */
public class BookCategory implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer id;               // 类型ID
    private String categoryName;      // 类型名称
    private String description;       // 类型描述
    
    public BookCategory() {}
    
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    @Override
    public String toString() {
        return "BookCategory{" +
                "id=" + id +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }
}
