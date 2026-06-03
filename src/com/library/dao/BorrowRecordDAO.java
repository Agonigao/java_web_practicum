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
     * 归还图书（更新借阅状态）- 兼容旧版本调用
     */
    public boolean returnBook(Integer recordId) {
        return returnBook(recordId, null);
    }

    /**
     * 归还图书（更新借阅状态，增加图书可借库存，计算逾期并插入归还记录）
     */
    public boolean returnBook(Integer recordId, Integer operatorId) {
        Connection conn = null;
        PreparedStatement pstmtFind = null;
        PreparedStatement pstmtUpdateRecord = null;
        PreparedStatement pstmtUpdateBook = null;
        PreparedStatement pstmtInsertReturn = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            // 1. 获取借阅记录信息
            String sqlFind = "SELECT reader_id, book_id, due_date, status FROM borrow_record WHERE id = ?";
            pstmtFind = conn.prepareStatement(sqlFind);
            pstmtFind.setInt(1, recordId);
            rs = pstmtFind.executeQuery();

            if (!rs.next()) {
                conn.rollback();
                return false;
            }

            int readerId = rs.getInt("reader_id");
            int bookId = rs.getInt("book_id");
            Timestamp dueDate = rs.getTimestamp("due_date");
            int status = rs.getInt("status");

            // 校验：如果已经归还，直接返回 false
            if (status != 0) {
                conn.rollback();
                return false;
            }

            // 2. 更新借阅记录状态为已归还 (status = 1)
            String sqlUpdateRecord = "UPDATE borrow_record SET status = 1 WHERE id = ?";
            pstmtUpdateRecord = conn.prepareStatement(sqlUpdateRecord);
            pstmtUpdateRecord.setInt(1, recordId);
            if (pstmtUpdateRecord.executeUpdate() == 0) {
                conn.rollback();
                return false;
            }

            // 3. 增加图书的可借库存数量
            String sqlUpdateBook = "UPDATE book_info SET available_count = available_count + 1 WHERE id = ?";
            pstmtUpdateBook = conn.prepareStatement(sqlUpdateBook);
            pstmtUpdateBook.setInt(1, bookId);
            if (pstmtUpdateBook.executeUpdate() == 0) {
                conn.rollback();
                return false;
            }

            // 4. 计算是否逾期及罚款
            Timestamp now = new Timestamp(System.currentTimeMillis());
            int isOverdue = 0;
            java.math.BigDecimal fineAmount = java.math.BigDecimal.ZERO;

            if (now.after(dueDate)) {
                isOverdue = 1;
                // 计算逾期天数
                long diffMs = now.getTime() - dueDate.getTime();
                long diffDays = diffMs / (1000 * 60 * 60 * 24);
                if (diffDays <= 0) {
                    diffDays = 1; // 至少按1天算
                }
                // 逾期每天罚款 1.00 元
                fineAmount = new java.math.BigDecimal(diffDays).multiply(new java.math.BigDecimal("1.00"));
            }

            // 5. 插入归还记录到 return_record
            String sqlInsertReturn = "INSERT INTO return_record (borrow_id, reader_id, book_id, return_date, is_overdue, fine_amount, operator_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
            pstmtInsertReturn = conn.prepareStatement(sqlInsertReturn);
            pstmtInsertReturn.setInt(1, recordId);
            pstmtInsertReturn.setInt(2, readerId);
            pstmtInsertReturn.setInt(3, bookId);
            pstmtInsertReturn.setTimestamp(4, now);
            pstmtInsertReturn.setInt(5, isOverdue);
            pstmtInsertReturn.setBigDecimal(6, fineAmount);
            if (operatorId != null) {
                pstmtInsertReturn.setInt(7, operatorId);
            } else {
                pstmtInsertReturn.setNull(7, java.sql.Types.INTEGER);
            }

            pstmtInsertReturn.executeUpdate();

            conn.commit();
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, pstmtFind, pstmtUpdateRecord, pstmtUpdateBook, pstmtInsertReturn, conn);
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
     * 查询所有未归还（借阅中）的记录
     */
    public List<BorrowRecord> findAll() {
        List<BorrowRecord> records = new ArrayList<>();
        String sql = "SELECT br.*, ri.real_name, bi.title AS book_title, bi.author AS book_author " +
                     "FROM borrow_record br " +
                     "LEFT JOIN reader_info ri ON br.reader_id = ri.id " +
                     "LEFT JOIN book_info bi ON br.book_id = bi.id " +
                     "WHERE br.status = 0 " +
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
