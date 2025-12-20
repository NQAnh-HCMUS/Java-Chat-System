USE ChatSystem;
GO
-- 1. Chèn dữ liệu người dùng TRƯỚC
INSERT INTO users (username, password, full_name, address, date_of_birth, gender, email, is_active, is_locked) VALUES
('john_doe', 'password123', N'1G$CL474GPEg', N'TP Ho Chi Minh', '1995-05-15', 'Male', 'john.doe@email.com', 1, 0),
('admin', 'admin123', N'Quan Tri Vien', N'Ha Noi', '1990-01-01', 'Male', 'admin@chatsystem.com', 1, 0),
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
('spam_user', 'password123', N'Spam User', N'Unknown', '2000-01-01', 'Male', 'spam@email.com', 1, 1),
('james', 'password123', N'James', N'Unknown', '1990-01-01', 'Other', 'james@example.com', 1, 0),
('mary', 'password123', N'Mary', N'Unknown', '1990-01-01', 'Other', 'mary@example.com', 1, 0),
('john', 'password123', N'John', N'Unknown', '1990-01-01', 'Other', 'john@example.com', 1, 0),
('patricia', 'password123', N'Patricia', N'Unknown', '1990-01-01', 'Other', 'patricia@example.com', 1, 0),
('robert', 'password123', N'Robert', N'Unknown', '1990-01-01', 'Other', 'robert@example.com', 1, 0),
('jennifer', 'password123', N'Jennifer', N'Unknown', '1990-01-01', 'Other', 'jennifer@example.com', 1, 0),
('michael', 'password123', N'Michael', N'Unknown', '1990-01-01', 'Other', 'michael@example.com', 1, 0),
('linda', 'password123', N'Linda', N'Unknown', '1990-01-01', 'Other', 'linda@example.com', 1, 0),
('william', 'password123', N'William', N'Unknown', '1990-01-01', 'Other', 'william@example.com', 1, 0),
('elizabeth', 'password123', N'Elizabeth', N'Unknown', '1990-01-01', 'Other', 'elizabeth@example.com', 1, 0),
('david', 'password123', N'David', N'Unknown', '1990-01-01', 'Other', 'david@example.com', 1, 0),
('barbara', 'password123', N'Barbara', N'Unknown', '1990-01-01', 'Other', 'barbara@example.com', 1, 0),
('richard', 'password123', N'Richard', N'Unknown', '1990-01-01', 'Other', 'richard@example.com', 1, 0),
('susan', 'password123', N'Susan', N'Unknown', '1990-01-01', 'Other', 'susan@example.com', 1, 0),
('joseph', 'password123', N'Joseph', N'Unknown', '1990-01-01', 'Other', 'joseph@example.com', 1, 0),
('jessica', 'password123', N'Jessica', N'Unknown', '1990-01-01', 'Other', 'jessica@example.com', 1, 0),
('thomas', 'password123', N'Thomas', N'Unknown', '1990-01-01', 'Other', 'thomas@example.com', 1, 0),
('sarah', 'password123', N'Sarah', N'Unknown', '1990-01-01', 'Other', 'sarah@example.com', 1, 0),
('charles', 'password123', N'Charles', N'Unknown', '1990-01-01', 'Other', 'charles@example.com', 1, 0),
('karen', 'password123', N'Karen', N'Unknown', '1990-01-01', 'Other', 'karen@example.com', 1, 0),
('christopher', 'password123', N'Christopher', N'Unknown', '1990-01-01', 'Other', 'christopher@example.com', 1, 0),
('nancy', 'password123', N'Nancy', N'Unknown', '1990-01-01', 'Other', 'nancy@example.com', 1, 0),
('daniel', 'password123', N'Daniel', N'Unknown', '1990-01-01', 'Other', 'daniel@example.com', 1, 0),
('lisa', 'password123', N'Lisa', N'Unknown', '1990-01-01', 'Other', 'lisa@example.com', 1, 0),
('matthew', 'password123', N'Matthew', N'Unknown', '1990-01-01', 'Other', 'matthew@example.com', 1, 0),
('betty', 'password123', N'Betty', N'Unknown', '1990-01-01', 'Other', 'betty@example.com', 1, 0),
('anthony', 'password123', N'Anthony', N'Unknown', '1990-01-01', 'Other', 'anthony@example.com', 1, 0),
('sandra', 'password123', N'Sandra', N'Unknown', '1990-01-01', 'Other', 'sandra@example.com', 1, 0),
('mark', 'password123', N'Mark', N'Unknown', '1990-01-01', 'Other', 'mark@example.com', 1, 0),
('margaret', 'password123', N'Margaret', N'Unknown', '1990-01-01', 'Other', 'margaret@example.com', 1, 0),
('donald', 'password123', N'Donald', N'Unknown', '1990-01-01', 'Other', 'donald@example.com', 1, 0),
('ashley', 'password123', N'Ashley', N'Unknown', '1990-01-01', 'Other', 'ashley@example.com', 1, 0),
('steven', 'password123', N'Steven', N'Unknown', '1990-01-01', 'Other', 'steven@example.com', 1, 0),
('kimberly', 'password123', N'Kimberly', N'Unknown', '1990-01-01', 'Other', 'kimberly@example.com', 1, 0),
('paul', 'password123', N'Paul', N'Unknown', '1990-01-01', 'Other', 'paul@example.com', 1, 0),
('emily', 'password123', N'Emily', N'Unknown', '1990-01-01', 'Other', 'emily@example.com', 1, 0),
('andrew', 'password123', N'Andrew', N'Unknown', '1990-01-01', 'Other', 'andrew@example.com', 1, 0),
('donna', 'password123', N'Donna', N'Unknown', '1990-01-01', 'Other', 'donna@example.com', 1, 0),
('joshua', 'password123', N'Joshua', N'Unknown', '1990-01-01', 'Other', 'joshua@example.com', 1, 0),
('michelle', 'password123', N'Michelle', N'Unknown', '1990-01-01', 'Other', 'michelle@example.com', 1, 0),
('kenneth', 'password123', N'Kenneth', N'Unknown', '1990-01-01', 'Other', 'kenneth@example.com', 1, 0),
('carol', 'password123', N'Carol', N'Unknown', '1990-01-01', 'Other', 'carol@example.com', 1, 0),
('kevin', 'password123', N'Kevin', N'Unknown', '1990-01-01', 'Other', 'kevin@example.com', 1, 0),
('amanda', 'password123', N'Amanda', N'Unknown', '1990-01-01', 'Other', 'amanda@example.com', 1, 0),
('brian', 'password123', N'Brian', N'Unknown', '1990-01-01', 'Other', 'brian@example.com', 1, 0),
('dorothy', 'password123', N'Dorothy', N'Unknown', '1990-01-01', 'Other', 'dorothy@example.com', 1, 0),
('george', 'password123', N'George', N'Unknown', '1990-01-01', 'Other', 'george@example.com', 1, 0),
('melissa', 'password123', N'Melissa', N'Unknown', '1990-01-01', 'Other', 'melissa@example.com', 1, 0),
('timothy', 'password123', N'Timothy', N'Unknown', '1990-01-01', 'Other', 'timothy@example.com', 1, 0),
('deborah', 'password123', N'Deborah', N'Unknown', '1990-01-01', 'Other', 'deborah@example.com', 1, 0),
('ronald', 'password123', N'Ronald', N'Unknown', '1990-01-01', 'Other', 'ronald@example.com', 1, 0),
('stephanie', 'password123', N'Stephanie', N'Unknown', '1990-01-01', 'Other', 'stephanie@example.com', 1, 0),
('jason', 'password123', N'Jason', N'Unknown', '1990-01-01', 'Other', 'jason@example.com', 1, 0),
('rebecca', 'password123', N'Rebecca', N'Unknown', '1990-01-01', 'Other', 'rebecca@example.com', 1, 0),
('edward', 'password123', N'Edward', N'Unknown', '1990-01-01', 'Other', 'edward@example.com', 1, 0),
('sharon', 'password123', N'Sharon', N'Unknown', '1990-01-01', 'Other', 'sharon@example.com', 1, 0),
('jeffrey', 'password123', N'Jeffrey', N'Unknown', '1990-01-01', 'Other', 'jeffrey@example.com', 1, 0),
('laura', 'password123', N'Laura', N'Unknown', '1990-01-01', 'Other', 'laura@example.com', 1, 0),
('ryan', 'password123', N'Ryan', N'Unknown', '1990-01-01', 'Other', 'ryan@example.com', 1, 0),
('cynthia', 'password123', N'Cynthia', N'Unknown', '1990-01-01', 'Other', 'cynthia@example.com', 1, 0),
('jacob', 'password123', N'Jacob', N'Unknown', '1990-01-01', 'Other', 'jacob@example.com', 1, 0),
('kathleen', 'password123', N'Kathleen', N'Unknown', '1990-01-01', 'Other', 'kathleen@example.com', 1, 0),
('gary', 'password123', N'Gary', N'Unknown', '1990-01-01', 'Other', 'gary@example.com', 1, 0),
('amy', 'password123', N'Amy', N'Unknown', '1990-01-01', 'Other', 'amy@example.com', 1, 0),
('nicholas', 'password123', N'Nicholas', N'Unknown', '1990-01-01', 'Other', 'nicholas@example.com', 1, 0),
('angela', 'password123', N'Angela', N'Unknown', '1990-01-01', 'Other', 'angela@example.com', 1, 0),
('eric', 'password123', N'Eric', N'Unknown', '1990-01-01', 'Other', 'eric@example.com', 1, 0),
('shirley', 'password123', N'Shirley', N'Unknown', '1990-01-01', 'Other', 'shirley@example.com', 1, 0),
('jonathan', 'password123', N'Jonathan', N'Unknown', '1990-01-01', 'Other', 'jonathan@example.com', 1, 0),
('brenda', 'password123', N'Brenda', N'Unknown', '1990-01-01', 'Other', 'brenda@example.com', 1, 0),
('stephen', 'password123', N'Stephen', N'Unknown', '1990-01-01', 'Other', 'stephen@example.com', 1, 0),
('emma', 'password123', N'Emma', N'Unknown', '1990-01-01', 'Other', 'emma@example.com', 1, 0),
('larry', 'password123', N'Larry', N'Unknown', '1990-01-01', 'Other', 'larry@example.com', 1, 0),
('anna', 'password123', N'Anna', N'Unknown', '1990-01-01', 'Other', 'anna@example.com', 1, 0),
('justin', 'password123', N'Justin', N'Unknown', '1990-01-01', 'Other', 'justin@example.com', 1, 0),
('pamela', 'password123', N'Pamela', N'Unknown', '1990-01-01', 'Other', 'pamela@example.com', 1, 0),
('scott', 'password123', N'Scott', N'Unknown', '1990-01-01', 'Other', 'scott@example.com', 1, 0),
('nicole', 'password123', N'Nicole', N'Unknown', '1990-01-01', 'Other', 'nicole@example.com', 1, 0),
('brandon', 'password123', N'Brandon', N'Unknown', '1990-01-01', 'Other', 'brandon@example.com', 1, 0),
('katherine', 'password123', N'Katherine', N'Unknown', '1990-01-01', 'Other', 'katherine@example.com', 1, 0),
('benjamin', 'password123', N'Benjamin', N'Unknown', '1990-01-01', 'Other', 'benjamin@example.com', 1, 0),
('samantha', 'password123', N'Samantha', N'Unknown', '1990-01-01', 'Other', 'samantha@example.com', 1, 0),
('samuel', 'password123', N'Samuel', N'Unknown', '1990-01-01', 'Other', 'samuel@example.com', 1, 0),
('christine', 'password123', N'Christine', N'Unknown', '1990-01-01', 'Other', 'christine@example.com', 1, 0),
('gregory', 'password123', N'Gregory', N'Unknown', '1990-01-01', 'Other', 'gregory@example.com', 1, 0),
('helen', 'password123', N'Helen', N'Unknown', '1990-01-01', 'Other', 'helen@example.com', 1, 0),
('alexander', 'password123', N'Alexander', N'Unknown', '1990-01-01', 'Other', 'alexander@example.com', 1, 0),
('diane', 'password123', N'Diane', N'Unknown', '1990-01-01', 'Other', 'diane@example.com', 1, 0),
('patrick', 'password123', N'Patrick', N'Unknown', '1990-01-01', 'Other', 'patrick@example.com', 1, 0),
('ruth', 'password123', N'Ruth', N'Unknown', '1990-01-01', 'Other', 'ruth@example.com', 1, 0),
('frank', 'password123', N'Frank', N'Unknown', '1990-01-01', 'Other', 'frank@example.com', 1, 0),
('julie', 'password123', N'Julie', N'Unknown', '1990-01-01', 'Other', 'julie@example.com', 1, 0),
('raymond', 'password123', N'Raymond', N'Unknown', '1990-01-01', 'Other', 'raymond@example.com', 1, 0),
('olivia2', 'password123', N'Olivia', N'Unknown', '1990-01-01', 'Other', 'olivia2@example.com', 1, 0),
('alexander2', 'password123', N'Alexander', N'Unknown', '1990-01-01', 'Other', 'alexander2@example.com', 1, 0),
('joyce', 'password123', N'Joyce', N'Unknown', '1990-01-01', 'Other', 'joyce@example.com', 1, 0),
('jack', 'password123', N'Jack', N'Unknown', '1990-01-01', 'Other', 'jack@example.com', 1, 0),
('virginia', 'password123', N'Virginia', N'Unknown', '1990-01-01', 'Other', 'virginia@example.com', 1, 0),
('dennis', 'password123', N'Dennis', N'Unknown', '1990-01-01', 'Other', 'dennis@example.com', 1, 0),
('victoria', 'password123', N'Victoria', N'Unknown', '1990-01-01', 'Other', 'victoria@example.com', 1, 0);
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
