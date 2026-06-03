<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>我的借阅记录 - 吕梁学院资料室管理系统</title>
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
        <a href="${pageContext.request.contextPath}/reader/book"><i class="bi bi-search"></i> 图书查询</a>
        <a href="${pageContext.request.contextPath}/reader/myborrow" class="active"><i class="bi bi-journals"></i> 我的借阅</a>
        <a href="${pageContext.request.contextPath}/reader/profile"><i class="bi bi-person-bounding-box"></i> 个人信息</a>
        <a href="${pageContext.request.contextPath}/logout" class="btn-logout"><i class="bi bi-box-arrow-right"></i> 退出</a>
    </div>
</div>

<div class="main-container">
    <div class="glass-card">
        <div class="page-header-block">
            <h2><i class="bi bi-calendar2-week" style="color: #6366f1;"></i> 我的借阅历史与当前记录</h2>
            <div style="color: var(--text-secondary); font-size: 14px; font-weight: 500;">
                <i class="bi bi-person-circle"></i> 读者：<c:out value="${sessionScope.realName != null ? sessionScope.realName : sessionScope.username}" />
            </div>
        </div>

        <%-- 提示消息展示 --%>
        <c:if test="${not empty sessionScope.msg}">
            <div class="alert-box alert-success" style="margin-bottom: 20px;">
                <i class="bi bi-check-circle-fill" style="font-size: 18px;"></i>
                <div>${sessionScope.msg}</div>
            </div>
            <% session.removeAttribute("msg"); %>
        </c:if>
        <c:if test="${not empty sessionScope.error}">
            <div class="alert-box alert-danger" style="margin-bottom: 20px;">
                <i class="bi bi-exclamation-triangle-fill" style="font-size: 18px;"></i>
                <div>${sessionScope.error}</div>
            </div>
            <% session.removeAttribute("error"); %>
        </c:if>
        <c:if test="${param.msg == 'renew_success'}">
            <div class="alert-box alert-success" style="margin-bottom: 20px;">
                <i class="bi bi-check-circle-fill" style="font-size: 18px;"></i>
                <div>图书续借成功！</div>
            </div>
        </c:if>
        <c:if test="${param.msg == 'renew_failed'}">
            <div class="alert-box alert-danger" style="margin-bottom: 20px;">
                <i class="bi bi-exclamation-triangle-fill" style="font-size: 18px;"></i>
                <div>图书续借失败！</div>
            </div>
        </c:if>
        <c:if test="${param.msg == 'borrow_success'}">
            <div class="alert-box alert-success" style="margin-bottom: 20px;">
                <i class="bi bi-check-circle-fill" style="font-size: 18px;"></i>
                <div>图书借阅成功！</div>
            </div>
        </c:if>
        <c:if test="${param.msg == 'borrow_failed'}">
            <div class="alert-box alert-danger" style="margin-bottom: 20px;">
                <i class="bi bi-exclamation-triangle-fill" style="font-size: 18px;"></i>
                <div>图书借阅失败，请稍后重试。</div>
            </div>
        </c:if>
        <c:if test="${param.msg == 'return_success'}">
            <div class="alert-box alert-success" style="margin-bottom: 20px;">
                <i class="bi bi-check-circle-fill" style="font-size: 18px;"></i>
                <div>图书归还成功！</div>
            </div>
        </c:if>
        <c:if test="${param.msg == 'return_failed'}">
            <div class="alert-box alert-danger" style="margin-bottom: 20px;">
                <i class="bi bi-exclamation-triangle-fill" style="font-size: 18px;"></i>
                <div>图书归还失败，请稍后重试。</div>
            </div>
        </c:if>

        <c:choose>
            <c:when test="${not empty records}">
                <table>
                    <thead>
                    <tr>
                        <th>借阅书名</th>
                        <th>借阅日期</th>
                        <th>应还日期</th>
                        <th>当前状态</th>
                        <th>已续借次数</th>
                        <th style="width: 140px; text-align: center;">操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="record" items="${records}">
                        <tr>
                            <td>
                                <span style="font-weight: 700; color: white;">
                                    <i class="bi bi-book" style="color: #818cf8; margin-right: 6px;"></i>
                                    ${record.bookTitle}
                                </span>
                            </td>
                            <td><i class="bi bi-calendar-plus" style="color: var(--text-muted); margin-right: 4px;"></i> ${record.borrowDate}</td>
                            <td><i class="bi bi-calendar-minus" style="color: #fca5a5; margin-right: 4px;"></i> ${record.dueDate}</td>
                            <td>
                                <span class="badge ${record.status == 0 ? 'badge-warning' : 'badge-success'}">
                                    <i class="${record.status == 0 ? 'bi bi-bookmark-dash-fill' : 'bi bi-bookmark-check-fill'}"></i>
                                    ${record.status == 0 ? '借阅中' : '已归还'}
                                </span>
                            </td>
                            <td>
                                <span class="badge ${record.renewCount > 0 ? 'badge-info' : 'badge-secondary'}">
                                    ${record.renewCount} / 1 次
                                </span>
                            </td>
                            <td>
                                <div style="display: flex; justify-content: center;">
                                    <c:choose>
                                        <c:when test="${record.status == 0 && record.renewCount < 1}">
                                            <form method="post" action="${pageContext.request.contextPath}/borrow/renew" style="display:inline; margin:0;">
                                                <input type="hidden" name="recordId" value="${record.id}" />
                                                <button type="submit" class="btn btn-secondary" style="padding: 6px 12px; font-size: 12px;">
                                                    <i class="bi bi-arrow-repeat"></i> 续借
                                                </button>
                                            </form>
                                        </c:when>
                                        <c:when test="${record.status == 0}">
                                            <span class="small-muted" style="color: var(--text-muted); font-size: 12px;">已达续借上限</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="small-muted" style="color: var(--success); font-size: 12px;"><i class="bi bi-check-all"></i> 借阅已完结</span>
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
                    <i class="bi bi-clipboard-x" style="font-size: 48px; display: block; margin-bottom: 16px; color: var(--text-muted);"></i>
                    <h3>您当前没有任何图书借阅记录</h3>
                    <p style="margin-top: 8px; font-size: 14px;">需要借阅图书？请先到“图书查询”页面挑选您的心仪书籍。</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>
