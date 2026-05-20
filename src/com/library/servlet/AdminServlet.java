package com.library.servlet;

import com.library.dao.*;
import com.library.entity.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 管理员 Servlet - 处理仪表盘、用户管理、读者管理等
 */
@WebServlet("/admin/*")
public class AdminServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();
    private ReaderInfoDAO readerInfoDAO = new ReaderInfoDAO();
    private BookInfoDAO bookInfoDAO = new BookInfoDAO();
    private BorrowRecordDAO borrowRecordDAO = new BorrowRecordDAO();
    private ReturnRecordDAO returnRecordDAO = new ReturnRecordDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || "/dashboard".equals(pathInfo)) {
            showDashboard(request, response);
        } else if ("/user".equals(pathInfo)) {
            showUserList(request, response);
        } else if ("/reader".equals(pathInfo)) {
            showReaderList(request, response);
        } else if ("/book".equals(pathInfo)) {
            showBookList(request, response);
        } else if ("/category".equals(pathInfo)) {
            showCategoryList(request, response);
        } else if ("/return".equals(pathInfo)) {
            showReturnList(request, response);
        } else if ("/user/delete".equals(pathInfo)) {
            deleteUser(request, response);
        }
    }

    private void showDashboard(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            // 统计数据
            int totalBooks = bookInfoDAO.countAll();
            int borrowingCount = borrowRecordDAO.countBorrowing();
            int totalReaders = readerInfoDAO.countAll();
            int overdueCount = returnRecordDAO.countOverdue();
            
            // 即将到期的记录
            List<BorrowRecord> dueSoonRecords = borrowRecordDAO.findDueSoon(3);
            
            request.setAttribute("totalBooks", totalBooks);
            request.setAttribute("borrowingCount", borrowingCount);
            request.setAttribute("totalReaders", totalReaders);
            request.setAttribute("overdueCount", overdueCount);
            request.setAttribute("dueSoonRecords", dueSoonRecords);
            
            request.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("[AdminServlet] ❌ dashboard 错误: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "加载仪表盘数据失败: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error/500.jsp").forward(request, response);
        }
    }

    private void showUserList(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        List<User> users = userDAO.findAll();
        request.setAttribute("users", users);
        request.getRequestDispatcher("/WEB-INF/views/admin/user_list.jsp").forward(request, response);
    }

    private void showReaderList(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        List<ReaderInfo> readers = readerInfoDAO.findAll();
        request.setAttribute("readers", readers);
        request.getRequestDispatcher("/WEB-INF/views/admin/reader_list.jsp").forward(request, response);
    }

    private void showBookList(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        List<BookInfo> books = bookInfoDAO.findAll();
        request.setAttribute("books", books);
        request.getRequestDispatcher("/WEB-INF/views/admin/book_list.jsp").forward(request, response);
    }

    private void showCategoryList(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        List<BookCategory> categories = new BookCategoryDAO().findAll();
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/WEB-INF/views/admin/category_list.jsp").forward(request, response);
    }

    private void showReturnList(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        List<ReturnRecord> returns = returnRecordDAO.findAll();
        request.setAttribute("returns", returns);
        request.getRequestDispatcher("/WEB-INF/views/admin/return_list.jsp").forward(request, response);
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        userDAO.delete(id);
        response.sendRedirect(request.getContextPath() + "/admin/user");
    }
}
