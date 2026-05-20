package com.library.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 图书借阅记录实体类
 */
public class BorrowRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer id;               // 借阅ID
    private Integer readerId;         // 读者ID
    private String readerName;        // 读者姓名（关联查询）
    private Integer bookId;           // 图书ID
    private String bookTitle;         // 书名（关联查询）
    private String bookAuthor;        // 作者（关联查询）
    private Timestamp borrowDate;     // 借阅时间
    private Timestamp dueDate;        // 应还时间
    private Integer status;           // 状态：0-借阅中，1-已归还，2-逾期
    private Integer renewCount;       // 续借次数
    
    public BorrowRecord() {}
    
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public Integer getReaderId() { return readerId; }
    public void setReaderId(Integer readerId) { this.readerId = readerId; }
    
    public String getReaderName() { return readerName; }
    public void setReaderName(String readerName) { this.readerName = readerName; }
    
    public Integer getBookId() { return bookId; }
    public void setBookId(Integer bookId) { this.bookId = bookId; }
    
    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }
    
    public String getBookAuthor() { return bookAuthor; }
    public void setBookAuthor(String bookAuthor) { this.bookAuthor = bookAuthor; }
    
    public Timestamp getBorrowDate() { return borrowDate; }
    public void setBorrowDate(Timestamp borrowDate) { this.borrowDate = borrowDate; }
    
    public Timestamp getDueDate() { return dueDate; }
    public void setDueDate(Timestamp dueDate) { this.dueDate = dueDate; }
    
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    
    public Integer getRenewCount() { return renewCount; }
    public void setRenewCount(Integer renewCount) { this.renewCount = renewCount; }
    
    @Override
    public String toString() {
        return "BorrowRecord{" +
                "id=" + id +
                ", readerName='" + readerName + '\'' +
                ", bookTitle='" + bookTitle + '\'' +
                ", status=" + status +
                '}';
    }
}
