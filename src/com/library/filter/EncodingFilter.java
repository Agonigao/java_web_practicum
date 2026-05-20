package com.library.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * 字符编码过滤器 - 统一处理所有请求的字符编码
 */
@WebFilter("/*")
public class EncodingFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // 设置请求和响应的字符编码为 UTF-8
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        
        // 继续过滤器链
        chain.doFilter(request, response);
    }
    
    @Override
    public void destroy() {
        // 销毁
    }
}
