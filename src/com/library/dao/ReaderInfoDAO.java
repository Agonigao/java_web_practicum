package com.library.dao;

import com.library.entity.ReaderInfo;
import com.library.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 读者信息 DAO
 */
public class ReaderInfoDAO {
    
    public boolean add(ReaderInfo reader) {
        String sql = "INSERT INTO reader_info (user_id, reader_type_id, real_name, student_id, phone, email, department, register_date) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, reader.getUserId());
            pstmt.setInt(2, reader.getReaderTypeId());
            pstmt.setString(3, reader.getRealName());
            pstmt.setString(4, reader.getStudentId());
            pstmt.setString(5, reader.getPhone());
            pstmt.setString(6, reader.getEmail());
            pstmt.setString(7, reader.getDepartment());
            pstmt.setDate(8, reader.getRegisterDate());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public ReaderInfo findById(Integer id) {
        String sql = "SELECT r.*, t.type_name FROM reader_info r LEFT JOIN reader_type t ON r.reader_type_id = t.id WHERE r.id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return extractReaderInfo(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public ReaderInfo findByUserId(Integer userId) {
        String sql = "SELECT r.*, t.type_name FROM reader_info r LEFT JOIN reader_type t ON r.reader_type_id = t.id WHERE r.user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return extractReaderInfo(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<ReaderInfo> findAll() {
        List<ReaderInfo> readers = new ArrayList<>();
        String sql = "SELECT r.*, t.type_name FROM reader_info r LEFT JOIN reader_type t ON r.reader_type_id = t.id ORDER BY r.register_date DESC";
        
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                readers.add(extractReaderInfo(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return readers;
    }
    
    public boolean update(ReaderInfo reader) {
        String sql = "UPDATE reader_info SET reader_type_id = ?, real_name = ?, student_id = ?, phone = ?, email = ?, department = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, reader.getReaderTypeId());
            pstmt.setString(2, reader.getRealName());
            pstmt.setString(3, reader.getStudentId());
            pstmt.setString(4, reader.getPhone());
            pstmt.setString(5, reader.getEmail());
            pstmt.setString(6, reader.getDepartment());
            pstmt.setInt(7, reader.getId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean delete(Integer id) {
        String sql = "DELETE FROM reader_info WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * 统计读者总数
     */
    public int countAll() {
        String sql = "SELECT COUNT(*) FROM reader_info";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    private ReaderInfo extractReaderInfo(ResultSet rs) throws SQLException {
        ReaderInfo reader = new ReaderInfo();
        reader.setId(rs.getInt("id"));
        reader.setUserId(rs.getInt("user_id"));
        reader.setReaderTypeId(rs.getInt("reader_type_id"));
        reader.setTypeName(rs.getString("type_name"));
        reader.setRealName(rs.getString("real_name"));
        reader.setStudentId(rs.getString("student_id"));
        reader.setPhone(rs.getString("phone"));
        reader.setEmail(rs.getString("email"));
        reader.setDepartment(rs.getString("department"));
        reader.setRegisterDate(rs.getDate("register_date"));
        return reader;
    }
}
