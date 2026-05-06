-- 2. Chèn dữ liệu mẫu (Sửa lỗi Unknown column 'id' bằng cách dùng user_id)
INSERT INTO role (role_name) VALUES ('ADMIN'), ('STAFF'), ('STUDENT');

INSERT INTO user (user_name, password, full_name, email, phone_number)
VALUES ('SV001', '123456', 'Nguyen Tien Luong', 'luong.student@example.com', '0912345678');

-- Gán Role (Dùng user_id và id của bảng role)
INSERT INTO user_role (user_id, role_id)
VALUES (
           (SELECT user_id FROM user WHERE user_name = 'SV001'),
           (SELECT id FROM role WHERE role_name = 'STUDENT')
       );

-- Chèn Profile Student
INSERT INTO student (student_id, student_code, major, class, dob, status, total_debt)
VALUES (
           (SELECT user_id FROM user WHERE user_name = 'SV001'),
           'SV001',
           'Software Engineering',
           'SE1601',
           '2002-05-20',
           'ACTIVE',
           0.0
       );