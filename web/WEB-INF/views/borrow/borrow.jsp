<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>确认借阅图书 - 吕梁学院资料室管理系统</title>
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
        .info-section {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 30px;
            margin-bottom: 30px;
        }
        @media (max-width: 768px) {
            .info-section {
                grid-template-columns: 1fr;
                gap: 20px;
            }
        }
        .info-card {
            background: rgba(255, 255, 255, 0.03);
            border: 1px solid var(--glass-border);
            border-radius: 10px;
            padding: 20px;
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
            margin-bottom: 12px;
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
        <c:choose>
            <c:when test="${sessionScope.role == 'admin'}">
                <a href="${pageContext.request.contextPath}/admin/dashboard"><i class="bi bi-speedometer2"></i> 控制台</a>
                <a href="${pageContext.request.contextPath}/book/list"><i class="bi bi-book"></i> 图书管理</a>
                <a href="${pageContext.request.contextPath}/borrow/list" class="active"><i class="bi bi-arrow-left-right"></i> 借还中心</a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/reader/book" class="active"><i class="bi bi-search"></i> 图书查询</a>
                <a href="${pageContext.request.contextPath}/reader/myborrow"><i class="bi bi-journals"></i> 我的借阅</a>
                <a href="${pageContext.request.contextPath}/reader/profile"><i class="bi bi-person-bounding-box"></i> 个人信息</a>
            </c:otherwise>
        </c:choose>
        <a href="${pageContext.request.contextPath}/logout" class="btn-logout"><i class="bi bi-box-arrow-right"></i> 退出</a>
    </div>
</div>

<div class="main-container">
    <div class="glass-card" style="max-width: 800px; margin: 0 auto;">
        <div class="page-header-block">
            <h2><i class="bi bi-bookmark-plus" style="color: #818cf8;"></i> 确认借阅图书</h2>
            <div style="color: var(--text-secondary); font-size: 14px; font-weight: 500;">
                <i class="bi bi-person-circle"></i> 操作人：<c:out value="${sessionScope.realName != null ? sessionScope.realName : sessionScope.username}" />
            </div>
        </div>

        <div class="info-section">
            <!-- 图书详情卡片 -->
            <div class="info-card">
                <h3><i class="bi bi-book-half" style="color: #818cf8;"></i> 图书详细信息</h3>
                <div class="info-item">
                    <span class="info-label">书名</span>
                    <span class="info-value" style="color: #a5b4fc; font-weight: 700;">${book.title}</span>
                </div>
                <div class="info-item">
                    <span class="info-label">作者</span>
                    <span class="info-value">${book.author}</span>
                </div>
                <div class="info-item">
                    <span class="info-label">ISBN</span>
                    <span class="info-value" style="font-family: monospace;">${book.isbn}</span>
                </div>
                <div class="info-item">
                    <span class="info-label">出版社</span>
                    <span class="info-value">${book.publisher}</span>
                </div>
                <div class="info-item">
                    <span class="info-label">馆藏位置</span>
                    <span class="info-value">${book.location}</span>
                </div>
                <div class="info-item">
                    <span class="info-label">当前可借</span>
                    <span class="info-value">
                        <span class="badge badge-success">${book.availableCount} 册</span>
                    </span>
                </div>
            </div>

            <!-- 读者详情卡片 -->
            <div class="info-card">
                <h3><i class="bi bi-person-bounding-box" style="color: #10b981;"></i> 借阅人信息</h3>
                <c:choose>
                    <c:when test="${sessionScope.role == 'reader'}">
                        <div class="info-item">
                            <span class="info-label">姓名</span>
                            <span class="info-value">${reader.realName}</span>
                        </div>
                        <div class="info-item">
                            <span class="info-label">读者类型</span>
                            <span class="info-value"><span class="badge badge-info">${readerType.typeName}</span></span>
                        </div>
                        <div class="info-item">
                            <span class="info-label">证件号 / 学号</span>
                            <span class="info-value">${reader.studentId}</span>
                        </div>
                        <div class="info-item">
                            <span class="info-label">可借天数</span>
                            <span class="info-value">${readerType.borrowLimitDays} 天</span>
                        </div>
                        <div class="info-item">
                            <span class="info-label">最大借阅数</span>
                            <span class="info-value">${readerType.maxBorrowNum} 册</span>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <!-- 管理员角色可以代读者借书 -->
                        <form id="adminBorrowForm">
                            <div class="form-group">
                                <label for="readerSelect">选择读者</label>
                                <select id="readerSelect" name="readerIdSelect" required>
                                    <option value="">-- 请选择借阅读者 --</option>
                                    <c:forEach var="r" items="${readers}">
                                        <option value="${r.id}">${r.realName} (${r.studentId} - ${r.typeName})</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </form>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <form action="${pageContext.request.contextPath}/borrow/borrow" method="post" id="borrowForm">
            <input type="hidden" name="action" value="borrow">
            <input type="hidden" name="bookId" value="${book.id}">
            
            <c:choose>
                <c:when test="${sessionScope.role == 'reader'}">
                    <input type="hidden" name="readerId" value="${reader.id}">
                </c:when>
                <c:otherwise>
                    <input type="hidden" name="readerId" id="realReaderId">
                </c:otherwise>
            </c:choose>

            <div style="display: flex; gap: 16px; justify-content: flex-end; margin-top: 20px;">
                <c:choose>
                    <c:when test="${sessionScope.role == 'admin'}">
                        <a href="${pageContext.request.contextPath}/borrow/list" class="btn btn-secondary">
                            <i class="bi bi-x-circle"></i> 取消
                        </a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/reader/book" class="btn btn-secondary">
                            <i class="bi bi-x-circle"></i> 取消
                        </a>
                    </c:otherwise>
                </c:choose>
                <button type="submit" class="btn btn-primary" id="confirmBtn">
                    <i class="bi bi-check-circle"></i> 确认借阅
                </button>
            </div>
        </form>
    </div>
</div>

<script>
    document.getElementById('borrowForm').addEventListener('submit', function(e) {
        <c:if test="${sessionScope.role == 'admin'}">
        var select = document.getElementById('readerSelect');
        var readerId = select.value;
        if (!readerId) {
            alert('请先选择借阅读者！');
            e.preventDefault();
            return;
        }
        document.getElementById('realReaderId').value = readerId;
        </c:if>
    });
</script>
</body>
</html>
