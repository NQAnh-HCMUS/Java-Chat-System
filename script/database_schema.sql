--DROP DATABASE IF EXISTS ChatSystem;

CREATE DATABASE ChatSystem;
GO

USE ChatSystem;
GO

-- Bảng người dùng
CREATE TABLE users (
    user_id INT PRIMARY KEY IDENTITY(1,1),
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name NVARCHAR(100) NOT NULL,
    address NVARCHAR(200),
    date_of_birth DATE,
    gender NVARCHAR(10) CHECK (gender IN ('Male', 'Female', 'Other')),
    email VARCHAR(100) UNIQUE NOT NULL,
    is_active BIT DEFAULT 1,
    is_locked BIT DEFAULT 0,
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE()
);
GO

-- Bảng lịch sử đăng nhập
CREATE TABLE login_history (
    login_id INT PRIMARY KEY IDENTITY(1,1),
    user_id INT,
    login_time DATETIME DEFAULT GETDATE(),
    ip_address VARCHAR(45),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);
GO

-- Bảng bạn bè
CREATE TABLE friendships (
    friendship_id INT PRIMARY KEY IDENTITY(1,1),
    user_id1 INT,
    user_id2 INT,
    status NVARCHAR(10) DEFAULT 'pending' CHECK (status IN ('pending', 'accepted', 'blocked')),
    created_at DATETIME DEFAULT GETDATE(),
    blocked_by INT NULL,
    FOREIGN KEY (user_id1) REFERENCES users(user_id),
    FOREIGN KEY (user_id2) REFERENCES users(user_id),
    FOREIGN KEY (blocked_by) REFERENCES users(user_id),
    CONSTRAINT unique_friendship UNIQUE (user_id1, user_id2)
);
GO

-- Bảng nhóm chat
CREATE TABLE chat_groups (
    group_id INT PRIMARY KEY IDENTITY(1,1),
    group_name NVARCHAR(100) NOT NULL,
    created_by INT,
    created_at DATETIME DEFAULT GETDATE(),
    is_encrypted BIT DEFAULT 0,
    encryption_key VARCHAR(255),
    FOREIGN KEY (created_by) REFERENCES users(user_id)
);
GO

-- Bảng thành viên nhóm
CREATE TABLE group_members (
    group_member_id INT PRIMARY KEY IDENTITY(1,1),
    group_id INT,
    user_id INT,
    is_admin BIT DEFAULT 0,
    joined_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (group_id) REFERENCES chat_groups(group_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT unique_member UNIQUE (group_id, user_id)
);
GO

-- Bảng tin nhắn cá nhân
CREATE TABLE private_messages (
    message_id INT PRIMARY KEY IDENTITY(1,1),
    sender_id INT,
    receiver_id INT,
    message_content TEXT NOT NULL,
    sent_at DATETIME DEFAULT GETDATE(),
    is_read BIT DEFAULT 0,
    is_deleted BIT DEFAULT 0,
    FOREIGN KEY (sender_id) REFERENCES users(user_id),
    FOREIGN KEY (receiver_id) REFERENCES users(user_id)
);
GO

-- Bảng tin nhắn nhóm
CREATE TABLE group_messages (
    message_id INT PRIMARY KEY IDENTITY(1,1),
    group_id INT,
    sender_id INT,
    message_content TEXT NOT NULL,
    sent_at DATETIME DEFAULT GETDATE(),
    is_deleted BIT DEFAULT 0,
    FOREIGN KEY (group_id) REFERENCES chat_groups(group_id),
    FOREIGN KEY (sender_id) REFERENCES users(user_id)
);
GO

-- Bảng báo cáo spam
CREATE TABLE spam_reports (
    report_id INT PRIMARY KEY IDENTITY(1,1),
    reporter_id INT,
    reported_user_id INT,
    message_id INT NULL,
    report_reason TEXT,
    reported_at DATETIME DEFAULT GETDATE(),
    status NVARCHAR(10) DEFAULT 'pending' CHECK (status IN ('pending', 'resolved')),
    FOREIGN KEY (reporter_id) REFERENCES users(user_id),
    FOREIGN KEY (reported_user_id) REFERENCES users(user_id)
);
GO

-- Bảng hoạt động người dùng
CREATE TABLE user_activities (
    activity_id INT PRIMARY KEY IDENTITY(1,1),
    user_id INT,
    activity_date DATE,
    login_count INT DEFAULT 0,
    private_chat_partners INT DEFAULT 0,
    group_chat_count INT DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT unique_activity UNIQUE (user_id, activity_date)
);
GO