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
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 个人信息管理 Servlet
 */
@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();
    private ReaderInfoDAO readerInfoDAO = new ReaderInfoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // 转发到个人信息修改页面
        request.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String realName = request.getParameter("realName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String newPassword = request.getParameter("newPassword");

        // 更新读者详细信息
        ReaderInfo readerInfo = readerInfoDAO.findByUserId(userId);
        if (readerInfo != null) {
            readerInfo.setRealName(realName);
            readerInfo.setEmail(email);
            readerInfo.setPhone(phone);
            readerInfoDAO.update(readerInfo);
        }
        
        // 如果输入了新密码则修改密码
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            userDAO.changePassword(userId, newPassword);
        }
        
        session.setAttribute("msg", "个人信息修改成功");
        
        response.sendRedirect(request.getContextPath() + "/profile");
    }
}
