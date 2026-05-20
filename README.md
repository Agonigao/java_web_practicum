# 📚 吕梁学院资料室管理系统 (Lvliang University Library Management System)

[![Java](https://img.shields.io/badge/Java-17%2B-orange.svg?style=flat-square&logo=openjdk)](https://openjdk.org/)
[![Jakarta EE](https://img.shields.io/badge/Jakarta%20EE-10%2FServlet%206.0-blue.svg?style=flat-square)](https://jakarta.ee/)
[![Tomcat](https://img.shields.io/badge/Tomcat-11.0%2B-red.svg?style=flat-square&logo=apache-tomcat)](https://tomcat.apache.org/)
[![MySQL](https://img.shields.io/badge/MySQL-9.7%20%2F%208.x-blue.svg?style=flat-square&logo=mysql)](https://www.mysql.com/)
[![Aesthetic](https://img.shields.io/badge/UI-Glassmorphism%20%2F%20Modern-purple.svg?style=flat-square)](#-界面视觉-ui-showcase)
[![Encoding](https://img.shields.io/badge/Encoding-UTF--8%20Compliant-success.svg?style=flat-square)](#-核心优化与系统特性)

基于 **Jakarta EE 10 (Servlet 6.0 & JSP 3.1)** 与 **MySQL** 架构构建的高端、现代化高校资料室管理系统。项目全面升级了原有的旧版 JSP 架构，注入了极具现代科技感的 **Glassmorphism（毛玻璃/渐变微光）UI 设计体系**，并深度优化了系统级的 **UTF-8 字符流通道**，完美解决了传统 Java Web 项目中常见的中文字符乱码问题。

---

## 🌟 核心优化与系统特性

### 1. 💎 Glassmorphism 现代美学设计
- **视觉惊艳**：摒弃传统教条式、简陋的表单页面，全站引入深邃和谐的渐变暗色背景与高透光率的毛玻璃（Glassmorphism）卡片面板。
- **动态交互**：全站按钮及交互卡片具备顺滑的过渡动画（Transitions）与微光悬停效果（Hover Effects），大幅提升操作专注度与感官愉悦度。
- **优雅自适应**：基于现代 CSS Grid 与 Flexbox 构建响应式视口，完美适配各类桌面端、平板与手机浏览器。

### 2. 🔐 完善的角色权限控制 (RBAC)
系统严格区分为 **管理员 (Admin)** 与 **读者 (Reader)** 双重身份，并设计了全套业务流程：
- **管理员端**：
  - 📊 **可视化大屏仪表盘**：直观展示图书总数、已借出数、读者总数及逾期归还警示。
  - 📖 **图书全生命周期管理**：图书录入 (ISBN 校验/分类绑定/存放位置指定)、修改、删除、分类配置。
  - 👤 **读者账号管理与规则设定**：读者类别定制（本科生/研究生/教师，可独立限定其最大借阅数、天数及续借限制），读者档案审批。
  - 🔄 **借阅/归还事务台**：集中处理全局借阅历史、代办读者归还与逾期罚款流水。
- **读者端**：
  - 🔍 **智能检索**：支持多维度（书名、作者、ISBN、分类）的实时图书检索。
  - 📅 **借阅清单与自助续借**：实时查询个人正在借阅的图书，根据读者类型规则，提供一键自助续借服务。
  - ⚙️ **个人中心**：可自主查看与更新个人档案（包含学号/工号、所属系部、邮箱、电话等）。

### 3. 🛡️ 动态图形验证码防护 (CAPTCHA)
- 自研图形验证码渲染引擎（`CaptchaServlet` 与 `CaptchaUtil`），在登录与注册核心节点进行双重验证，有效抵御自动化爬虫与暴力破解攻击。

### 4. 🔠 全链路 UTF-8 字符流保障
- 彻底解决 Tomcat 环境下中文乱码的行业痼疾。通过部署全局 `@WebFilter("/*")` 拦截器（`EncodingFilter`），统一配置请求与响应报文的字符编码，结合数据库连接池 `utf8mb4` 字符集编码设置，确保中文字符在 `浏览器 ⇄ Servlet ⇄ 数据库` 的整个生命周期内无损传输。

---

## 📂 项目目录结构说明

```text
java_web_practicum/
├── src/
│   └── com/
│       └── library/
│           ├── dao/          # 数据库交互层 (Data Access Object)
│           │   ├── BookCategoryDAO.java    # 图书分类数据交互
│           │   ├── BookInfoDAO.java        # 图书详情与库存管理
│           │   ├── BorrowRecordDAO.java    # 借阅状态追踪
│           │   ├── ReaderInfoDAO.java      # 读者基本信息交互
│           │   ├── ReaderTypeDAO.java      # 读者级别与借阅规则交互
│           │   ├── ReturnRecordDAO.java    # 归还与逾期罚款交互
│           │   └── UserDAO.java            # 系统账号安全凭证交互
│           ├── entity/       # 业务实体类模型 (POJO)
│           ├── filter/       # 安全与字符集过滤器
│           │   └── EncodingFilter.java     # 全局 UTF-8 字符拦截器
│           ├── servlet/      # 业务逻辑控制器 (Controller)
│           │   ├── AdminServlet.java       # 管理员综合控制
│           │   ├── BookServlet.java        # 图书增删改查路由
│           │   ├── BorrowServlet.java      # 借还书流程控制
│           │   ├── CaptchaServlet.java     # 安全验证码动态绘制
│           │   └── ...
│           └── util/         # 基础支撑工具类
│               ├── CaptchaUtil.java        # 验证码生成算法
│               ├── DBUtil.java             # 现代 MySQL 数据库链接管理
│               └── DBDebugUtil.java        # 数据库状态自检工具
│   └── library_system_full.sql # 完整数据库建表与初始化脚本
├── web/
│   ├── WEB-INF/
│   │   ├── views/            # 受保护的高安全级别视图层 (JSP)
│   │   │   ├── admin/        # 管理员业务模版 (Dashboard/图书管理/读者管理...)
│   │   │   ├── reader/       # 读者业务模版 (图书查询/我的借阅/个人中心)
│   │   │   ├── borrow/       # 借还管理专用视图
│   │   │   ├── error/        # 404/500 高清容错提示页
│   │   │   ├── login.jsp     # 毛玻璃极致视觉登录页
│   │   │   └── register.jsp  # 读者注册页面
│   │   └── web.xml           # 部署描述符 (Session超时、欢迎页与404路由)
│   └── static/
│       └── css/
│           └── style.css     # 全局 Glassmorphism 核心样式设计系统
├── pom.xml                   # Maven 依赖与构建配置文件
└── java-web-practicum.iml    # 开发环境配置文件
```

---

## 🛠️ 技术选型与环境依赖

- **开发工具**：IntelliJ IDEA 2023+ / VS Code
- **运行环境**：JDK 17 或更高版本 (项目编译使用 OpenJDK 17)
- **数据库**：MySQL 8.0 / 9.0 或更高版本
- **Web 容器**：Apache Tomcat 11.0.x (原生兼容 Jakarta EE 10 规范)
- **构建工具**：Apache Maven 3.8+
- **关键依赖**：
  - `jakarta.servlet-api` (6.0.0)
  - `jakarta.servlet.jsp-api` (3.1.0)
  - `jakarta.servlet.jsp.jstl` (3.0.0) - JSTL 标准标签库
  - `mysql-connector-j` (9.7.0) - MySQL 官方高性能驱动

---

## 🚀 快速开始与本地部署

### 第一步：克隆仓库并导入
```bash
git clone https://github.com/your-username/java_web_practicum.git
```
在 IntelliJ IDEA 中选择 `Open`，并指向该目录。IDEA 会自动检测 `pom.xml` 并加载相关依赖。

### 第二步：数据库初始化与连接配置
1. 打开 MySQL 客户端（如 Navicat、Datagrip 或命令行），执行 `src/library_system_full.sql` 脚本：
   ```sql
   SOURCE C:/path/to/your/project/src/library_system_full.sql;
   ```
   *该脚本将自动创建名为 `library_system` 的库，并初始化各类测试数据（图书、读者类型、系统用户及关联角色）。*

2. 检查并修改配置文件 [DBUtil.java](file:///c:/Users/dell/IdeaProjects/java_web_practicum/src/com/library/util/DBUtil.java) 中的数据库账户密码：
   ```java
   private static final String URL = "jdbc:mysql://localhost:3306/library_system?useSSL=false&serverTimezone=UTC&characterEncoding=utf8";
   private static final String USERNAME = "root";  // 您的数据库用户名
   private static final String PASSWORD = "1234";  // 您的数据库密码
   ```

3. **测试连接**：本项目提供了一个独立的自检工具 `DatabaseDebugMain.java`，直接在 IDE 中运行该类，控制台输出 `[DBUtil] ✅ 数据库连接测试通过！` 即代表连接成功！

### 第三步：项目编译与打包
通过 Maven 进行项目构建：
```bash
mvn clean package
```
构建成功后，在根目录的 `target/` 文件夹下会生成名为 `library-system.war` 的发布包。

### 第打四步：部署在 Apache Tomcat 11.0.x
1. **热部署配置**（推荐）：
   - 在 IntelliJ IDEA 中点击 `Run` -> `Edit Configurations...`。
   - 新增 `Tomcat Server` -> `Local`。
   - 在 `Deployment` 选项卡中，添加项目标的 `java-web-practicum:war exploded`。
   - 设置 Application Context 为 `/` 或 `/library-system`。
2. **手动部署**：
   - 将打包好的 `library-system.war` 复制到 Tomcat 的 `webapps/` 目录下，启动 Tomcat 即可。

---

## 🔐 预设测试账号

部署完成后，使用浏览器访问 `http://localhost:8080/library-system/` （具体端口及上下文路径视您的 Tomcat 配置而定），即可进入毛玻璃视觉登录主页。可以使用以下测试账号进行系统体验：

| 登录账号 | 初始密码 | 所属角色 | 体验功能 |
| :--- | :--- | :--- | :--- |
| **`admin`** | `admin123` | **系统管理员** | 仪表盘统计、图书上下架、分类配置、读者类型规则、借还状态控制、系统用户管理 |
| **`zhangsan`** | `123456` | **普通读者 (学生)** | 个人档案修改、模糊图书检索、正在借阅管理、一键自助续借 |

---

## 🎨 界面视觉 (UI Showcase)

> 系统页面全部使用 **高级暗黑渐变背景**，搭配 **Frosted Glass (磨砂玻璃)** 半透明卡片，带来丝滑顺畅的视觉体验：

- **登录与注册页面**：左右居中的磨砂玻璃登录卡片，支持动态验证码实时换图验证，输入字段配有流畅的呼吸灯发光边框。
- **管理员大屏**：顶部排开的渐变发光数据统计块，支持表格记录防溢出处理，具备响应式的操作按钮。
- **借还书事务台**：通过色彩鲜明的徽章（Badges）状态栏（`借阅中` - 蓝色、`已归还` - 绿色、`逾期` - 红色）对图书流向进行精细化追踪。
- **读者图书查询**：毛玻璃检索框配合网格卡片，点击极速响应。

---

## ⚖️ 开源协议 (License)

本项目采用 [MIT License](LICENSE) 协议开源。欢迎在此基础上进行二次开发和学习交流！

---

*✨ 本项目由吕梁学院计科系资料室管理系统研发小组精心打磨，致力于探索极致、高效的 Java Web 实践教学方案。*
