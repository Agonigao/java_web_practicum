<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>图书查询 - 吕梁学院资料室管理系统</title>
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
        .search-form-row {
            display: flex;
            gap: 12px;
            align-items: center;
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
        <a href="${pageContext.request.contextPath}/reader/book" class="active"><i class="bi bi-search"></i> 图书查询</a>
        <a href="${pageContext.request.contextPath}/reader/myborrow"><i class="bi bi-journals"></i> 我的借阅</a>
        <a href="${pageContext.request.contextPath}/reader/profile"><i class="bi bi-person-bounding-box"></i> 个人信息</a>
        <a href="${pageContext.request.contextPath}/logout" class="btn-logout"><i class="bi bi-box-arrow-right"></i> 退出</a>
    </div>
</div>

<div class="main-container">
    <div class="glass-card">
        <div class="page-header-block">
            <h2><i class="bi bi-search" style="color: #6366f1;"></i> 在馆图书查询</h2>
            <div style="color: var(--text-secondary); font-size: 14px; font-weight: 500;">
                <i class="bi bi-person-circle"></i> 欢迎您，<c:out value="${sessionScope.realName != null ? sessionScope.realName : sessionScope.username}" />
            </div>
        </div>

        <!-- 极简高端搜索框 -->
        <form action="${pageContext.request.contextPath}/reader/book" method="get" class="search-form-row">
            <div class="form-group" style="margin: 0; width: 320px; position: relative;">
                <input type="text" name="keyword" value="${param.keyword}" placeholder="搜索书名、作者或 ISBN..." style="padding-left: 40px; height: 42px;">
                <i class="bi bi-search" style="position: absolute; left: 14px; top: 11px; color: var(--text-muted); font-size: 15px;"></i>
            </div>
            <button type="submit" class="btn btn-primary" style="height: 42px;">
                立即搜索
            </button>
            <c:if test="${not empty param.keyword}">
                <a href="${pageContext.request.contextPath}/reader/book" class="btn btn-secondary" style="height: 42px; display: inline-flex; align-items: center;">
                    清除筛选
                </a>
            </c:if>
        </form>

        <c:choose>
            <c:when test="${not empty books}">
                <table>
                    <thead>
                    <tr>
                        <th>书名</th>
                        <th>作者</th>
                        <th>ISBN</th>
                        <th>出版社</th>
                        <th>分类</th>
                        <th>馆藏可借状态</th>
                        <th style="width: 140px; text-align: center;">操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="book" items="${books}">
                        <%-- 搜索过滤辅助（如果没有后端搜索支持，此前端过滤做双重保障） --%>
                        <c:if test="${empty param.keyword || fn:contains(fn:toLowerCase(book.title), fn:toLowerCase(param.keyword)) || fn:contains(fn:toLowerCase(book.author), fn:toLowerCase(param.keyword)) || fn:contains(fn:toLowerCase(book.isbn), fn:toLowerCase(param.keyword))}">
                            <tr>
                                <td>
                                    <span style="font-weight: 700; color: white;">
                                        <i class="bi bi-book-half" style="color: #818cf8; margin-right: 6px;"></i>
                                        ${book.title}
                                    </span>
                                </td>
                                <td>${book.author}</td>
                                <td style="font-family: monospace; color: #cbd5e1;">${book.isbn}</td>
                                <td>${book.publisher}</td>
                                <td>
                                    <span class="badge badge-info">
                                        ${book.categoryName != null ? book.categoryName : '通用'}
                                    </span>
                                </td>
                                <td>
                                    <span class="badge ${book.availableCount > 0 ? 'badge-success' : 'badge-danger'}">
                                        ${book.availableCount > 0 ? '可借阅' : '已借完'} (${book.availableCount}/${book.totalCount})
                                    </span>
                                </td>
                                <td>
                                    <div style="display: flex; justify-content: center;">
                                        <c:choose>
                                            <c:when test="${book.availableCount > 0}">
                                                <a href="${pageContext.request.contextPath}/borrow/borrow?bookId=${book.id}" class="btn btn-primary" style="padding: 6px 16px; font-size: 13px;">
                                                    <i class="bi bi-bookmark-plus"></i> 借阅
                                                </a>
                                            </c:when>
                                            <c:otherwise>
                                                <button class="btn btn-secondary" disabled style="padding: 6px 16px; font-size: 13px; opacity: 0.5; cursor: not-allowed;">
                                                    不可借
                                                </button>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </td>
                            </tr>
                        </c:if>
                    </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <div style="padding: 60px; text-align: center; color: var(--text-muted);">
                    <i class="bi bi-book" style="font-size: 48px; display: block; margin-bottom: 16px; color: var(--text-muted);"></i>
                    <h3>资料室内暂无可借图书</h3>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>
