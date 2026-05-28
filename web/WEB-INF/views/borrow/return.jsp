<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>确认归还图书 - 吕梁学院资料室管理系统</title>
    <!-- 引入 Bootstrap 图标 CDN -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap-icons.min.css">
    <!-- 引入全局高端 UI 样式 -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
    <style>
        .page-header-block {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 24px;
            border-bottom: 1px solid var(--glass-border);
            padding-bottom: 16px;
        }
        .page-header-block h2 {
            display: flex;
            align-items: center;
            gap: 10px;
            font-size: 24px;
        }
        .info-card {
            background: rgba(255, 255, 255, 0.03);
            border: 1px solid var(--glass-border);
            border-radius: 10px;
            padding: 24px;
            margin-bottom: 24px;
        }
        .info-card h3 {
            font-size: 16px;
            border-bottom: 1px solid var(--glass-border);
            padding-bottom: 10px;
            margin-bottom: 16px;
            display: flex;
            align-items: center;
            gap: 8px;
        }
        .info-item {
            display: flex;
            justify-content: space-between;
            margin-bottom: 14px;
            font-size: 14px;
        }
        .info-label {
            color: var(--text-muted);
        }
        .info-value {
            color: white;
            font-weight: 500;
        }
    </style>
</head>
<body>

<!-- 顶部浮动磨砂导航栏 -->
<div class="frosted-navbar">
    <div class="brand">
        <i class="bi bi-journal-bookmark-fill" style="color: #818cf8; font-size: 24px;"></i>
        <span>资料室管理系统</span>
    </div>
    <div class="nav-links">
        <a href="${pageContext.request.contextPath}/admin/dashboard"><i class="bi bi-speedometer2"></i> 控制台</a>
        <a href="${pageContext.request.contextPath}/book/list"><i class="bi bi-book"></i> 图书管理</a>
        <a href="${pageContext.request.contextPath}/borrow/list" class="active"><i class="bi bi-arrow-left-right"></i> 借还中心</a>
        <a href="${pageContext.request.contextPath}/logout" class="btn-logout"><i class="bi bi-box-arrow-right"></i> 退出</a>
    </div>
</div>

<div class="main-container">
    <div class="glass-card" style="max-width: 600px; margin: 0 auto;">
        <div class="page-header-block">
            <h2><i class="bi bi-journal-arrow-down" style="color: #10b981;"></i> 确认归还图书</h2>
            <div style="color: var(--text-secondary); font-size: 14px; font-weight: 500;">
                <i class="bi bi-person-circle"></i> 操作人：<c:out value="${sessionScope.realName != null ? sessionScope.realName : sessionScope.username}" />
            </div>
        </div>

        <div class="info-card">
            <h3><i class="bi bi-clipboard-check" style="color: #10b981;"></i> 借阅详情</h3>
            <div class="info-item">
                <span class="info-label">借阅人（读者）</span>
                <span class="info-value" style="color: #a5b4fc; font-weight: 700;">${record.readerName}</span>
            </div>
            <div class="info-item">
                <span class="info-label">借阅书名</span>
                <span class="info-value" style="font-weight: 600;">${record.bookTitle}</span>
            </div>
            <div class="info-item">
                <span class="info-label">作者</span>
                <span class="info-value">${record.bookAuthor}</span>
            </div>
            <div class="info-item">
                <span class="info-label">借阅日期</span>
                <span class="info-value">${record.borrowDate}</span>
            </div>
            <div class="info-item">
                <span class="info-label">应还日期</span>
                <span class="info-value" style="color: #fca5a5;">${record.dueDate}</span>
            </div>
            <div class="info-item">
                <span class="info-label">续借次数</span>
                <span class="info-value"><span class="badge badge-info">${record.renewCount} 次</span></span>
            </div>
        </div>

        <form action="${pageContext.request.contextPath}/borrow/return" method="post">
            <input type="hidden" name="action" value="return">
            <input type="hidden" name="recordId" value="${record.id}">

            <div style="display: flex; gap: 16px; justify-content: flex-end;">
                <a href="${pageContext.request.contextPath}/borrow/list" class="btn btn-secondary">
                    <i class="bi bi-x-circle"></i> 取消
                </a>
                <button type="submit" class="btn btn-success">
                    <i class="bi bi-check-circle"></i> 确认归还
                </button>
            </div>
        </form>
    </div>
</div>
</body>
</html>
