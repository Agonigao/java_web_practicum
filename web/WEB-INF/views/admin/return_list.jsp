<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>归还记录 - 吕梁学院资料室管理系统</title>
    <!-- 引入 Bootstrap 图标 CDN -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
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
        <a href="${pageContext.request.contextPath}/admin/dashboard"><i class="bi bi-speedometer2"></i> 仪表盘</a>
        <a href="${pageContext.request.contextPath}/admin/user"><i class="bi bi-people"></i> 用户管理</a>
        <a href="${pageContext.request.contextPath}/admin/reader"><i class="bi bi-person-badge"></i> 读者管理</a>
        <a href="${pageContext.request.contextPath}/admin/book"><i class="bi bi-book"></i> 图书管理</a>
        <a href="${pageContext.request.contextPath}/borrow/list" class="active"><i class="bi bi-arrow-left-right"></i> 借阅管理</a>
        <a href="${pageContext.request.contextPath}/logout" class="btn-logout"><i class="bi bi-box-arrow-right"></i> 退出</a>
    </div>
</div>

<div class="main-container">
    <div class="glass-card">
        <div class="page-header-block">
            <h2><i class="bi bi-journal-check" style="color: #10b981;"></i> 图书归还历史记录</h2>
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-secondary">
                <i class="bi bi-arrow-left"></i> 返回控制台
            </a>
        </div>

        <c:choose>
            <c:when test="${not empty returns}">
                <table>
                    <thead>
                    <tr>
                        <th style="width: 80px;">记录 ID</th>
                        <th>读者姓名</th>
                        <th>借阅图书</th>
                        <th>借出日期</th>
                        <th>实际归还日期</th>
                        <th>是否逾期</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="record" items="${returns}">
                        <tr>
                            <td><strong># ${record.id}</strong></td>
                            <td>
                                <span style="font-weight: 700; color: white;">
                                    <i class="bi bi-person-circle" style="color: #818cf8; margin-right: 4px;"></i>
                                    ${record.readerName}
                                </span>
                            </td>
                            <td>
                                <span style="font-weight: 500;">
                                    <i class="bi bi-book" style="color: var(--text-muted); margin-right: 4px;"></i>
                                    ${record.bookTitle}
                                </span>
                            </td>
                            <td><i class="bi bi-clock-history" style="color: var(--text-muted); margin-right: 4px;"></i> ${record.borrowDate}</td>
                            <td><i class="bi bi-calendar-check" style="color: #34d399; margin-right: 4px;"></i> ${record.returnDate}</td>
                            <td>
                                <span class="badge ${record.isOverdue == 1 ? 'badge-danger' : 'badge-success'}">
                                    <i class="${record.isOverdue == 1 ? 'bi bi-shield-fill-x' : 'bi bi-shield-fill-check'}"></i>
                                    ${record.isOverdue == 1 ? '已逾期' : '按时归还'}
                                </span>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <div style="padding: 60px; text-align: center; color: var(--text-muted);">
                    <i class="bi bi-inbox-fill" style="font-size: 48px; display: block; margin-bottom: 16px; color: var(--text-muted);"></i>
                    <h3>暂无任何归还记录</h3>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>
