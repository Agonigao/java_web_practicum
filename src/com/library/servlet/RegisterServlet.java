package com.library.servlet;

import com.library.dao.UserDAO;
import com.library.dao.ReaderInfoDAO;
import com.library.entity.User;
import com.library.entity.ReaderInfo;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 注册 Servlet
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();
    private ReaderInfoDAO readerInfoDAO = new ReaderInfoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String realName = request.getParameter("realName");
        String studentId = request.getParameter("studentId");
        String readerTypeStr = request.getParameter("readerType");
        String department = request.getParameter("department");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        
        // 验证必填字段
        if (username == null || username.trim().isEmpty() || 
            password == null || password.trim().isEmpty() || 
            realName == null || realName.trim().isEmpty() || 
            studentId == null || studentId.trim().isEmpty() || 
            readerTypeStr == null || readerTypeStr.trim().isEmpty()) {
            request.setAttribute("error", "请填写所有必填字段");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
            return;
        }
        
        int readerTypeId;
        try {
            readerTypeId = Integer.parseInt(readerTypeStr.trim());
        } catch (NumberFormatException e) {
            request.setAttribute("error", "读者类型选择无效");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
            return;
        }
        
        // 验证密码
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "两次输入的密码不一致");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
            return;
        }
        
        // 检查用户名是否已存在
        if (userDAO.findByUsername(username.trim()) != null) {
            request.setAttribute("error", "用户名已存在");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
            return;
        }
        
        // 创建用户（默认角色为 reader）
        User user = new User();
        user.setUsername(username.trim());
        user.setPassword(password);
        user.setRole("reader");
        user.setStatus(1);
        
        boolean success = userDAO.register(user);
        
        if (success) {
            // 获取刚创建的用户ID
            User createdUser = userDAO.findByUsername(username.trim());
            
            // 创建读者信息
            ReaderInfo readerInfo = new ReaderInfo();
            readerInfo.setUserId(createdUser.getId());
            readerInfo.setRealName(realName);
            readerInfo.setStudentId(studentId);
            readerInfo.setReaderTypeId(readerTypeId);
            readerInfo.setDepartment(department != null ? department : "");
            readerInfo.setPhone(phone != null ? phone : "");
            readerInfo.setEmail(email != null ? email : "");
            
            readerInfoDAO.add(readerInfo);
            
            response.sendRedirect(request.getContextPath() + "/login?msg=registered");
        } else {
            request.setAttribute("error", "注册失败，请重试");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
        }
    }
}
