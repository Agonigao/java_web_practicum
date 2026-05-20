package com.library.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 图书归还记录实体类
 */
public class ReturnRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer id;               // 归还ID
    private Integer borrowId;         // 关联借阅表ID
    private Integer readerId;         // 读者ID
    private String readerName;        // 读者姓名（关联查询）
    private Integer bookId;           // 图书ID
    private String bookTitle;         // 书名（关联查询）
    private Timestamp returnDate;     // 实际归还时间
    private Integer isOverdue;        // 是否逾期：0-否，1-是
    private BigDecimal fineAmount;    // 逾期罚款金额
    private Integer operatorId;       // 操作归还的管理员ID
    private String operatorName;      // 管理员姓名（关联查询）
    
    public ReturnRecord() {}
    
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public Integer getBorrowId() { return borrowId; }
    public void setBorrowId(Integer borrowId) { this.borrowId = borrowId; }
    
    public Integer getReaderId() { return readerId; }
    public void setReaderId(Integer readerId) { this.readerId = readerId; }
    
    public String getReaderName() { return readerName; }
    public void setReaderName(String readerName) { this.readerName = readerName; }
    
    public Integer getBookId() { return bookId; }
    public void setBookId(Integer bookId) { this.bookId = bookId; }
    
    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }
    
    public Timestamp getReturnDate() { return returnDate; }
    public void setReturnDate(Timestamp returnDate) { this.returnDate = returnDate; }
    
    public Integer getIsOverdue() { return isOverdue; }
    public void setIsOverdue(Integer isOverdue) { this.isOverdue = isOverdue; }
    
    public BigDecimal getFineAmount() { return fineAmount; }
    public void setFineAmount(BigDecimal fineAmount) { this.fineAmount = fineAmount; }
    
    public Integer getOperatorId() { return operatorId; }
    public void setOperatorId(Integer operatorId) { this.operatorId = operatorId; }
    
    public String getOperatorName() { return operatorName; }
    public void setOperatorName(String operatorName) { this.operatorName = operatorName; }
    
    @Override
    public String toString() {
        return "ReturnRecord{" +
                "id=" + id +
                ", readerName='" + readerName + '\'' +
                ", bookTitle='" + bookTitle + '\'' +
                ", isOverdue=" + isOverdue +
                '}';
    }
}
