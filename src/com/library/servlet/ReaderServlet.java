package com.library.servlet;

import com.library.dao.ReaderInfoDAO;
import com.library.dao.BookInfoDAO;
import com.library.dao.BorrowRecordDAO;
import com.library.entity.ReaderInfo;
import com.library.entity.BookInfo;
import com.library.entity.BorrowRecord;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 读者个人中心 Servlet
 */
@WebServlet("/reader/*")
public class ReaderServlet extends HttpServlet {
    private ReaderInfoDAO readerInfoDAO = new ReaderInfoDAO();
    private BookInfoDAO bookInfoDAO = new BookInfoDAO();
    private BorrowRecordDAO borrowRecordDAO = new BorrowRecordDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || "/book".equals(pathInfo)) {
            showBookQuery(request, response);
        } else if ("/myborrow".equals(pathInfo)) {
            showMyBorrow(request, response);
        } else if ("/profile".equals(pathInfo)) {
            showProfile(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("update".equals(action)) {
            updateProfile(request, response);
        }
    }

    private void showBookQuery(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        List<BookInfo> books = bookInfoDAO.findAll();
        request.setAttribute("books", books);
        request.getRequestDispatcher("/WEB-INF/views/reader/book_query.jsp").forward(request, response);
    }

    private void showMyBorrow(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int userId = (int) request.getSession().getAttribute("userId");
        ReaderInfo reader = readerInfoDAO.findByUserId(userId);
        if (reader != null) {
            List<BorrowRecord> records = borrowRecordDAO.findByReaderId(reader.getId());
            request.setAttribute("records", records);
        }
        request.getRequestDispatcher("/WEB-INF/views/reader/my_borrow.jsp").forward(request, response);
    }

    private void showProfile(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int userId = (int) request.getSession().getAttribute("userId");
        ReaderInfo reader = readerInfoDAO.findByUserId(userId);
        request.setAttribute("reader", reader);
        request.getRequestDispatcher("/WEB-INF/views/reader/profile.jsp").forward(request, response);
    }

    private void updateProfile(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int userId = (int) request.getSession().getAttribute("userId");
        
        ReaderInfo reader = readerInfoDAO.findByUserId(userId);
        reader.setRealName(request.getParameter("realName"));
        reader.setPhone(request.getParameter("phone"));
        reader.setEmail(request.getParameter("email"));
        reader.setDepartment(request.getParameter("department"));
        
        readerInfoDAO.update(reader);
        response.sendRedirect(request.getContextPath() + "/reader/profile?msg=updated");
    }
}
