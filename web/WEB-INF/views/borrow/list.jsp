<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>借阅管理 - 吕梁学院资料室管理系统</title>
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
        .borrow-action-tab {
            display: flex;
            gap: 10px;
            margin-bottom: 20px;
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
            <h2><i class="bi bi-journals" style="color: #6366f1;"></i> 借阅管理</h2>
            <div style="display: flex; gap: 8px;">
                <a href="${pageContext.request.contextPath}/admin/return" class="btn btn-secondary" style="background: rgba(16, 185, 129, 0.15); border-color: rgba(16, 185, 129, 0.3); color: #6ee7b7;">
                    <i class="bi bi-journal-check"></i> 查看归还记录
                </a>
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-secondary">
                    <i class="bi bi-arrow-left"></i> 返回控制台
                </a>
            </div>
        </div>

        <c:choose>
            <c:when test="${not empty records}">
                <table>
                    <thead>
                    <tr>
                        <th style="width: 80px;">记录 ID</th>
                        <th>读者姓名</th>
                        <th>借阅书名</th>
                        <th>借阅日期</th>
                        <th>应还日期</th>
                        <th>借阅状态</th>
                        <th style="width: 140px; text-align: center;">操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="record" items="${records}">
                        <tr>
                            <td><strong># ${record.id}</strong></td>
                            <td>
                                <span style="font-weight: 700; color: white;">
                                    <i class="bi bi-person" style="color: #818cf8; margin-right: 4px;"></i>
                                    ${record.readerName}
                                </span>
                            </td>
                            <td>
                                <span style="font-weight: 500;">
                                    <i class="bi bi-book" style="color: var(--text-muted); margin-right: 4px;"></i>
                                    ${record.bookTitle}
                                </span>
                            </td>
                            <td><i class="bi bi-calendar2-event" style="color: var(--text-muted); margin-right: 4px;"></i> ${record.borrowDate}</td>
                            <td><i class="bi bi-calendar-event" style="color: #fca5a5; margin-right: 4px;"></i> ${record.dueDate}</td>
                            <td>
                                <span class="badge ${record.status == 0 ? 'badge-warning' : 'badge-success'}">
                                    <i class="${record.status == 0 ? 'bi bi-bookmark-dash-fill' : 'bi bi-bookmark-check-fill'}"></i>
                                    ${record.status == 0 ? '借阅中' : '已归还'}
                                </span>
                            </td>
                            <td>
                                <div style="display: flex; justify-content: center;">
                                    <c:choose>
                                        <c:when test="${record.status == 0}">
                                            <a href="${pageContext.request.contextPath}/borrow/return?recordId=${record.id}" class="btn btn-success" style="padding: 6px 12px; font-size: 12px;">
                                                <i class="bi bi-journal-arrow-down"></i> 归还
                                            </a>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="small-muted" style="color: var(--text-muted); font-size: 12px;"><i class="bi bi-check-lg"></i> 无需操作</span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <div style="padding: 60px; text-align: center; color: var(--text-muted);">
                    <i class="bi bi-journals" style="font-size: 48px; display: block; margin-bottom: 16px; color: var(--text-muted);"></i>
                    <h3>当前无任何读者借阅记录</h3>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>
