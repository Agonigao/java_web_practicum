<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>用户管理 - 吕梁学院资料室管理系统</title>
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
        <a href="${pageContext.request.contextPath}/admin/user" class="active"><i class="bi bi-people"></i> 用户管理</a>
        <a href="${pageContext.request.contextPath}/admin/reader"><i class="bi bi-person-badge"></i> 读者管理</a>
        <a href="${pageContext.request.contextPath}/admin/book"><i class="bi bi-book"></i> 图书管理</a>
        <a href="${pageContext.request.contextPath}/borrow/list"><i class="bi bi-arrow-left-right"></i> 借阅管理</a>
        <a href="${pageContext.request.contextPath}/logout" class="btn-logout"><i class="bi bi-box-arrow-right"></i> 退出</a>
    </div>
</div>

<div class="main-container">
    <div class="glass-card">
        <div class="page-header-block">
            <h2><i class="bi bi-people-fill" style="color: #6366f1;"></i> 系统账号管理</h2>
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-secondary">
                <i class="bi bi-arrow-left"></i> 返回控制台
            </a>
        </div>

        <c:choose>
            <c:when test="${not empty users}">
                <table>
                    <thead>
                    <tr>
                        <th style="width: 100px;">账号 ID</th>
                        <th>登录用户名</th>
                        <th>系统角色</th>
                        <th>账号状态</th>
                        <th>注册创建时间</th>
                        <th style="width: 140px; text-align: center;">操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="user" items="${users}">
                        <tr>
                            <td><strong># ${user.id}</strong></td>
                            <td>
                                <span style="font-weight: 700; color: white;">
                                    <i class="bi bi-person" style="color: #818cf8; margin-right: 6px;"></i>
                                    ${user.username}
                                </span>
                            </td>
                            <td>
                                <span class="badge ${user.role == 'admin' ? 'badge-danger' : 'badge-info'}">
                                    <i class="${user.role == 'admin' ? 'bi bi-shield-lock' : 'bi bi-person'}"></i>
                                    ${user.role == 'admin' ? '管理员' : '读者'}
                                </span>
                            </td>
                            <td>
                                <span class="badge ${user.status == 1 ? 'badge-success' : 'badge-danger'}">
                                    ${user.status == 1 ? '正常使用' : '已被禁用'}
                                </span>
                            </td>
                            <td><i class="bi bi-clock" style="color: var(--text-muted); margin-right: 4px;"></i> ${user.createTime}</td>
                            <td>
                                <div style="display: flex; justify-content: center;">
                                    <c:choose>
                                        <c:when test="${user.id != sessionScope.userId}">
                                            <a href="${pageContext.request.contextPath}/admin/user?action=delete&id=${user.id}" class="btn btn-danger" style="padding: 6px 12px; font-size: 12px;" onclick="return confirm('确认删除该系统账号吗？(对应读者档案可能将连带受损!)')">
                                                <i class="bi bi-trash"></i> 删除账号
                                            </a>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="small-muted" style="color: var(--text-muted); font-size: 12px;"><i class="bi bi-shield-check"></i> 当前登录中</span>
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
                    <i class="bi bi-shield-slash" style="font-size: 48px; display: block; margin-bottom: 16px; color: var(--text-muted);"></i>
                    <h3>暂无任何登录账号记录</h3>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>
