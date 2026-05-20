package com.library.dao;

import com.library.entity.BorrowRecord;
import com.library.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 图书借阅记录 DAO
 */
public class BorrowRecordDAO {
    
    /**
     * 创建借阅记录（借书）
     */
    public boolean borrow(BorrowRecord record) {
        Connection conn = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);
            
            // 1. 减少图书可借数量
            String sql1 = "UPDATE book_info SET available_count = available_count - 1 WHERE id = ? AND available_count > 0";
            pstmt1 = conn.prepareStatement(sql1);
            pstmt1.setInt(1, record.getBookId());
            if (pstmt1.executeUpdate() == 0) {
                conn.rollback();
                return false;
            }
            
            // 2. 创建借阅记录
            String sql2 = "INSERT INTO borrow_record (reader_id, book_id, borrow_date, due_date, status, renew_count) VALUES (?, ?, ?, ?, 0, 0)";
            pstmt2 = conn.prepareStatement(sql2);
            pstmt2.setInt(1, record.getReaderId());
            pstmt2.setInt(2, record.getBookId());
            pstmt2.setTimestamp(3, record.getBorrowDate());
            pstmt2.setTimestamp(4, record.getDueDate());
            pstmt2.executeUpdate();
            
            conn.commit();
            return true;
            
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
        } finally {
            DBUtil.close(pstmt2, pstmt1, conn);
        }
        return false;
    }
    
    /**
     * 归还图书（更新借阅状态）
     */
    public boolean returnBook(Integer recordId) {
        String sql = "UPDATE borrow_record SET status = 1, return_date = NOW() WHERE id = ? AND status = 0";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, recordId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * 续借图书
     */
    public boolean renew(Integer recordId, Integer limitDays) {
        String sql = "UPDATE borrow_record SET due_date = DATE_ADD(due_date, INTERVAL ? DAY), renew_count = renew_count + 1 WHERE id = ? AND status = 0";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, limitDays);
            pstmt.setInt(2, recordId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public BorrowRecord findById(Integer id) {
        String sql = "SELECT br.*, ri.real_name, bi.title AS book_title, bi.author AS book_author " +
                     "FROM borrow_record br " +
                     "LEFT JOIN reader_info ri ON br.reader_id = ri.id " +
                     "LEFT JOIN book_info bi ON br.book_id = bi.id " +
                     "WHERE br.id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return extractBorrowRecord(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 查询读者的借阅记录
     */
    public List<BorrowRecord> findByReaderId(Integer readerId) {
        return findByReaderIdAndStatus(readerId, null);
    }
    
    public List<BorrowRecord> findByReaderIdAndStatus(Integer readerId, Integer status) {
        List<BorrowRecord> records = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT br.*, ri.real_name, bi.title AS book_title, bi.author AS book_author " +
                                              "FROM borrow_record br " +
                                              "LEFT JOIN reader_info ri ON br.reader_id = ri.id " +
                                              "LEFT JOIN book_info bi ON br.book_id = bi.id " +
                                              "WHERE br.reader_id = ?");
        
        if (status != null) {
            sql.append(" AND br.status = ?");
        }
        
        sql.append(" ORDER BY br.borrow_date DESC");
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            
            pstmt.setInt(1, readerId);
            if (status != null) pstmt.setInt(2, status);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    records.add(extractBorrowRecord(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }
    
    /**
     * 查询所有借阅记录
     */
    public List<BorrowRecord> findAll() {
        List<BorrowRecord> records = new ArrayList<>();
        String sql = "SELECT br.*, ri.real_name, bi.title AS book_title, bi.author AS book_author " +
                     "FROM borrow_record br " +
                     "LEFT JOIN reader_info ri ON br.reader_id = ri.id " +
                     "LEFT JOIN book_info bi ON br.book_id = bi.id " +
                     "ORDER BY br.borrow_date DESC";
        
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                records.add(extractBorrowRecord(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }
    
    /**
     * 查询即将到期的借阅记录（未来指定天数内）
     */
    public List<BorrowRecord> findDueSoon(int days) {
        List<BorrowRecord> records = new ArrayList<>();
        String sql = "SELECT br.*, ri.real_name, bi.title AS book_title " +
                     "FROM borrow_record br " +
                     "LEFT JOIN reader_info ri ON br.reader_id = ri.id " +
                     "LEFT JOIN book_info bi ON br.book_id = bi.id " +
                     "WHERE br.status = 0 AND br.due_date BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL ? DAY)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, days);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    records.add(extractBorrowRecord(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }
    
    /**
     * 统计当前借阅中的数量
     */
    public int countBorrowing() {
        String sql = "SELECT COUNT(*) FROM borrow_record WHERE status = 0";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    private BorrowRecord extractBorrowRecord(ResultSet rs) throws SQLException {
        BorrowRecord record = new BorrowRecord();
        record.setId(rs.getInt("id"));
        record.setReaderId(rs.getInt("reader_id"));
        record.setReaderName(rs.getString("real_name"));
        record.setBookId(rs.getInt("book_id"));
        record.setBookTitle(rs.getString("book_title"));
        record.setBookAuthor(rs.getString("book_author"));
        record.setBorrowDate(rs.getTimestamp("borrow_date"));
        record.setDueDate(rs.getTimestamp("due_date"));
        record.setStatus(rs.getInt("status"));
        record.setRenewCount(rs.getInt("renew_count"));
        return record;
    }
}
