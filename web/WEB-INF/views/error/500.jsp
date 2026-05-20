<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>500 - 服务器内部错误</title>
    <!-- 引入 Bootstrap 图标 CDN -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <!-- 引入全局高端 UI 样式 -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
    <style>
        .error-number {
            font-size: 96px;
            font-weight: 800;
            line-height: 1;
            margin-bottom: 12px;
            background: linear-gradient(135deg, #ef4444 0%, #b91c1c 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }
        .error-details-box {
            background: rgba(0, 0, 0, 0.4);
            border: 1px solid var(--glass-border);
            border-radius: 8px;
            padding: 16px;
            margin: 20px 0;
            text-align: left;
            font-family: monospace;
            font-size: 13px;
            color: #fca5a5;
            overflow-x: auto;
            max-height: 200px;
        }
    </style>
</head>
<body class="auth-page">
    <div class="glass-card auth-card-wide" style="text-align: center;">
        <div class="error-number">500</div>
        <h2 style="margin-bottom: 12px;">服务器内部发生错误</h2>
        <p style="color: var(--text-secondary); font-size: 14px; line-height: 1.6;">
            抱歉，服务器处理您的请求时遭遇意外错误。<br>请稍后重新尝试您的操作。
        </p>

        <% 
            String errorMsg = (String) request.getAttribute("error");
            if (errorMsg != null || exception != null) {
        %>
            <div class="error-details-box">
                <div style="font-weight: bold; margin-bottom: 6px; color: white;"><i class="bi bi-bug"></i> 异常诊断详情：</div>
                <% if (errorMsg != null) { %>
                    <div><%= errorMsg %></div>
                <% } %>
                <% if (exception != null) { %>
                    <div style="margin-top: 4px;"><%= exception.getClass().getName() %>: <%= exception.getMessage() %></div>
                <% } %>
            </div>
        <% } %>

        <div style="display: flex; gap: 12px; margin-top: 24px;">
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-secondary" style="flex: 1; height: 46px;">
                <i class="bi bi-speedometer2"></i> 管理员控制台
            </a>
            <a href="${pageContext.request.contextPath}/login" class="btn btn-primary" style="flex: 1; height: 46px;">
                <i class="bi bi-house-door-fill"></i> 返回首页
            </a>
        </div>
    </div>
</body>
</html>
