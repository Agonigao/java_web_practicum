package com.library.dao;

import com.library.entity.ReaderType;
import com.library.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 读者类型 DAO
 */
public class ReaderTypeDAO {
    
    public boolean add(ReaderType type) {
        String sql = "INSERT INTO reader_type (type_name, max_borrow_num, borrow_limit_days, renew_limit) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, type.getTypeName());
            pstmt.setInt(2, type.getMaxBorrowNum());
            pstmt.setInt(3, type.getBorrowLimitDays());
            pstmt.setInt(4, type.getRenewLimit());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public ReaderType findById(Integer id) {
        String sql = "SELECT * FROM reader_type WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return extractReaderType(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<ReaderType> findAll() {
        List<ReaderType> types = new ArrayList<>();
        String sql = "SELECT * FROM reader_type";
        
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                types.add(extractReaderType(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return types;
    }
    
    public boolean update(ReaderType type) {
        String sql = "UPDATE reader_type SET type_name = ?, max_borrow_num = ?, borrow_limit_days = ?, renew_limit = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, type.getTypeName());
            pstmt.setInt(2, type.getMaxBorrowNum());
            pstmt.setInt(3, type.getBorrowLimitDays());
            pstmt.setInt(4, type.getRenewLimit());
            pstmt.setInt(5, type.getId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean delete(Integer id) {
        String sql = "DELETE FROM reader_type WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private ReaderType extractReaderType(ResultSet rs) throws SQLException {
        ReaderType type = new ReaderType();
        type.setId(rs.getInt("id"));
        type.setTypeName(rs.getString("type_name"));
        type.setMaxBorrowNum(rs.getInt("max_borrow_num"));
        type.setBorrowLimitDays(rs.getInt("borrow_limit_days"));
        type.setRenewLimit(rs.getInt("renew_limit"));
        return type;
    }
}
