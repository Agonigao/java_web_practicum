<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>管理员控制台 - 吕梁学院资料室管理系统</title>
    <!-- 引入 Bootstrap 图标 CDN -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap-icons.min.css">
    <!-- 引入全局高端 UI 样式 -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
    <style>
        .dashboard-header-icon {
            font-size: 24px;
            color: #818cf8;
        }
        .stat-icon {
            position: absolute;
            right: 20px;
            bottom: 16px;
            font-size: 36px;
            color: rgba(255, 255, 255, 0.05);
            transition: all 0.3s ease;
        }
        .stat-card:hover .stat-icon {
            color: rgba(99, 102, 241, 0.2);
            transform: scale(1.1);
        }
    </style>
</head>
<body>

<!-- 顶部浮动磨砂导航栏 -->
<div class="frosted-navbar">
    <div class="brand">
        <i class="bi bi-journal-bookmark-fill dashboard-header-icon"></i>
        <span>资料室管理系统</span>
    </div>
    <div class="nav-links">
        <a href="${pageContext.request.contextPath}/admin/dashboard" class="active"><i class="bi bi-speedometer2"></i> 仪表盘</a>
        <a href="${pageContext.request.contextPath}/admin/user"><i class="bi bi-people"></i> 用户管理</a>
        <a href="${pageContext.request.contextPath}/admin/reader"><i class="bi bi-person-badge"></i> 读者管理</a>
        <a href="${pageContext.request.contextPath}/admin/book"><i class="bi bi-book"></i> 图书管理</a>
        <a href="${pageContext.request.contextPath}/borrow/list"><i class="bi bi-arrow-left-right"></i> 借阅管理</a>
        <a href="${pageContext.request.contextPath}/logout" class="btn-logout"><i class="bi bi-box-arrow-right"></i> 退出</a>
    </div>
</div>

<div class="main-container">
    <!-- 欢迎横幅卡片 -->
    <div class="welcome-banner">
        <div class="greeting">
            <h3>欢迎回来，<c:out value="${sessionScope.realName != null ? sessionScope.realName : sessionScope.username}" /> <i class="bi bi-hand-thumbs-up" style="color: #fcd34d;"></i></h3>
            <p class="small-muted" style="color: var(--text-secondary); font-size: 14px;">吕梁学院资料室管理系统已就绪，当前为管理员最高权限控制台。</p>
        </div>
        <div style="display: flex; gap: 10px;">
            <a href="${pageContext.request.contextPath}/admin/user" class="btn btn-primary">
                <i class="bi bi-person-gear"></i> 管理用户
            </a>
            <a href="${pageContext.request.contextPath}/admin/book" class="btn btn-secondary">
                <i class="bi bi-plus-circle"></i> 管理图书
            </a>
        </div>
    </div>

    <!-- 核心指标卡片 -->
    <div class="stats-grid">
        <div class="glass-card stat-card">
            <div class="num"><c:out value="${totalBooks != null ? totalBooks : 0}" /></div>
            <div class="label">图书总数</div>
            <i class="bi bi-bookshelf stat-icon"></i>
        </div>
        <div class="glass-card stat-card">
            <div class="num"><c:out value="${borrowingCount != null ? borrowingCount : 0}" /></div>
            <div class="label">当前借阅</div>
            <i class="bi bi-arrow-down-up stat-icon"></i>
        </div>
        <div class="glass-card stat-card">
            <div class="num"><c:out value="${totalReaders != null ? totalReaders : 0}" /></div>
            <div class="label">读者总数</div>
            <i class="bi bi-people stat-icon"></i>
        </div>
        <div class="glass-card stat-card">
            <div class="num"><c:out value="${overdueCount != null ? overdueCount : 0}" /></div>
            <div class="label">逾期次数</div>
            <i class="bi bi-exclamation-triangle stat-icon" style="color: rgba(239, 68, 68, 0.15);"></i>
        </div>
    </div>

    <!-- 异常与提醒表格卡片 -->
    <div class="glass-card" style="margin-bottom: 28px;">
        <div class="section-title">
            <span><i class="bi bi-bell-fill" style="color: var(--warning);"></i> 即将到期提醒（未来 7 天内）</span>
        </div>

        <c:choose>
            <c:when test="${not empty dueSoonRecords}">
                <table>
                    <thead>
                    <tr>
                        <th>读者姓名</th>
                        <th>书名</th>
                        <th>应还日期</th>
                        <th>状态</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="record" items="${dueSoonRecords}">
                        <tr>
                            <td><strong><c:out value="${record.readerName}" /></strong></td>
                            <td><c:out value="${record.bookTitle}" /></td>
                            <td style="color:#f87171; font-weight:700;"><i class="bi bi-clock"></i> <c:out value="${record.dueDate}" /></td>
                            <td>
                                <span class="badge badge-warning">即将到期</span>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <div style="padding:40px; text-align:center; color:var(--text-muted); font-size: 14px;">
                    <i class="bi bi-clipboard-check" style="font-size: 32px; display: block; margin-bottom: 12px; color: var(--success);"></i>
                    暂无即将到期的借阅记录
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- 快速快捷菜单 -->
    <div class="glass-card">
        <div class="section-title">
            <span><i class="bi bi-grid-fill" style="color: #818cf8;"></i> 快速操作</span>
        </div>
        <div class="action-grid">
            <div class="glass-card action-card" onclick="location.href='${pageContext.request.contextPath}/admin/user'">
                <div class="icon-wrapper">
                    <i class="bi bi-people-fill"></i>
                </div>
                <div class="title">用户管理</div>
                <div class="small-muted">系统登录账号及状态管理</div>
            </div>
            <div class="glass-card action-card" onclick="location.href='${pageContext.request.contextPath}/admin/reader'">
                <div class="icon-wrapper">
                    <i class="bi bi-person-badge-fill"></i>
                </div>
                <div class="title">读者管理</div>
                <div class="small-muted">读者类型、可借数与详细资料</div>
            </div>
            <div class="glass-card action-card" onclick="location.href='${pageContext.request.contextPath}/admin/book'">
                <div class="icon-wrapper">
                    <i class="bi bi-book-half"></i>
                </div>
                <div class="title">图书管理</div>
                <div class="small-muted">图书的添加、修改和列表控制</div>
            </div>
            <div class="glass-card action-card" onclick="location.href='${pageContext.request.contextPath}/admin/category'">
                <div class="icon-wrapper">
                    <i class="bi bi-tags-fill"></i>
                </div>
                <div class="title">图书类型管理</div>
                <div class="small-muted">定义与配置图书分类层级</div>
            </div>
            <div class="glass-card action-card" onclick="location.href='${pageContext.request.contextPath}/borrow/list'">
                <div class="icon-wrapper">
                    <i class="bi bi-journals"></i>
                </div>
                <div class="title">借阅管理</div>
                <div class="small-muted">追踪并审核所有读者借阅流向</div>
            </div>
            <div class="glass-card action-card" onclick="location.href='${pageContext.request.contextPath}/admin/return'">
                <div class="icon-wrapper">
                    <i class="bi bi-journal-check"></i>
                </div>
                <div class="title">归还管理</div>
                <div class="small-muted">处理借阅到期与归还惩罚确认</div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
