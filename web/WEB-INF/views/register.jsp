<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>注册 - 吕梁学院资料室管理系统</title>
    <!-- 引入 Bootstrap 图标 CDN -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap-icons.min.css">
    <!-- 引入全局高端 UI 样式 -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
    <style>
        .register-icon-header {
            font-size: 40px;
            background: linear-gradient(135deg, #a5b4fc 0%, #6366f1 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            margin-bottom: 12px;
            display: inline-block;
        }
    </style>
</head>
<body class="auth-page">
<div class="glass-card auth-card-wide">
    <div class="auth-header">
        <div class="register-icon-header">
            <i class="bi bi-person-plus-fill"></i>
        </div>
        <h1>新读者注册</h1>
        <p>创建您的读者账号以借阅资料</p>
    </div>

    <%
        String error = (String) request.getAttribute("error");
        String success = (String) request.getAttribute("success");
    %>

    <% if (error != null) { %>
    <div class="alert-box alert-danger">
        <i class="bi bi-exclamation-triangle-fill" style="font-size: 18px;"></i>
        <div><%= error %></div>
    </div>
    <% } %>

    <% if (success != null) { %>
    <div class="alert-box alert-success">
        <i class="bi bi-check-circle-fill" style="font-size: 18px;"></i>
        <div><%= success %></div>
    </div>
    <% } %>

    <form action="${pageContext.request.contextPath}/register" method="post">
        <div class="form-row">
            <div class="form-group">
                <label for="username">用户名 *</label>
                <div style="position: relative;">
                    <input type="text" id="username" name="username" placeholder="请输入用户名" required style="padding-left: 40px;">
                    <i class="bi bi-person" style="position: absolute; left: 14px; top: 12px; color: var(--text-muted); font-size: 16px;"></i>
                </div>
            </div>
            <div class="form-group">
                <label for="realName">真实姓名 *</label>
                <div style="position: relative;">
                    <input type="text" id="realName" name="realName" placeholder="请输入真实姓名" required style="padding-left: 40px;">
                    <i class="bi bi-card-text" style="position: absolute; left: 14px; top: 12px; color: var(--text-muted); font-size: 16px;"></i>
                </div>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="studentId">学号/工号 *</label>
                <div style="position: relative;">
                    <input type="text" id="studentId" name="studentId" placeholder="请输入学号或工号" required style="padding-left: 40px;">
                    <i class="bi bi-mortarboard" style="position: absolute; left: 14px; top: 12px; color: var(--text-muted); font-size: 16px;"></i>
                </div>
            </div>
            <div class="form-group">
                <label for="readerType">读者类型 *</label>
                <div style="position: relative;">
                    <select id="readerType" name="readerType" required style="padding-left: 40px; appearance: none; -webkit-appearance: none;">
                        <option value="">请选择读者类型</option>
                        <option value="1">本科生</option>
                        <option value="2">研究生</option>
                        <option value="3">教师</option>
                        <option value="4">其他</option>
                    </select>
                    <i class="bi bi-tags" style="position: absolute; left: 14px; top: 12px; color: var(--text-muted); font-size: 16px;"></i>
                    <i class="bi bi-chevron-down" style="position: absolute; right: 14px; top: 14px; color: var(--text-muted); font-size: 12px; pointer-events: none;"></i>
                </div>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="department">所在院系 *</label>
                <div style="position: relative;">
                    <input type="text" id="department" name="department" placeholder="请输入所在院系" required style="padding-left: 40px;">
                    <i class="bi bi-building" style="position: absolute; left: 14px; top: 12px; color: var(--text-muted); font-size: 16px;"></i>
                </div>
            </div>
            <div class="form-group">
                <label for="phone">联系电话</label>
                <div style="position: relative;">
                    <input type="tel" id="phone" name="phone" placeholder="请输入手机号码" style="padding-left: 40px;">
                    <i class="bi bi-telephone" style="position: absolute; left: 14px; top: 12px; color: var(--text-muted); font-size: 16px;"></i>
                </div>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="email">邮箱</label>
                <div style="position: relative;">
                    <input type="email" id="email" name="email" placeholder="请输入邮箱地址" style="padding-left: 40px;">
                    <i class="bi bi-envelope" style="position: absolute; left: 14px; top: 12px; color: var(--text-muted); font-size: 16px;"></i>
                </div>
            </div>
            <div class="form-group">
                <label for="password">密码 *</label>
                <div style="position: relative;">
                    <input type="password" id="password" name="password" placeholder="请输入密码" required style="padding-left: 40px;">
                    <i class="bi bi-lock" style="position: absolute; left: 14px; top: 12px; color: var(--text-muted); font-size: 16px;"></i>
                </div>
            </div>
        </div>

        <div class="form-group">
            <label for="confirmPassword">确认密码 *</label>
            <div style="position: relative;">
                <input type="password" id="confirmPassword" name="confirmPassword" placeholder="请再次输入密码" required style="padding-left: 40px;">
                <i class="bi bi-lock-fill" style="position: absolute; left: 14px; top: 12px; color: var(--text-muted); font-size: 16px;"></i>
            </div>
        </div>

        <button type="submit" class="btn btn-primary" style="width: 100%; height: 46px; margin-top: 12px;">
            立即注册 <i class="bi bi-person-check-fill" style="font-size: 18px;"></i>
        </button>
    </form>

    <div class="auth-footer-link">
        已有账号？<a href="${pageContext.request.contextPath}/login">立即登录</a>
    </div>
</div>
</body>
</html>