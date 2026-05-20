package com.library.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * 图书信息实体类
 */
public class BookInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer id;               // 图书ID
    private Integer categoryId;       // 图书类型ID
    private String categoryName;      // 类型名称（关联查询）
    private String isbn;              // ISBN号
    private String title;             // 书名
    private String author;            // 作者
    private String publisher;         // 出版社
    private Date publishDate;         // 出版日期
    private BigDecimal price;         // 价格
    private Integer totalCount;       // 总库存
    private Integer availableCount;   // 可借库存
    private String location;          // 存放位置
    private Timestamp addTime;        // 入库时间
    
    public BookInfo() {}
    
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }
    
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    
    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    
    public Date getPublishDate() { return publishDate; }
    public void setPublishDate(Date publishDate) { this.publishDate = publishDate; }
    
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    
    public Integer getTotalCount() { return totalCount; }
    public void setTotalCount(Integer totalCount) { this.totalCount = totalCount; }
    
    public Integer getAvailableCount() { return availableCount; }
    public void setAvailableCount(Integer availableCount) { this.availableCount = availableCount; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public Timestamp getAddTime() { return addTime; }
    public void setAddTime(Timestamp addTime) { this.addTime = addTime; }
    
    @Override
    public String toString() {
        return "BookInfo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", availableCount=" + availableCount +
                '}';
    }
}
