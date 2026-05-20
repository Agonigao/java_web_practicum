<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>添加图书 - 吕梁学院资料室管理系统</title>
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

<div class="main-container" style="max-width: 800px;">
    <div class="glass-card">
        <div class="page-header-block">
            <h2><i class="bi bi-plus-circle" style="color: #6366f1;"></i> 录入新图书</h2>
            <a href="${pageContext.request.contextPath}/admin/book" class="btn btn-secondary">
                <i class="bi bi-arrow-left"></i> 返回列表
            </a>
        </div>

        <form action="${pageContext.request.contextPath}/book?action=add" method="post">
            <div class="form-row">
                <div class="form-group">
                    <label>ISBN 国际标准书号 *</label>
                    <div style="position: relative;">
                        <input type="text" name="isbn" required placeholder="例如：978-7-111-11111-1" style="padding-left: 40px;">
                        <i class="bi bi-barcode" style="position: absolute; left: 14px; top: 12px; color: var(--text-muted); font-size: 16px;"></i>
                    </div>
                </div>
                <div class="form-group">
                    <label>书名 *</label>
                    <div style="position: relative;">
                        <input type="text" name="title" required placeholder="请输入书名" style="padding-left: 40px;">
                        <i class="bi bi-book" style="position: absolute; left: 14px; top: 12px; color: var(--text-muted); font-size: 16px;"></i>
                    </div>
                </div>
            </div>
            
            <div class="form-row">
                <div class="form-group">
                    <label>作者 *</label>
                    <div style="position: relative;">
                        <input type="text" name="author" required placeholder="请输入作者" style="padding-left: 40px;">
                        <i class="bi bi-person-fill" style="position: absolute; left: 14px; top: 12px; color: var(--text-muted); font-size: 16px;"></i>
                    </div>
                </div>
                <div class="form-group">
                    <label>出版社 *</label>
                    <div style="position: relative;">
                        <input type="text" name="publisher" required placeholder="请输入出版社" style="padding-left: 40px;">
                        <i class="bi bi-building" style="position: absolute; left: 14px; top: 12px; color: var(--text-muted); font-size: 16px;"></i>
                    </div>
                </div>
            </div>
            
            <div class="form-row">
                <div class="form-group">
                    <label>出版日期</label>
                    <div style="position: relative;">
                        <input type="date" name="publishDate" style="padding-left: 40px;">
                        <i class="bi bi-calendar-event" style="position: absolute; left: 14px; top: 12px; color: var(--text-muted); font-size: 16px;"></i>
                    </div>
                </div>
                <div class="form-group">
                    <label>图书分类 *</label>
                    <div style="position: relative;">
                        <select name="categoryId" required style="padding-left: 40px; appearance: none; -webkit-appearance: none;">
                            <option value="">请选择图书分类</option>
                            <c:forEach var="cat" items="${categories}">
                                <option value="${cat.id}">${cat.categoryName}</option>
                            </c:forEach>
                        </select>
                        <i class="bi bi-tags" style="position: absolute; left: 14px; top: 12px; color: var(--text-muted); font-size: 16px;"></i>
                        <i class="bi bi-chevron-down" style="position: absolute; right: 14px; top: 14px; color: var(--text-muted); font-size: 12px; pointer-events: none;"></i>
                    </div>
                </div>
            </div>
            
            <div class="form-row">
                <div class="form-group">
                    <label>总数量 *</label>
                    <div style="position: relative;">
                        <input type="number" name="totalCount" required min="1" value="1" style="padding-left: 40px;">
                        <i class="bi bi-layers" style="position: absolute; left: 14px; top: 12px; color: var(--text-muted); font-size: 16px;"></i>
                    </div>
                </div>
                <div class="form-group">
                    <label>可借数量 *</label>
                    <div style="position: relative;">
                        <input type="number" name="availableCount" required min="0" value="1" style="padding-left: 40px;">
                        <i class="bi bi-check2-square" style="position: absolute; left: 14px; top: 12px; color: var(--text-muted); font-size: 16px;"></i>
                    </div>
                </div>
            </div>
            
            <div class="form-group">
                <label>存放位置</label>
                <div style="position: relative;">
                    <input type="text" name="location" placeholder="例如：A区书架-102" style="padding-left: 40px;">
                    <i class="bi bi-geo-alt" style="position: absolute; left: 14px; top: 12px; color: var(--text-muted); font-size: 16px;"></i>
                </div>
            </div>
            
            <div style="margin-top: 30px; display: flex; gap: 12px;">
                <button type="submit" class="btn btn-primary" style="flex: 1; height: 46px;">
                    <i class="bi bi-save"></i> 确认保存
                </button>
                <a href="${pageContext.request.contextPath}/admin/book" class="btn btn-secondary" style="flex: 1; height: 46px;">
                    取消
                </a>
            </div>
        </form>
    </div>
</div>
</body>
</html>
