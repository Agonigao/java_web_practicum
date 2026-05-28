package com.library.filter;

import com.library.entity.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 安全权限控制过滤器 - 统一进行用户登录认证与角色越权校验
 */
@WebFilter("/*")
public class SecurityFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        
        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();
        String path = uri.substring(contextPath.length());
        
        // 1. 放行静态资源和公开页面
        if (path.startsWith("/static/") || 
            path.endsWith(".css") || 
            path.endsWith(".js") || 
            path.endsWith(".png") || 
            path.endsWith(".jpg") || 
            path.endsWith(".jpeg") || 
            path.endsWith(".gif") || 
            path.endsWith(".ico") ||
            "/".equals(path) || 
            "/index.jsp".equals(path) || 
            "/login".equals(path) || 
            "/register".equals(path) || 
            "/captcha".equals(path)) {
            
            chain.doFilter(request, response);
            return;
        }
        
        // 2. 检查用户是否已登录
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            // 未登录，重定向到登录页面
            resp.sendRedirect(contextPath + "/login?msg=nologin");
            return;
        }
        
        String role = user.getRole();
        
        // 3. 越权校验：管理员专用路径限制
        if (path.startsWith("/admin/") || path.startsWith("/book/") || 
            path.equals("/borrow/list") || path.startsWith("/borrow/return")) {
            
            if (!"admin".equals(role)) {
                // 非管理员越权访问管理端，拒绝请求，重定向至读者图书查询页并提示无权
                resp.sendRedirect(contextPath + "/reader/book?msg=unauthorized");
                return;
            }
        }
        
        // 4. 越权校验：读者专用路径限制
        if (path.startsWith("/reader/")) {
            if (!"reader".equals(role) && !"admin".equals(role)) {
                resp.sendRedirect(contextPath + "/login?msg=unauthorized");
                return;
            }
        }
        
        // 校验通过，继续过滤器链
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
