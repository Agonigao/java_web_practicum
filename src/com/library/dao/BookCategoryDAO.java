package com.library.dao;

import com.library.entity.BookCategory;
import com.library.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 图书类型 DAO
 */
public class BookCategoryDAO {
    
    public boolean add(BookCategory category) {
        String sql = "INSERT INTO book_category (category_name, description) VALUES (?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, category.getCategoryName());
            pstmt.setString(2, category.getDescription());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public BookCategory findById(Integer id) {
        String sql = "SELECT * FROM book_category WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    BookCategory category = new BookCategory();
                    category.setId(rs.getInt("id"));
                    category.setCategoryName(rs.getString("category_name"));
                    category.setDescription(rs.getString("description"));
                    return category;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<BookCategory> findAll() {
        List<BookCategory> categories = new ArrayList<>();
        String sql = "SELECT * FROM book_category";
        
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                BookCategory category = new BookCategory();
                category.setId(rs.getInt("id"));
                category.setCategoryName(rs.getString("category_name"));
                category.setDescription(rs.getString("description"));
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }
    
    public boolean update(BookCategory category) {
        String sql = "UPDATE book_category SET category_name = ?, description = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, category.getCategoryName());
            pstmt.setString(2, category.getDescription());
            pstmt.setInt(3, category.getId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean delete(Integer id) {
        String sql = "DELETE FROM book_category WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
