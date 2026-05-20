package com.library.servlet;

import com.library.dao.BorrowRecordDAO;
import com.library.dao.BookInfoDAO;
import com.library.entity.BorrowRecord;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 借阅管理 Servlet
 */
@WebServlet("/borrow/*")
public class BorrowServlet extends HttpServlet {
    private BorrowRecordDAO borrowRecordDAO = new BorrowRecordDAO();
    private BookInfoDAO bookInfoDAO = new BookInfoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || "/list".equals(pathInfo)) {
            listBorrows(request, response);
        } else if ("/borrow".equals(pathInfo)) {
            showBorrowForm(request, response);
        } else if ("/return".equals(pathInfo)) {
            showReturnForm(request, response);
        } else if ("/renew".equals(pathInfo)) {
            renewBook(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("borrow".equals(action)) {
            doBorrow(request, response);
        } else if ("return".equals(action)) {
            doReturn(request, response);
        }
    }

    private void listBorrows(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        List<BorrowRecord> records = borrowRecordDAO.findAll();
        request.setAttribute("records", records);
        request.getRequestDispatcher("/WEB-INF/views/borrow/list.jsp").forward(request, response);
    }

    private void showBorrowForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int bookId = Integer.parseInt(request.getParameter("bookId"));
        request.setAttribute("book", bookInfoDAO.findById(bookId));
        request.getRequestDispatcher("/WEB-INF/views/borrow/borrow.jsp").forward(request, response);
    }

    private void showReturnForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int recordId = Integer.parseInt(request.getParameter("recordId"));
        request.setAttribute("record", borrowRecordDAO.findById(recordId));
        request.getRequestDispatcher("/WEB-INF/views/borrow/return.jsp").forward(request, response);
    }

    private void doBorrow(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int readerId = Integer.parseInt(request.getParameter("readerId"));
        int bookId = Integer.parseInt(request.getParameter("bookId"));
        
        BorrowRecord record = new BorrowRecord();
        record.setReaderId(readerId);
        record.setBookId(bookId);
        
        boolean success = borrowRecordDAO.borrow(record);
        
        if (success) {
            response.sendRedirect(request.getContextPath() + "/borrow/list?msg=borrow_success");
        } else {
            response.sendRedirect(request.getContextPath() + "/borrow/list?msg=borrow_failed");
        }
    }

    private void doReturn(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int recordId = Integer.parseInt(request.getParameter("recordId"));
        
        boolean success = borrowRecordDAO.returnBook(recordId);
        
        if (success) {
            response.sendRedirect(request.getContextPath() + "/borrow/list?msg=return_success");
        } else {
            response.sendRedirect(request.getContextPath() + "/borrow/list?msg=return_failed");
        }
    }

    private void renewBook(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        Integer recordId = Integer.valueOf(request.getParameter("recordId"));
        
        // 续借30天
        boolean success = borrowRecordDAO.renew(recordId, Integer.valueOf(30));
        
        if (success) {
            response.sendRedirect(request.getContextPath() + "/borrow/list?msg=renew_success");
        } else {
            response.sendRedirect(request.getContextPath() + "/borrow/list?msg=renew_failed");
        }
    }
}
