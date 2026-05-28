<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>404 - 页面不存在</title>
    <!-- 引入 Bootstrap 图标 CDN -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap-icons.min.css">
    <!-- 引入全局高端 UI 样式 -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
    <style>
        .error-number {
            font-size: 96px;
            font-weight: 800;
            line-height: 1;
            margin-bottom: 12px;
            background: linear-gradient(135deg, #f59e0b 0%, #ef4444 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }
    </style>
</head>
<body class="auth-page">
    <div class="glass-card auth-card" style="text-align: center;">
        <div class="error-number">404</div>
        <h2 style="margin-bottom: 12px;">页面迷路了</h2>
        <p style="color: var(--text-secondary); font-size: 14px; margin-bottom: 28px; line-height: 1.6;">
            抱歉，您所访问的页面不存在或已被管理员移除。<br>请确认输入 URL 是否正确。
        </p>
        <a href="${pageContext.request.contextPath}/login" class="btn btn-primary" style="width: 100%; height: 46px;">
            <i class="bi bi-house-door-fill"></i> 返回系统首页
        </a>
    </div>
</body>
</html>
