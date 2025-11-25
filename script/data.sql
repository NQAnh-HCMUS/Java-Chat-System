USE ChatSystem;
GO
-- 1. Chèn dữ liệu người dùng TRƯỚC
INSERT INTO users (username, password, full_name, address, date_of_birth, gender, email, is_active, is_locked) VALUES
('admin', 'admin123', N'Quan Tri Vien', N'Ha Noi', '1990-01-01', 'Male', 'admin@chatsystem.com', 1, 0),
('john_doe', 'password123', N'John Doe', N'TP Ho Chi Minh', '1995-05-15', 'Male', 'john.doe@email.com', 1, 0),
('jane_smith', 'password123', N'Jane Smith', N'Da Nang', '1998-08-20', 'Female', 'jane.smith@email.com', 1, 0),
('mike_wilson', 'password123', N'Mike Wilson', N'Hai Phong', '1993-03-10', 'Male', 'mike.wilson@email.com', 1, 0),
('sarah_johnson', 'password123', N'Sarah Johnson', N'Can Tho', '1996-07-25', 'Female', 'sarah.johnson@email.com', 1, 0),
('david_brown', 'password123', N'David Brown', N'Nha Trang', '1994-11-30', 'Male', 'david.brown@email.com', 1, 0),
('linda_wang', 'password123', N'Linda Wang', N'Hue', '1997-02-14', 'Female', 'linda.wang@email.com', 1, 0),
('robert_chen', 'password123', N'Robert Chen', N'Vung Tau', '1992-09-05', 'Male', 'robert.chen@email.com', 1, 0),
('emily_tran', 'password123', N'Emily Tran', N'Bien Hoa', '1999-04-18', 'Female', 'emily.tran@email.com', 1, 0),
('michael_nguyen', 'password123', N'Michael Nguyen', N'Ha Long', '1991-12-22', 'Male', 'michael.nguyen@email.com', 1, 0),
('sophia_le', 'password123', N'Sophia Le', N'Quy Nhon', '1998-06-08', 'Female', 'sophia.le@email.com', 1, 0),
('william_pham', 'password123', N'William Pham', N'Thanh Hoa', '1993-03-03', 'Male', 'william.pham@email.com', 1, 0),
('olivia_vo', 'password123', N'Olivia Vo', N'Vinh', '1996-10-12', 'Female', 'olivia.vo@email.com', 1, 0),
('james_hoang', 'password123', N'James Hoang', N'Buon Ma Thuot', '1994-07-07', 'Male', 'james.hoang@email.com', 1, 0),
('ava_do', 'password123', N'Ava Do', N'Pleiku', '1997-01-28', 'Female', 'ava.do@email.com', 1, 0),
('spam_user', 'password123', N'Spam User', N'Unknown', '2000-01-01', 'Male', 'spam@email.com', 1, 1);
GO

-- 2. Chèn lịch sử đăng nhập
INSERT INTO login_history (user_id, login_time, ip_address) VALUES
(1, '2024-01-15 08:30:00', '192.168.1.100'),
(2, '2024-01-15 09:15:00', '192.168.1.101'),
(3, '2024-01-15 10:20:00', '192.168.1.102'),
(4, '2024-01-15 11:05:00', '192.168.1.103'),
(5, '2024-01-15 13:45:00', '192.168.1.104'),
(6, '2024-01-15 14:30:00', '192.168.1.105'),
(7, '2024-01-15 15:20:00', '192.168.1.106'),
(8, '2024-01-15 16:10:00', '192.168.1.107'),
(2, '2024-01-16 08:25:00', '192.168.1.101'),
(3, '2024-01-16 09:10:00', '192.168.1.102'),
(4, '2024-01-16 10:05:00', '192.168.1.103'),
(5, '2024-01-16 11:20:00', '192.168.1.104'),
(6, '2024-01-16 14:15:00', '192.168.1.105'),
(7, '2024-01-16 15:30:00', '192.168.1.106'),
(8, '2024-01-16 16:45:00', '192.168.1.107');
GO

-- 3. Chèn quan hệ bạn bè
INSERT INTO friendships (user_id1, user_id2, status, created_at) VALUES
(2, 3, 'accepted', '2024-01-10 08:00:00'),
(2, 4, 'accepted', '2024-01-10 09:00:00'),
(2, 5, 'accepted', '2024-01-10 10:00:00'),
(2, 6, 'pending', '2024-01-11 11:00:00'),
(3, 4, 'accepted', '2024-01-11 12:00:00'),
(3, 7, 'accepted', '2024-01-11 13:00:00'),
(3, 8, 'accepted', '2024-01-12 14:00:00'),
(4, 5, 'accepted', '2024-01-12 15:00:00'),
(4, 9, 'accepted', '2024-01-13 16:00:00'),
(5, 6, 'accepted', '2024-01-13 17:00:00');
GO

-- 4. Chèn nhóm chat
INSERT INTO chat_groups (group_name, created_by, created_at) VALUES
(N'Nhom ban than', 2, '2024-01-05 10:00:00'),
(N'Cong viec', 1, '2024-01-06 09:00:00'),
(N'Gia dinh', 3, '2024-01-07 14:00:00'),
(N'Hoc tap', 4, '2024-01-08 16:00:00'),
(N'Du lich', 5, '2024-01-09 11:00:00');
PRINT 'Đã chèn xong nhóm chat.';
GO

-- 5. Chèn thành viên nhóm
INSERT INTO group_members (group_id, user_id, is_admin, joined_at) VALUES
-- Nhóm 1: Nhom ban than
(1, 2, 1, '2024-01-05 10:00:00'),
(1, 3, 0, '2024-01-05 10:05:00'),
(1, 4, 0, '2024-01-05 10:10:00'),
(1, 5, 0, '2024-01-05 10:15:00'),

-- Nhóm 2: Cong viec
(2, 1, 1, '2024-01-06 09:00:00'),
(2, 2, 0, '2024-01-06 09:05:00'),
(2, 3, 0, '2024-01-06 09:10:00'),

-- Nhóm 3: Gia dinh
(3, 3, 1, '2024-01-07 14:00:00'),
(3, 8, 0, '2024-01-07 14:05:00'),
(3, 9, 0, '2024-01-07 14:10:00'),

-- Nhóm 4: Hoc tap
(4, 4, 1, '2024-01-08 16:00:00'),
(4, 10, 0, '2024-01-08 16:05:00'),
(4, 11, 0, '2024-01-08 16:10:00'),

-- Nhóm 5: Du lich
(5, 5, 1, '2024-01-09 11:00:00'),
(5, 13, 0, '2024-01-09 11:05:00'),
(5, 14, 0, '2024-01-09 11:10:00');
GO

-- 6. Chèn tin nhắn cá nhân
INSERT INTO private_messages (sender_id, receiver_id, message_content, sent_at, is_read) VALUES
(2, 3, N'Chao ban! Ban khoe khong?', '2024-01-15 10:30:00', 1),
(3, 2, N'Minh khoe, cam on ban! Con ban thi sao?', '2024-01-15 10:31:00', 1),
(2, 3, N'Minh cung khoe. Toi nay di cafe khong?', '2024-01-15 10:32:00', 1),
(3, 2, N'Duoc do, may gio gap?', '2024-01-15 10:33:00', 1),
(2, 4, N'Ban co tai lieu du an khong?', '2024-01-16 14:20:00', 1),
(4, 2, N'Co, minh se gui cho ban sau.', '2024-01-16 14:21:00', 1),
(5, 6, N'Hom nay hop luc may gio?', '2024-01-16 15:10:00', 1),
(6, 5, N'3 gio chieu nhe!', '2024-01-16 15:11:00', 1),
(7, 8, N'Ban da xem phim do chua?', '2024-01-17 20:15:00', 0),
(8, 9, N'Mai di an trua khong?', '2024-01-17 21:00:00', 0);
GO

-- 7. Chèn tin nhắn nhóm
INSERT INTO group_messages (group_id, sender_id, message_content, sent_at) VALUES
(1, 2, N'Chao moi nguoi!', '2024-01-15 11:00:00'),
(1, 3, N'Chao ca nha!', '2024-01-15 11:01:00'),
(1, 4, N'Cuoi tuan nay co ke hoach gi khong?', '2024-01-15 11:02:00'),
(2, 1, N'Chao moi nguoi, hom nay chung ta hop luc 2 gio', '2024-01-16 08:30:00'),
(2, 2, N'OK, minh se tham gia day du', '2024-01-16 08:31:00'),
(3, 3, N'Chuc mung nam moi ca nha!', '2024-01-17 00:00:00'),
(3, 8, N'Chuc mung nam moi!', '2024-01-17 00:01:00'),
(4, 4, N'Ai co tai lieu hoc tap khong?', '2024-01-17 14:20:00'),
(4, 10, N'Minh co, de minh gui cho', '2024-01-17 14:21:00'),
(5, 5, N'Moi nguoi muon di dau du lich?', '2024-01-18 10:15:00');
GO

-- 8. Chèn báo cáo spam
INSERT INTO spam_reports (reporter_id, reported_user_id, message_id, report_reason, reported_at, status) VALUES
(2, 16, NULL, N'Nguoi dung gui tin nhan spam lien tuc', '2024-01-14 15:30:00', 'resolved'),
(3, 16, NULL, N'Quang cao san pham khong mong muon', '2024-01-14 16:20:00', 'resolved'),
(4, 16, NULL, N'Tin nhan khieu khich', '2024-01-15 11:45:00', 'pending'),
(5, 7, NULL, N'Lam phien qua nhieu', '2024-01-16 14:10:00', 'pending'),
(6, 16, NULL, N'Gui link doc hai', '2024-01-17 09:15:00', 'resolved');
GO

-- 9. Chèn hoạt động người dùng
INSERT INTO user_activities (user_id, activity_date, login_count, private_chat_partners, group_chat_count) VALUES
(1, '2024-01-15', 1, 0, 1),
(2, '2024-01-15', 1, 3, 2),
(3, '2024-01-15', 1, 2, 2),
(4, '2024-01-15', 1, 2, 1),
(5, '2024-01-15', 1, 1, 1),
(6, '2024-01-15', 1, 1, 1),
(7, '2024-01-15', 1, 1, 1),
(8, '2024-01-15', 1, 1, 1),
(2, '2024-01-16', 1, 2, 1),
(3, '2024-01-16', 1, 1, 1);
GO