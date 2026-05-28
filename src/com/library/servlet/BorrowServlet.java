package com.library.servlet;

import com.library.dao.BorrowRecordDAO;
import com.library.dao.BookInfoDAO;
import com.library.dao.ReaderInfoDAO;
import com.library.dao.ReaderTypeDAO;
import com.library.entity.BorrowRecord;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

        String role = (String) request.getSession().getAttribute("role");
        if ("reader".equals(role)) {
            // 读者角色：获取当前读者信息
            Integer readerId = (Integer) request.getSession().getAttribute("readerId");

            // 如果 session 中没有 readerId，尝试从 userId 查询
            if (readerId == null) {
                Integer userId = (Integer) request.getSession().getAttribute("userId");
                if (userId != null) {
                    com.library.entity.ReaderInfo tempReader = readerInfoDAO.findByUserId(userId);
                    if (tempReader != null) {
                        readerId = tempReader.getId();
                        request.getSession().setAttribute("readerId", readerId);
                        request.getSession().setAttribute("readerTypeId", tempReader.getReaderTypeId());
                    }
                }
            }

            if (readerId != null) {
                request.setAttribute("reader", readerInfoDAO.findById(readerId));
                Integer readerTypeId = (Integer) request.getSession().getAttribute("readerTypeId");
                if (readerTypeId != null) {
                    request.setAttribute("readerType", readerTypeDAO.findById(readerTypeId));
                }
            }
        } else {
            // 管理员角色：获取所有读者列表
            request.setAttribute("readers", readerInfoDAO.findAll());
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
        try {
            String readerIdStr = request.getParameter("readerId");
            String bookIdStr = request.getParameter("bookId");

            if (readerIdStr == null || readerIdStr.trim().isEmpty() ||
                bookIdStr == null || bookIdStr.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/error?msg=missing_params");
                return;
            }

            int readerId = Integer.parseInt(readerIdStr);
            int bookId = Integer.parseInt(bookIdStr);

            BorrowRecord record = new BorrowRecord();
            record.setReaderId(readerId);
            record.setBookId(bookId);

            // 设置借阅时间为当前时间
            record.setBorrowDate(new Timestamp(System.currentTimeMillis()));

            // 设置应还时间为借阅时间之后30天
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, 30);
            record.setDueDate(new Timestamp(cal.getTimeInMillis()));

            boolean success = borrowRecordDAO.borrow(record);

            String role = (String) request.getSession().getAttribute("role");
            String redirectPath = "reader".equals(role) ? "/reader/myborrow" : "/borrow/list";

            if (success) {
                response.sendRedirect(request.getContextPath() + redirectPath + "?msg=borrow_success");
            } else {
                response.sendRedirect(request.getContextPath() + redirectPath + "?msg=borrow_failed");
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/error?msg=invalid_params");
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
