<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>办理归还 - 吕梁学院资料室管理系统</title>
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
                <i class="bi bi-arrow-return-left" style="color: #10b981;"></i> 确认归还信息
            </h2>
        </div>
        <form action="${pageContext.request.contextPath}/borrow/action" method="post">
            <input type="hidden" name="action" value="return">
            <input type="hidden" name="recordId" value="${record.id}">
            <div class="form-group" style="margin-bottom: 20px;">
                <label style="display:block; margin-bottom:6px; color:var(--text-secondary);">图书信息</label>
                <input type="text" value="《${record.bookTitle}》" readonly class="form-control" style="width:100%; padding:10px; border-radius:6px; background:rgba(0,0,0,0.1); border:1px solid rgba(255,255,255,0.1); color:#fff; cursor:not-allowed;">
            </div>
            <div class="form-group" style="margin-bottom: 20px;">
                <label style="display:block; margin-bottom:6px; color:var(--text-secondary);">借阅人</label>
                <input type="text" value="${record.readerName}" readonly class="form-control" style="width:100%; padding:10px; border-radius:6px; background:rgba(0,0,0,0.1); border:1px solid rgba(255,255,255,0.1); color:#fff; cursor:not-allowed;">
            </div>
            <button type="submit" class="btn btn-primary" style="width: 100%; padding: 12px; margin-top: 10px; font-size: 16px; background-color: #10b981; border-color: #10b981;">
                <i class="bi bi-check2-circle"></i> 确认归还图书
            </button>
        </form>
    </div>
</div>
</body>
</html>
