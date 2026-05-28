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
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 登录 Servlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();
    private ReaderInfoDAO readerInfoDAO = new ReaderInfoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 设置请求和响应的字符编码
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String captchaInput = request.getParameter("captcha");
        
        System.out.println("\n========== [LoginServlet] 登录请求 ==========");
        System.out.println("[LoginServlet] 用户名: " + username);
        System.out.println("[LoginServlet] 密码: " + password);
        System.out.println("[LoginServlet] 验证码输入: " + captchaInput);
        
        StringBuilder debugInfo = new StringBuilder();
        
        try {
            // 验证验证码
            String sessionCaptcha = (String) request.getSession().getAttribute("captchaCode");
            if (sessionCaptcha == null || !sessionCaptcha.equalsIgnoreCase(captchaInput)) {
                System.out.println("[LoginServlet] ❌ 验证码错误");
                System.out.println("[LoginServlet] 会话中的验证码: " + sessionCaptcha);
                System.out.println("[LoginServlet] 用户输入的验证码: " + captchaInput);
                
                debugInfo.append("<strong>错误详情：</strong><br>");
                debugInfo.append("- 错误类型：验证码错误<br>");
                debugInfo.append("- 会话验证码：" + (sessionCaptcha != null ? sessionCaptcha : "null(已过期)") + "<br>");
                debugInfo.append("- 输入验证码：" + captchaInput + "<br>");
                debugInfo.append("- 解决方案：请刷新页面重新输入验证码<br>");
                
                request.setAttribute("error", "验证码错误");
                request.setAttribute("debug_info", debugInfo.toString());
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
                return;
            }
            
            System.out.println("[LoginServlet] ✅ 验证码正确，开始验证用户...");
            debugInfo.append("<strong>调试信息：</strong><br>");
            debugInfo.append("- 验证码验证：通过<br>");
            debugInfo.append("- 用户名：" + username + "<br>");
            
            User user = userDAO.login(username.trim(), password);
            
            if (user != null) {
                System.out.println("[LoginServlet] ✅ 登录成功！准备跳转...");
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("userId", user.getId());
                session.setAttribute("username", user.getUsername());
                session.setAttribute("role", user.getRole());
                
                // 如果是读者角色，获取读者信息
                if ("reader".equals(user.getRole())) {
                    ReaderInfo reader = readerInfoDAO.findByUserId(user.getId());
                    if (reader != null) {
                        session.setAttribute("readerId", reader.getId());
                        session.setAttribute("readerTypeId", reader.getReaderTypeId());
                        session.setAttribute("realName", reader.getRealName());
                    }
                } else {
                    // 管理员用户也设置 realName
                    session.setAttribute("realName", user.getUsername());
                }

                // 根据角色跳转到不同页面
                if ("admin".equals(user.getRole())) {
                    System.out.println("[LoginServlet] 跳转到管理员后台");
                    response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                } else {
                    System.out.println("[LoginServlet] 跳转到读者页面");
                    response.sendRedirect(request.getContextPath() + "/reader/book");
                }
            } else {
                System.err.println("[LoginServlet] ❌ 登录失败：用户名或密码错误");
                System.err.println("[LoginServlet] 可能原因：");
                System.err.println("[LoginServlet]   1. 用户名不存在");
                System.err.println("[LoginServlet]   2. 密码错误");
                System.err.println("[LoginServlet]   3. 用户状态异常（status != 1）");
                System.err.println("[LoginServlet]   4. 数据库连接问题");
                
                debugInfo.append("- 登录结果：失败<br>");
                debugInfo.append("- 可能原因：<br>");
                debugInfo.append("&nbsp;&nbsp;1. 用户名不存在<br>");
                debugInfo.append("&nbsp;&nbsp;2. 密码错误<br>");
                debugInfo.append("&nbsp;&nbsp;3. 用户状态异常（status != 1）<br>");
                debugInfo.append("&nbsp;&nbsp;4. 数据库连接问题<br>");
                debugInfo.append("- 测试账号：<br>");
                debugInfo.append("&nbsp;&nbsp;管理员：admin / admin123<br>");
                debugInfo.append("&nbsp;&nbsp;读者：zhangsan / 1234<br>");
                debugInfo.append("- 建议操作：<br>");
                debugInfo.append("&nbsp;&nbsp;1. 检查用户名拼写是否正确<br>");
                debugInfo.append("&nbsp;&nbsp;2. 检查密码是否正确（注意大小写）<br>");
                debugInfo.append("&nbsp;&nbsp;3. 查看 Tomcat 控制台日志获取详细数据库错误<br>");
                debugInfo.append("&nbsp;&nbsp;4. 确认数据库中是否存在该用户<br>");
                
                request.setAttribute("error", "用户名或密码错误");
                request.setAttribute("debug_info", debugInfo.toString());
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            System.err.println("[LoginServlet] ❌ 发生未预期的异常！");
            System.err.println("[LoginServlet] 异常类型: " + e.getClass().getName());
            System.err.println("[LoginServlet] 异常消息: " + e.getMessage());
            e.printStackTrace();
            
            debugInfo.append("<strong style='color:red;'>系统异常：</strong><br>");
            debugInfo.append("- 异常类型：" + e.getClass().getName() + "<br>");
            debugInfo.append("- 异常消息：" + (e.getMessage() != null ? e.getMessage() : "未知错误") + "<br>");
            debugInfo.append("- 堆栈跟踪：<pre style='background:#f5f5f5;padding:10px;margin:5px 0;font-size:11px;overflow:auto;max-height:200px;'>");
            for (StackTraceElement element : e.getStackTrace()) {
                debugInfo.append(element.toString()).append("<br>");
            }
            debugInfo.append("</pre>");
            debugInfo.append("- 建议：请查看 Tomcat 控制台日志获取完整错误信息<br>");
            
            request.setAttribute("error", "服务器内部错误：" + e.getClass().getSimpleName());
            request.setAttribute("debug_info", debugInfo.toString());
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
        
        System.out.println("========== [LoginServlet] 登录请求处理完成 ==========\n");
    }
}
