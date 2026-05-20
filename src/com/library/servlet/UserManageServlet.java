package com.library.servlet;

import com.library.dao.ReaderInfoDAO;
import com.library.dao.UserDAO;
import com.library.entity.ReaderInfo;
import com.library.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 用户管理 Servlet (管理员权限)
 */
@WebServlet("/admin/user")
public class UserManageServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();
    private ReaderInfoDAO readerInfoDAO = new ReaderInfoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if ("delete".equals(action)) {
            deleteUser(request, response);
        } else {
            // 默认查询所有用户
            List<User> users = userDAO.findAll();
            request.setAttribute("users", users);
            request.getRequestDispatcher("/WEB-INF/views/admin/user_list.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("add".equals(action)) {
            addUser(request, response);
        } else if ("update".equals(action)) {
            updateUser(request, response);
        }
    }

    private void addUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String realName = request.getParameter("realName");
        String role = request.getParameter("role");
        
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);
        
        // 先创建用户
        if (userDAO.register(user)) {
            // 获取刚创建的用户ID，然后创建读者信息
            User createdUser = userDAO.findByUsername(username);
            if (createdUser != null) {
                ReaderInfo readerInfo = new ReaderInfo();
                readerInfo.setUserId(createdUser.getId());
                readerInfo.setRealName(realName);
                readerInfoDAO.add(readerInfo);  // 使用 add 方法
            }
            response.sendRedirect(request.getContextPath() + "/admin/user?msg=success");
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/user?msg=error");
        }
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        userDAO.delete(id);
        response.sendRedirect(request.getContextPath() + "/admin/user");
    }
    
    private void updateUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 实现更新逻辑
        response.sendRedirect(request.getContextPath() + "/admin/user");
    }
}
