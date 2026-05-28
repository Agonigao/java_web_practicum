package com.library.servlet;

import com.library.dao.BorrowRecordDAO;
import com.library.entity.BorrowRecord;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 处理图书续借请求
 */
@WebServlet("/borrow/renew")
public class BorrowRenewServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(BorrowRenewServlet.class.getName());
    private final BorrowRecordDAO borrowRecordDAO = new BorrowRecordDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        handleRenew(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        handleRenew(request, response);
    }

    private void handleRenew(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String recordIdStr = request.getParameter("recordId");
        if (recordIdStr == null) {
            response.sendRedirect(request.getContextPath() + "/borrow?my");
            return;
        }

        try {
            Integer recordId = Integer.valueOf(recordIdStr);
            BorrowRecord record = borrowRecordDAO.findById(recordId);

            // 校验：只有借阅中的书可以续借，且只能续借一次
            if (record != null && record.getStatus() == 0) {
                if (record.getRenewCount() < 1) {
                    // 调用 DAO 的 renew 方法，延长应还日期 30 天
                    boolean success = borrowRecordDAO.renew(recordId, 30);

                    if (success) {
                        request.getSession().setAttribute("msg", "续借成功！");
                    } else {
                        request.getSession().setAttribute("error", "续借失败，请稍后重试");
                    }
                } else {
                    request.getSession().setAttribute("error", "该书已达到最大续借次数");
                }
            } else {
                request.getSession().setAttribute("error", "该记录不存在或不是借阅状态");
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to renew borrow record", e);
            request.getSession().setAttribute("error", "续借失败：" + e.getMessage());
        }
        
        response.sendRedirect(request.getContextPath() + "/borrow?my");
    }
}
