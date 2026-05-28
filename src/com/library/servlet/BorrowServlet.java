package com.library.servlet;

import com.library.dao.BorrowRecordDAO;
import com.library.dao.BookInfoDAO;
import com.library.dao.ReaderInfoDAO;
import com.library.dao.ReaderTypeDAO;
import com.library.entity.BorrowRecord;
import com.library.entity.ReaderInfo;
import com.library.entity.ReaderType;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

/**
 * 借阅管理 Servlet
 */
@WebServlet("/borrow/*")
public class BorrowServlet extends HttpServlet {
    private BorrowRecordDAO borrowRecordDAO = new BorrowRecordDAO();
    private BookInfoDAO bookInfoDAO = new BookInfoDAO();
    private ReaderInfoDAO readerInfoDAO = new ReaderInfoDAO();
    private ReaderTypeDAO readerTypeDAO = new ReaderTypeDAO();

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
        
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        
        if ("reader".equals(role)) {
            Integer userId = (Integer) session.getAttribute("userId");
            if (userId != null) {
                ReaderInfo reader = readerInfoDAO.findByUserId(userId);
                request.setAttribute("reader", reader);
                if (reader != null) {
                    ReaderType readerType = readerTypeDAO.findById(reader.getReaderTypeId());
                    request.setAttribute("readerType", readerType);
                }
            }
        } else {
            // Admin can choose which reader to borrow the book for
            List<ReaderInfo> readers = readerInfoDAO.findAll();
            request.setAttribute("readers", readers);
        }
        
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
        
        // 获取读者类型以确定借阅限期天数
        ReaderInfo reader = readerInfoDAO.findById(readerId);
        int limitDays = 30; // 默认30天
        if (reader != null) {
            ReaderType readerType = readerTypeDAO.findById(reader.getReaderTypeId());
            if (readerType != null) {
                limitDays = readerType.getBorrowLimitDays();
            }
        }
        
        // 计算借阅时间和应还时间
        Timestamp borrowDate = new Timestamp(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        cal.setTime(borrowDate);
        cal.add(Calendar.DAY_OF_YEAR, limitDays);
        Timestamp dueDate = new Timestamp(cal.getTimeInMillis());
        
        record.setBorrowDate(borrowDate);
        record.setDueDate(dueDate);
        
        boolean success = borrowRecordDAO.borrow(record);

        String role = (String) request.getSession().getAttribute("role");
        String redirectPath = "reader".equals(role) ? "/reader/myborrow" : "/borrow/list";

        if (success) {
            response.sendRedirect(request.getContextPath() + redirectPath + "?msg=borrow_success");
        } else {
            response.sendRedirect(request.getContextPath() + redirectPath + "?msg=borrow_failed");
        }
    }

    private void doReturn(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int recordId = Integer.parseInt(request.getParameter("recordId"));
        
        boolean success = borrowRecordDAO.returnBook(recordId);

        String role = (String) request.getSession().getAttribute("role");
        String redirectPath = "reader".equals(role) ? "/reader/myborrow" : "/borrow/list";

        if (success) {
            response.sendRedirect(request.getContextPath() + redirectPath + "?msg=return_success");
        } else {
            response.sendRedirect(request.getContextPath() + redirectPath + "?msg=return_failed");
        }
    }

    private void renewBook(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        Integer recordId = Integer.valueOf(request.getParameter("recordId"));
        
        // 续借30天
        boolean success = borrowRecordDAO.renew(recordId, Integer.valueOf(30));

        String role = (String) request.getSession().getAttribute("role");
        String redirectPath = "reader".equals(role) ? "/reader/myborrow" : "/borrow/list";

        if (success) {
            response.sendRedirect(request.getContextPath() + redirectPath + "?msg=renew_success");
        } else {
            response.sendRedirect(request.getContextPath() + redirectPath + "?msg=renew_failed");
        }
    }
}
