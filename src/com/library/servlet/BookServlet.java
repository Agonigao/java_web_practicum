package com.library.servlet;

import com.library.dao.BookInfoDAO;
import com.library.dao.BookCategoryDAO;
import com.library.entity.BookInfo;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

/**
 * 图书管理 Servlet
 */
@WebServlet("/book/*")
public class BookServlet extends HttpServlet {
    private BookInfoDAO bookInfoDAO = new BookInfoDAO();
    private BookCategoryDAO categoryDAO = new BookCategoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || "/list".equals(pathInfo)) {
            listBooks(request, response);
        } else if ("/add".equals(pathInfo)) {
            showAddForm(request, response);
        } else if ("/edit".equals(pathInfo)) {
            showEditForm(request, response);
        } else if ("/delete".equals(pathInfo)) {
            deleteBook(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("add".equals(action)) {
            addBook(request, response);
        } else if ("update".equals(action)) {
            updateBook(request, response);
        }
    }

    private void listBooks(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        List<BookInfo> books = bookInfoDAO.findAll();
        request.setAttribute("books", books);
        request.getRequestDispatcher("/WEB-INF/views/admin/book_list.jsp").forward(request, response);
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.setAttribute("categories", categoryDAO.findAll());
        request.getRequestDispatcher("/WEB-INF/views/admin/book_add.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        BookInfo book = bookInfoDAO.findById(id);
        request.setAttribute("book", book);
        request.setAttribute("categories", categoryDAO.findAll());
        request.getRequestDispatcher("/WEB-INF/views/admin/book_edit.jsp").forward(request, response);
    }

    private void addBook(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        BookInfo book = new BookInfo();
        book.setIsbn(request.getParameter("isbn"));
        book.setTitle(request.getParameter("title"));
        book.setAuthor(request.getParameter("author"));
        book.setPublisher(request.getParameter("publisher"));
        
        // 将 String 转换为 java.sql.Date
        String publishDateStr = request.getParameter("publishDate");
        if (publishDateStr != null && !publishDateStr.trim().isEmpty()) {
            book.setPublishDate(java.sql.Date.valueOf(publishDateStr));
        }
        
        book.setCategoryId(Integer.parseInt(request.getParameter("categoryId")));
        book.setTotalCount(Integer.parseInt(request.getParameter("totalCount")));
        book.setAvailableCount(Integer.parseInt(request.getParameter("availableCount")));
        book.setLocation(request.getParameter("location"));
        
        bookInfoDAO.add(book);
        response.sendRedirect(request.getContextPath() + "/book/list");
    }

    private void updateBook(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        BookInfo book = new BookInfo();
        book.setId(Integer.parseInt(request.getParameter("id")));
        book.setIsbn(request.getParameter("isbn"));
        book.setTitle(request.getParameter("title"));
        book.setAuthor(request.getParameter("author"));
        book.setPublisher(request.getParameter("publisher"));
        
        // 将 String 转换为 java.sql.Date
        String publishDateStr = request.getParameter("publishDate");
        if (publishDateStr != null && !publishDateStr.trim().isEmpty()) {
            book.setPublishDate(java.sql.Date.valueOf(publishDateStr));
        }
        
        book.setCategoryId(Integer.parseInt(request.getParameter("categoryId")));
        book.setTotalCount(Integer.parseInt(request.getParameter("totalCount")));
        book.setAvailableCount(Integer.parseInt(request.getParameter("availableCount")));
        book.setLocation(request.getParameter("location"));
        
        bookInfoDAO.update(book);
        response.sendRedirect(request.getContextPath() + "/book/list");
    }

    private void deleteBook(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        bookInfoDAO.delete(id);
        response.sendRedirect(request.getContextPath() + "/book/list");
    }
}
