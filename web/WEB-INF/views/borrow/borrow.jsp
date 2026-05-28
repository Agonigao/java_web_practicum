<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.library.dao.ReaderInfoDAO" %>
<%@ page import="com.library.entity.ReaderInfo" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    Integer userId = (Integer) session.getAttribute("userId");
    ReaderInfo reader = null;
    if (userId != null) {
        reader = new ReaderInfoDAO().findByUserId(userId);
    }
    request.setAttribute("reader", reader);
    LocalDate today = LocalDate.now();
    LocalDate dueDate = today.plusDays(30);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    request.setAttribute("todayStr", today.format(formatter));
    request.setAttribute("dueDateStr", dueDate.format(formatter));
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>办理借阅 - 吕梁学院资料室管理系统</title>
    <!-- 引入 Bootstrap 图标 CDN -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap-icons.min.css">
    <!-- 引入全局高端 UI 样式 -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
</head>
<body>
<!-- 顶部浮动磨砂导航栏 -->
<div class="frosted-navbar">
    <div class="brand">
        <i class="bi bi-journal-bookmark-fill" style="color: #818cf8; font-size: 24px;"></i>
        <span>资料室管理系统</span>
    </div>
    <div class="nav-links">
        <a href="javascript:history.back()"><i class="bi bi-arrow-left"></i> 返回</a>
    </div>
</div>
<div class="main-container" style="max-width: 600px;">
    <div class="glass-card">
        <div style="border-bottom: 1px solid var(--glass-border); padding-bottom: 16px; margin-bottom: 24px;">
            <h2 style="font-size: 20px; display: flex; align-items: center; gap: 10px;">
                <i class="bi bi-bookmark-plus" style="color: #6366f1;"></i> 确认借阅信息
            </h2>
        </div>
        <form action="${pageContext.request.contextPath}/borrow/action" method="post">
            <input type="hidden" name="action" value="borrow">
            <input type="hidden" name="bookId" value="${book.id}">
            <c:if test="${reader != null}">
                <input type="hidden" name="readerId" value="${reader.id}">
            </c:if>
            <div class="form-group" style="margin-bottom: 20px;">
                <label style="display:block; margin-bottom:6px; color:var(--text-secondary);">借阅的书籍</label>
                <input type="text" value="《${book.title}》 - ${book.author}" readonly class="form-control" style="width:100%; padding:10px; border-radius:6px; background:rgba(0,0,0,0.1); border:1px solid rgba(255,255,255,0.1); color:#fff; cursor:not-allowed;">
            </div>
            <div class="form-group" style="margin-bottom: 20px;">
                <label style="display:block; margin-bottom:6px; color:var(--text-secondary);">借阅数量</label>
                <input type="text" value="1 本" readonly class="form-control" style="width:100%; padding:10px; border-radius:6px; background:rgba(0,0,0,0.1); border:1px solid rgba(255,255,255,0.1); color:#fff; cursor:not-allowed;">
            </div>
            <div class="form-group" style="margin-bottom: 20px;">
                <label style="display:block; margin-bottom:6px; color:var(--text-secondary);">借阅日期</label>
                <input type="text" value="${todayStr}" readonly class="form-control" style="width:100%; padding:10px; border-radius:6px; background:rgba(0,0,0,0.1); border:1px solid rgba(255,255,255,0.1); color:#fff; cursor:not-allowed;">
            </div>
            <div class="form-group" style="margin-bottom: 20px;">
                <label style="display:block; margin-bottom:6px; color:var(--text-secondary);">应还日期 (30天后)</label>
                <input type="text" value="${dueDateStr}" readonly class="form-control" style="width:100%; padding:10px; border-radius:6px; background:rgba(0,0,0,0.1); border:1px solid rgba(255,255,255,0.1); color:#fca5a5; cursor:not-allowed;">
            </div>
            <c:if test="${reader == null}">
                <div class="form-group" style="margin-bottom: 20px;">
                    <label style="display:block; margin-bottom:6px; color:var(--text-secondary);">读者 ID</label>
                    <input type="number" name="readerId" required class="form-control" placeholder="请输入你的借阅证ID" style="width:100%; padding:10px; border-radius:6px; background:rgba(255,255,255,0.05); border:1px solid rgba(255,255,255,0.2); color:#fff;">
                </div>
            </c:if>
            <button type="submit" class="btn btn-primary" style="width: 100%; padding: 12px; margin-top: 10px; font-size: 16px;">
                <i class="bi bi-check2-circle"></i> 确认借阅图书
            </button>
        </form>
    </div>
</div>
</body>
</html>
    private void doBorrow(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int readerId = Integer.parseInt(request.getParameter("readerId"));
        int bookId = Integer.parseInt(request.getParameter("bookId"));

        BorrowRecord record = new BorrowRecord();
        record.setReaderId(readerId);
        record.setBookId(bookId);

        boolean success = borrowRecordDAO.borrow(record);

        String role = (String) request.getSession().getAttribute("role");

        // 【修改这里】：如果是普通读者去原本的"我的借阅"，如果是管理员也让其回到"图书查询"页面，不跳转到管理者页面
        String redirectPath = "reader".equals(role) ? "/reader/myborrow" : "/reader/book";

        if (success) {
            response.sendRedirect(request.getContextPath() + redirectPath + "?msg=borrow_success");
        } else {
            response.sendRedirect(request.getContextPath() + redirectPath + "?msg=borrow_failed");
        }
    }    private void doBorrow(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int readerId = Integer.parseInt(request.getParameter("readerId"));
        int bookId = Integer.parseInt(request.getParameter("bookId"));

        BorrowRecord record = new BorrowRecord();
        record.setReaderId(readerId);
        record.setBookId(bookId);

        boolean success = borrowRecordDAO.borrow(record);

        String role = (String) request.getSession().getAttribute("role");

        // 【修改这里】：如果是普通读者去原本的"我的借阅"，如果是管理员也让其回到"图书查询"页面，不跳转到管理者页面
        String redirectPath = "reader".equals(role) ? "/reader/myborrow" : "/reader/book";

        if (success) {
            response.sendRedirect(request.getContextPath() + redirectPath + "?msg=borrow_success");
        } else {
            response.sendRedirect(request.getContextPath() + redirectPath + "?msg=borrow_failed");
        }
    }