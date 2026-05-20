package com.library.servlet;

import com.library.util.CaptchaUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证码生成Servlet
 */
@WebServlet("/captcha")
public class CaptchaServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 设置响应头，禁止缓存
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");

        // 生成验证码并存入 Session
        String code = CaptchaUtil.generateCaptcha(response.getOutputStream());
        request.getSession().setAttribute("captchaCode", code);
    }
}
