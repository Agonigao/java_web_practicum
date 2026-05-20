package com.library.dao;

import com.library.entity.BookInfo;
import com.library.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 图书信息 DAO
 */
public class BookInfoDAO {
    
    public boolean add(BookInfo book) {
        String sql = "INSERT INTO book_info (category_id, isbn, title, author, publisher, publish_date, price, total_count, available_count, location) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, book.getCategoryId());
            pstmt.setString(2, book.getIsbn());
            pstmt.setString(3, book.getTitle());
            pstmt.setString(4, book.getAuthor());
            pstmt.setString(5, book.getPublisher());
            pstmt.setDate(6, book.getPublishDate());
            pstmt.setBigDecimal(7, book.getPrice());
            pstmt.setInt(8, book.getTotalCount());
            pstmt.setInt(9, book.getAvailableCount());
            pstmt.setString(10, book.getLocation());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public BookInfo findById(Integer id) {
        String sql = "SELECT b.*, c.category_name FROM book_info b LEFT JOIN book_category c ON b.category_id = c.id WHERE b.id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return extractBookInfo(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<BookInfo> findAll() {
        return findByCondition(null, null);
    }
    
    public List<BookInfo> findByCondition(String keyword, Integer categoryId) {
        List<BookInfo> books = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT b.*, c.category_name FROM book_info b LEFT JOIN book_category c ON b.category_id = c.id WHERE 1=1");
        List<Object> params = new ArrayList<>();
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (b.title LIKE ? OR b.author LIKE ? OR b.isbn LIKE ?)");
            String likeKeyword = "%" + keyword + "%";
            params.add(likeKeyword);
            params.add(likeKeyword);
            params.add(likeKeyword);
        }
        
        if (categoryId != null && categoryId > 0) {
            sql.append(" AND b.category_id = ?");
            params.add(categoryId);
        }
        
        sql.append(" ORDER BY b.add_time DESC");
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    books.add(extractBookInfo(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }
    
    public boolean update(BookInfo book) {
        String sql = "UPDATE book_info SET category_id = ?, isbn = ?, title = ?, author = ?, publisher = ?, publish_date = ?, " +
                     "price = ?, total_count = ?, available_count = ?, location = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, book.getCategoryId());
            pstmt.setString(2, book.getIsbn());
            pstmt.setString(3, book.getTitle());
            pstmt.setString(4, book.getAuthor());
            pstmt.setString(5, book.getPublisher());
            pstmt.setDate(6, book.getPublishDate());
            pstmt.setBigDecimal(7, book.getPrice());
            pstmt.setInt(8, book.getTotalCount());
            pstmt.setInt(9, book.getAvailableCount());
            pstmt.setString(10, book.getLocation());
            pstmt.setInt(11, book.getId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean delete(Integer id) {
        String sql = "DELETE FROM book_info WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean decreaseAvailableCount(Integer bookId) {
        String sql = "UPDATE book_info SET available_count = available_count - 1 WHERE id = ? AND available_count > 0";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean increaseAvailableCount(Integer bookId) {
        String sql = "UPDATE book_info SET available_count = available_count + 1 WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * 统计图书总数
     */
    public int countTotal() {
        String sql = "SELECT COUNT(*) FROM book_info";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    /**
     * 统计图书总数（别名方法）
     */
    public int countAll() {
        return countTotal();
    }
    
    private BookInfo extractBookInfo(ResultSet rs) throws SQLException {
        BookInfo book = new BookInfo();
        book.setId(rs.getInt("id"));
        book.setCategoryId(rs.getInt("category_id"));
        book.setCategoryName(rs.getString("category_name"));
        book.setIsbn(rs.getString("isbn"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setPublisher(rs.getString("publisher"));
        book.setPublishDate(rs.getDate("publish_date"));
        book.setPrice(rs.getBigDecimal("price"));
        book.setTotalCount(rs.getInt("total_count"));
        book.setAvailableCount(rs.getInt("available_count"));
        book.setLocation(rs.getString("location"));
        book.setAddTime(rs.getTimestamp("add_time"));
        return book;
    }
}
