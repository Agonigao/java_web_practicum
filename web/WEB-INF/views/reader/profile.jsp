<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>个人中心 - 吕梁学院资料室管理系统</title>
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
        <a href="${pageContext.request.contextPath}/reader/book"><i class="bi bi-search"></i> 图书查询</a>
        <a href="${pageContext.request.contextPath}/reader/myborrow"><i class="bi bi-journals"></i> 我的借阅</a>
        <a href="${pageContext.request.contextPath}/reader/profile" class="active"><i class="bi bi-person-bounding-box"></i> 个人信息</a>
        <a href="${pageContext.request.contextPath}/logout" class="btn-logout"><i class="bi bi-box-arrow-right"></i> 退出</a>
    </div>
</div>

<div class="main-container" style="max-width: 720px;">
    <div class="glass-card">
        <div class="page-header-block">
            <h2><i class="bi bi-person-circle" style="color: #6366f1;"></i> 个人档案信息</h2>
            <a href="${pageContext.request.contextPath}/reader/book" class="btn btn-secondary">
                <i class="bi bi-arrow-left"></i> 返回查询
            </a>
        </div>

        <c:if test="${param.msg == 'updated'}">
            <div class="alert-box alert-success" style="margin-bottom: 24px;">
                <i class="bi bi-check-circle-fill" style="font-size: 18px;"></i>
                <div>您的个人档案资料已成功更新保存！</div>
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/reader/profile?action=update" method="post">
            <div class="form-row">
                <div class="form-group">
                    <label>真实姓名 *</label>
                    <div style="position: relative;">
                        <input type="text" name="realName" value="${reader.realName}" required placeholder="请输入您的真实姓名" style="padding-left: 40px;">
                        <i class="bi bi-card-text" style="position: absolute; left: 14px; top: 12px; color: var(--text-muted); font-size: 16px;"></i>
                    </div>
                </div>
                <div class="form-group">
                    <label>学号/工号 <span style="font-size: 11px; color: var(--text-muted); font-weight: 500;">(只读，不可修改)</span></label>
                    <div style="position: relative;">
                        <input type="text" value="${reader.studentId}" readonly style="padding-left: 40px; background: rgba(0,0,0,0.25); border-color: rgba(255,255,255,0.05); color: var(--text-muted); cursor: not-allowed;">
                        <i class="bi bi-mortarboard-fill" style="position: absolute; left: 14px; top: 12px; color: var(--text-muted); font-size: 16px;"></i>
                    </div>
                </div>
            </div>
            
            <div class="form-group">
                <label>所在院系 *</label>
                <div style="position: relative;">
                    <input type="text" name="department" value="${reader.department}" required placeholder="请输入您所在的所属院系" style="padding-left: 40px;">
                    <i class="bi bi-building" style="position: absolute; left: 14px; top: 12px; color: var(--text-muted); font-size: 16px;"></i>
                </div>
            </div>
            
            <div class="form-row">
                <div class="form-group">
                    <label>联系电话</label>
                    <div style="position: relative;">
                        <input type="tel" name="phone" value="${reader.phone}" placeholder="请输入联系电话/手机" style="padding-left: 40px;">
                        <i class="bi bi-telephone" style="position: absolute; left: 14px; top: 12px; color: var(--text-muted); font-size: 16px;"></i>
                    </div>
                </div>
                <div class="form-group">
                    <label>电子邮箱</label>
                    <div style="position: relative;">
                        <input type="email" name="email" value="${reader.email}" placeholder="请输入常用电子邮箱" style="padding-left: 40px;">
                        <i class="bi bi-envelope" style="position: absolute; left: 14px; top: 12px; color: var(--text-muted); font-size: 16px;"></i>
                    </div>
                </div>
            </div>
            
            <button type="submit" class="btn btn-primary" style="width: 100%; height: 46px; margin-top: 16px;">
                <i class="bi bi-save2"></i> 保存修改资料
            </button>
        </form>
    </div>
</div>
</body>
</html>
