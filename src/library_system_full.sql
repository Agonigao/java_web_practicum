-- 吕梁学院计科系资料室管理系统 - 完整数据库设计脚本
-- 包含：用户表、读者类型表、读者信息表、图书类型表、图书信息表、图书借阅表、图书归还表

DROP DATABASE IF EXISTS library_system;
CREATE DATABASE library_system CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE library_system;

-- 1. 用户表 (users) - 系统登录账号
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '登录账号',
    password VARCHAR(100) NOT NULL COMMENT '登录密码',
    role VARCHAR(20) DEFAULT 'reader' COMMENT '角色：admin-管理员，reader-读者',
    status INT DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- 2. 读者类型表 (reader_type)
CREATE TABLE reader_type (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '类型ID',
    type_name VARCHAR(50) NOT NULL COMMENT '类型名称（如：本科生、教师）',
    max_borrow_num INT DEFAULT 5 COMMENT '最大可借数量',
    borrow_limit_days INT DEFAULT 30 COMMENT '默认借阅天数',
    renew_limit INT DEFAULT 1 COMMENT '最大续借次数'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='读者类型表';

-- 3. 读者信息表 (reader_info)
CREATE TABLE reader_info (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '读者ID',
    user_id INT NOT NULL COMMENT '关联用户表ID',
    reader_type_id INT NOT NULL COMMENT '关联读者类型ID',
    real_name VARCHAR(50) NOT NULL COMMENT '真实姓名',
    student_id VARCHAR(20) COMMENT '学号/工号',
    phone VARCHAR(20) COMMENT '联系电话',
    email VARCHAR(100) COMMENT '邮箱',
    department VARCHAR(100) COMMENT '所在院系（如：计科系）',
    register_date DATE COMMENT '注册日期',
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (reader_type_id) REFERENCES reader_type(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='读者信息表';

-- 4. 图书类型表 (book_category)
CREATE TABLE book_category (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '类型ID',
    category_name VARCHAR(50) NOT NULL COMMENT '类型名称（如：计算机、文学）',
    description VARCHAR(200) COMMENT '类型描述'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='图书类型表';

-- 5. 图书信息表 (book_info)
CREATE TABLE book_info (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '图书ID',
    category_id INT NOT NULL COMMENT '图书类型ID',
    isbn VARCHAR(20) COMMENT 'ISBN号',
    title VARCHAR(200) NOT NULL COMMENT '书名',
    author VARCHAR(100) COMMENT '作者',
    publisher VARCHAR(100) COMMENT '出版社',
    publish_date DATE COMMENT '出版日期',
    price DECIMAL(10, 2) COMMENT '价格',
    total_count INT DEFAULT 0 COMMENT '总库存',
    available_count INT DEFAULT 0 COMMENT '可借库存',
    location VARCHAR(100) COMMENT '存放位置（如：A区-101架）',
    add_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '入库时间',
    FOREIGN KEY (category_id) REFERENCES book_category(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='图书信息表';

-- 6. 图书借阅表 (borrow_record)
CREATE TABLE borrow_record (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '借阅ID',
    reader_id INT NOT NULL COMMENT '读者ID',
    book_id INT NOT NULL COMMENT '图书ID',
    borrow_date DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '借阅时间',
    due_date DATETIME COMMENT '应还时间',
    status INT DEFAULT 0 COMMENT '状态：0-借阅中，1-已归还，2-逾期',
    renew_count INT DEFAULT 0 COMMENT '续借次数',
    FOREIGN KEY (reader_id) REFERENCES reader_info(id),
    FOREIGN KEY (book_id) REFERENCES book_info(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='图书借阅表';

-- 7. 图书归还表 (return_record)
CREATE TABLE return_record (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '归还ID',
    borrow_id INT NOT NULL COMMENT '关联借阅表ID',
    reader_id INT NOT NULL COMMENT '读者ID',
    book_id INT NOT NULL COMMENT '图书ID',
    return_date DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '实际归还时间',
    is_overdue INT DEFAULT 0 COMMENT '是否逾期：0-否，1-是',
    fine_amount DECIMAL(10, 2) DEFAULT 0.00 COMMENT '逾期罚款金额',
    operator_id INT COMMENT '操作归还的管理员ID',
    FOREIGN KEY (borrow_id) REFERENCES borrow_record(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='图书归还表';

-- ================= 初始化测试数据 =================

-- 1. 插入用户
INSERT INTO users (username, password, role) VALUES 
('admin', 'admin123', 'admin'),
('zhangsan', '123456', 'reader');

-- 2. 插入读者类型
INSERT INTO reader_type (type_name, max_borrow_num, borrow_limit_days, renew_limit) VALUES 
('本科生', 5, 30, 1),
('教师', 10, 60, 2);

-- 3. 插入读者信息
INSERT INTO reader_info (user_id, reader_type_id, real_name, student_id, department) VALUES 
(2, 1, '张三', '2021001', '计算机科学系');

-- 4. 插入图书类型
INSERT INTO book_category (category_name, description) VALUES 
('计算机', '编程、算法、硬件等'),
('文学', '小说、散文等'),
('历史', '历史传记、史料等');

-- 5. 插入图书
INSERT INTO book_info (category_id, isbn, title, author, publisher, publish_date, price, total_count, available_count, location) VALUES 
(1, '978-7-111-11111-1', 'Java 核心技术', 'Cay S. Horstmann', '机械工业出版社', '2020-01-01', 129.00, 5, 5, 'A101'),
(1, '978-7-115-22222-2', '算法导论', 'Thomas H. Cormen', '机械工业出版社', '2019-06-01', 138.00, 3, 3, 'A102'),
(2, '978-7-020-33333-3', '红楼梦', '曹雪芹', '人民文学出版社', '2008-07-01', 59.80, 8, 8, 'B201');

-- 查询验证
SELECT '✅ 数据库初始化完成！' AS result;
SELECT '用户表：' AS info; SELECT * FROM users;
SELECT '读者类型表：' AS info; SELECT * FROM reader_type;
SELECT '图书类型表：' AS info; SELECT * FROM book_category;
SELECT '图书信息表：' AS info; SELECT * FROM book_info;
