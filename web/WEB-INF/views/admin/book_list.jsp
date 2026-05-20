<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>图书管理 - 吕梁学院资料室管理系统</title>
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
        <a href="${pageContext.request.contextPath}/admin/book" class="active"><i class="bi bi-book"></i> 图书管理</a>
        <a href="${pageContext.request.contextPath}/borrow/list"><i class="bi bi-arrow-left-right"></i> 借阅管理</a>
        <a href="${pageContext.request.contextPath}/logout" class="btn-logout"><i class="bi bi-box-arrow-right"></i> 退出</a>
    </div>
</div>

<div class="main-container">
    <div class="glass-card">
        <div class="page-header-block">
            <h2><i class="bi bi-book-half" style="color: #6366f1;"></i> 图书列表</h2>
            <a href="${pageContext.request.contextPath}/book/add" class="btn btn-primary">
                <i class="bi bi-plus-circle-fill"></i> 添加图书
            </a>
        </div>

        <c:choose>
            <c:when test="${not empty books}">
                <table>
                    <thead>
                    <tr>
                        <th style="width: 80px;">ID</th>
                        <th>书名</th>
                        <th>作者</th>
                        <th>ISBN</th>
                        <th>出版社</th>
                        <th>可借 / 总量</th>
                        <th>存放位置</th>
                        <th style="width: 180px; text-align: center;">操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="book" items="${books}">
                        <tr>
                            <td><strong>${book.id}</strong></td>
                            <td><span style="font-weight: 600; color: white;">${book.title}</span></td>
                            <td>${book.author}</td>
                            <td style="font-family: monospace; color: #a5b4fc;">${book.isbn}</td>
                            <td>${book.publisher}</td>
                            <td>
                                <span class="badge ${book.availableCount > 0 ? 'badge-success' : 'badge-danger'}">
                                    ${book.availableCount} / ${book.totalCount}
                                </span>
                            </td>
                            <td><i class="bi bi-geo-alt" style="color: #a5b4fc; font-size: 13px;"></i> ${book.location}</td>
                            <td>
                                <div style="display: flex; gap: 8px; justify-content: center;">
                                    <a href="${pageContext.request.contextPath}/book/edit?id=${book.id}" class="btn btn-success" style="padding: 6px 12px; font-size: 12px;">
                                        <i class="bi bi-pencil-square"></i> 编辑
                                    </a>
                                    <a href="${pageContext.request.contextPath}/book/delete?id=${book.id}" class="btn btn-danger" style="padding: 6px 12px; font-size: 12px;" onclick="return confirm('确认删除该图书吗？')">
                                        <i class="bi bi-trash"></i> 删除
                                    </a>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <div style="padding: 60px; text-align: center; color: var(--text-muted);">
                    <i class="bi bi-journal-x" style="font-size: 48px; display: block; margin-bottom: 16px; color: var(--text-muted);"></i>
                    <h3>暂无任何图书记录</h3>
                    <p style="margin-top: 8px; font-size: 14px;">请点击右上角的“添加图书”按钮开始录入图书。</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>
