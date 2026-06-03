package com.library.dao;

import com.library.entity.ReturnRecord;
import com.library.util.DBUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 图书归还记录 DAO
 */
public class ReturnRecordDAO {
    
    /**
     * 添加归还记录
     */
    public boolean add(ReturnRecord record) {
        String sql = "INSERT INTO return_record (borrow_id, reader_id, book_id, return_date, is_overdue, fine_amount, operator_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, record.getBorrowId());
            pstmt.setInt(2, record.getReaderId());
            pstmt.setInt(3, record.getBookId());
            pstmt.setTimestamp(4, record.getReturnDate());
            pstmt.setInt(5, record.getIsOverdue());
            pstmt.setBigDecimal(6, record.getFineAmount());
            pstmt.setInt(7, record.getOperatorId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * 根据借阅ID查询归还记录
     */
    public ReturnRecord findByBorrowId(Integer borrowId) {
        String sql = "SELECT rr.*, ri.real_name, bi.title AS book_title, br.borrow_date " +
                     "FROM return_record rr " +
                     "LEFT JOIN reader_info ri ON rr.reader_id = ri.id " +
                     "LEFT JOIN book_info bi ON rr.book_id = bi.id " +
                     "LEFT JOIN borrow_record br ON rr.borrow_id = br.id " +
                     "WHERE rr.borrow_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, borrowId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return extractReturnRecord(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 查询读者的归还记录
     */
    public List<ReturnRecord> findByReaderId(Integer readerId) {
        List<ReturnRecord> records = new ArrayList<>();
        String sql = "SELECT rr.*, ri.real_name, bi.title AS book_title, br.borrow_date " +
                     "FROM return_record rr " +
                     "LEFT JOIN reader_info ri ON rr.reader_id = ri.id " +
                     "LEFT JOIN book_info bi ON rr.book_id = bi.id " +
                     "LEFT JOIN borrow_record br ON rr.borrow_id = br.id " +
                     "WHERE rr.reader_id = ? ORDER BY rr.return_date DESC";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, readerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    records.add(extractReturnRecord(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }
    
    /**
     * 查询所有归还记录
     */
    public List<ReturnRecord> findAll() {
        List<ReturnRecord> records = new ArrayList<>();
        String sql = "SELECT rr.*, ri.real_name, bi.title AS book_title, br.borrow_date " +
                     "FROM return_record rr " +
                     "LEFT JOIN reader_info ri ON rr.reader_id = ri.id " +
                     "LEFT JOIN book_info bi ON rr.book_id = bi.id " +
                     "LEFT JOIN borrow_record br ON rr.borrow_id = br.id " +
                     "ORDER BY rr.return_date DESC";
        
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                records.add(extractReturnRecord(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }
    
    /**
     * 查询逾期归还记录
     */
    public List<ReturnRecord> findOverdueRecords() {
        List<ReturnRecord> records = new ArrayList<>();
        String sql = "SELECT rr.*, ri.real_name, bi.title AS book_title, br.borrow_date " +
                     "FROM return_record rr " +
                     "LEFT JOIN reader_info ri ON rr.reader_id = ri.id " +
                     "LEFT JOIN book_info bi ON rr.book_id = bi.id " +
                     "LEFT JOIN borrow_record br ON rr.borrow_id = br.id " +
                     "WHERE rr.is_overdue = 1 ORDER BY rr.return_date DESC";
        
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                records.add(extractReturnRecord(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }
    
    /**
     * 统计逾期次数
     */
    public int countOverdue() {
        String sql = "SELECT COUNT(*) FROM return_record WHERE is_overdue = 1";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    private ReturnRecord extractReturnRecord(ResultSet rs) throws SQLException {
        ReturnRecord record = new ReturnRecord();
        record.setId(rs.getInt("id"));
        record.setBorrowId(rs.getInt("borrow_id"));
        record.setReaderId(rs.getInt("reader_id"));
        record.setReaderName(rs.getString("real_name"));
        record.setBookId(rs.getInt("book_id"));
        record.setBookTitle(rs.getString("book_title"));
        record.setReturnDate(rs.getTimestamp("return_date"));
        record.setIsOverdue(rs.getInt("is_overdue"));
        record.setFineAmount(rs.getBigDecimal("fine_amount"));
        record.setOperatorId(rs.getInt("operator_id"));
        record.setBorrowDate(rs.getTimestamp("borrow_date"));
        return record;
    }
}
