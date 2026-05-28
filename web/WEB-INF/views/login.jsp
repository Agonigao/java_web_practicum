<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>登录 - 吕梁学院资料室管理系统</title>
    <!-- 引入 Bootstrap 图标 CDN -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap-icons.min.css">
    <!-- 引入全局高端 UI 样式 -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
    <style>
        .login-icon-header {
            font-size: 40px;
            background: linear-gradient(135deg, #a5b4fc 0%, #6366f1 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            margin-bottom: 12px;
            display: inline-block;
        }
        .captcha-wrapper {
            display: flex;
            gap: 12px;
        }
        .captcha-wrapper input {
            flex: 1;
        }
        .captcha-img-btn {
            height: 46px;
            cursor: pointer;
            border-radius: 8px;
            border: 1px solid var(--glass-border);
            transition: all 0.25s ease;
        }
        .captcha-img-btn:hover {
            border-color: #6366f1;
            box-shadow: 0 0 10px rgba(99, 102, 241, 0.3);
        }
        .test-accounts-panel {
            margin-top: 24px;
            padding: 16px;
            border-radius: 8px;
            background: rgba(255, 255, 255, 0.03);
            border: 1px solid var(--glass-border);
            font-size: 13px;
        }
        .test-accounts-panel h4 {
            font-size: 13px;
            color: #a5b4fc;
            margin-bottom: 8px;
            display: flex;
            align-items: center;
            gap: 6px;
        }
        .test-accounts-panel table {
            margin: 0;
            width: 100%;
            border-spacing: 0;
        }
        .test-accounts-panel td {
            padding: 4px 0;
            font-size: 12px;
            color: var(--text-secondary);
            border: none;
            background: transparent;
        }
    </style>
</head>
<body class="auth-page">
    <div class="glass-card auth-card">
        <div class="auth-header">
            <div class="login-icon-header">
                <i class="bi bi-journal-bookmark-fill"></i>
            </div>
            <h1>吕梁学院资料室系统</h1>
            <p>请输入您的凭据登录控制台</p>
        </div>

        <c:if test="${not empty error}">
            <div class="alert-box alert-danger">
                <i class="bi bi-exclamation-triangle-fill" style="font-size: 18px;"></i>
                <div>
                    <div>${error}</div>
                    <c:if test="${not empty debug_info}">
                        <div style="margin-top: 8px; font-size: 12px; color: #fca5a5; line-height: 1.5;">
                            ${debug_info}
                        </div>
                    </c:if>
                </div>
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/login" method="post">
            <div class="form-group">
                <label for="username">用户名</label>
                <div style="position: relative;">
                    <input type="text" id="username" name="username" required placeholder="请输入用户名" style="padding-left: 40px;">
                    <i class="bi bi-person" style="position: absolute; left: 14px; top: 12px; color: var(--text-muted); font-size: 16px;"></i>
                </div>
            </div>
            <div class="form-group">
                <label for="password">密码</label>
                <div style="position: relative;">
                    <input type="password" id="password" name="password" required placeholder="请输入密码" style="padding-left: 40px;">
                    <i class="bi bi-lock" style="position: absolute; left: 14px; top: 12px; color: var(--text-muted); font-size: 16px;"></i>
                </div>
            </div>
            <div class="form-group">
                <label for="captcha">验证码</label>
                <div class="captcha-wrapper">
                    <div style="position: relative; flex: 1;">
                        <input type="text" id="captcha" name="captcha" required placeholder="请输入验证码" maxlength="4" style="padding-left: 40px;">
                        <i class="bi bi-shield-check" style="position: absolute; left: 14px; top: 12px; color: var(--text-muted); font-size: 16px;"></i>
                    </div>
                    <img class="captcha-img-btn" src="${pageContext.request.contextPath}/captcha" onclick="this.src='${pageContext.request.contextPath}/captcha?d='+Math.random()" title="点击刷新验证码">
                </div>
            </div>
            <button type="submit" class="btn btn-primary" style="width: 100%; height: 46px; margin-top: 8px;">
                登 录 <i class="bi bi-arrow-right-short" style="font-size: 20px;"></i>
            </button>
        </form>

        <div class="auth-footer-link">
            还没有账号？<a href="${pageContext.request.contextPath}/register">立即注册</a>
        </div>

        <div class="test-accounts-panel">
            <h4><i class="bi bi-shield-lock"></i> 测试账号</h4>
            <table>
                <tr>
                    <td><strong>管理员：</strong> admin / admin123</td>
                </tr>
                <tr>
                    <td><strong>读&nbsp;&nbsp;&nbsp;&nbsp;者：</strong> zhangsan / 123456</td>
                </tr>
            </table>
        </div>
    </div>
</body>
</html>