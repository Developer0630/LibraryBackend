-- 1. Thiết lập Vai trò & Người dùng
CREATE TABLE roles (
                       role_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                       role_name VARCHAR(50) UNIQUE NOT NULL,
                       description TEXT
);

CREATE TABLE users (
                       user_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) UNIQUE NOT NULL,
                       password_hash TEXT NOT NULL,
                       full_name VARCHAR(100) NOT NULL,
                       email VARCHAR(100) UNIQUE NOT NULL,
                       role_id INT UNSIGNED,
                       student_code VARCHAR(20) UNIQUE,
                       status VARCHAR(20) DEFAULT 'Active', --
                       current_debt DECIMAL(15, 2) DEFAULT 0, --
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE SET NULL
);

-- 2. Quản lý Kho sách
CREATE TABLE books (
                       book_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       author VARCHAR(255),
                       isbn VARCHAR(20) UNIQUE,
                       category VARCHAR(100),
                       description TEXT,
                       shelf_location VARCHAR(100), --
                       price DECIMAL(15, 2) NOT NULL, -- Dùng làm căn cứ tính phí hư hỏng
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE book_copies (
                             copy_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                             book_id INT UNSIGNED NOT NULL,
                             barcode VARCHAR(50) UNIQUE NOT NULL,
                             status VARCHAR(50) DEFAULT 'Available', --
                             condition_status VARCHAR(100) DEFAULT 'Good',
                             CONSTRAINT fk_copy_book FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE
);

-- 3. Quản lý Đặt trước & Mượn trả
CREATE TABLE reservations (
                              reservation_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                              student_id INT UNSIGNED NOT NULL,
                              book_id INT UNSIGNED NOT NULL,
                              copy_id INT UNSIGNED,
                              reserved_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              expires_at TIMESTAMP NOT NULL, -- 3 ngày làm việc, không tính T7-CN
                              status VARCHAR(50) DEFAULT 'Pending', -- Pending, Ready, Expired, Cancelled
                              CONSTRAINT fk_res_student FOREIGN KEY (student_id) REFERENCES users(user_id),
                              CONSTRAINT fk_res_book FOREIGN KEY (book_id) REFERENCES books(book_id),
                              CONSTRAINT fk_res_copy FOREIGN KEY (copy_id) REFERENCES book_copies(copy_id)
);

CREATE TABLE loans (
                       loan_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                       student_id INT UNSIGNED NOT NULL,
                       copy_id INT UNSIGNED NOT NULL,
                       staff_id INT UNSIGNED,
                       borrowed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       due_date TIMESTAMP NOT NULL, -- Hạn trả 120 ngày
                       returned_at TIMESTAMP NULL,
                       status VARCHAR(50) DEFAULT 'Active', -- Active, Returned, Lost
                       CONSTRAINT fk_loan_student FOREIGN KEY (student_id) REFERENCES users(user_id),
                       CONSTRAINT fk_loan_copy FOREIGN KEY (copy_id) REFERENCES book_copies(copy_id)
);

-- 4. Quản lý Vi phạm & Phí phạt
CREATE TABLE fines (
                       fine_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                       student_id INT UNSIGNED NOT NULL,
                       loan_id INT UNSIGNED,
                       amount DECIMAL(15, 2) NOT NULL, -- 5.000 VNĐ/ngày hoặc theo mức độ hư hỏng
                       reason_type VARCHAR(100), -- Trễ hạn, Hư hỏng nhẹ/TB/Nặng, Mất sách
                       notes TEXT,
                       status VARCHAR(50) DEFAULT 'Unpaid', -- Unpaid, Paid
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       CONSTRAINT fk_fine_student FOREIGN KEY (student_id) REFERENCES users(user_id),
                       CONSTRAINT fk_fine_loan FOREIGN KEY (loan_id) REFERENCES loans(loan_id)
);

CREATE TABLE fine_payments (
                               payment_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                               fine_id INT UNSIGNED NOT NULL,
                               staff_id INT UNSIGNED,
                               amount_paid DECIMAL(15, 2) NOT NULL,
                               payment_method VARCHAR(50), -- Tiền mặt, chuyển khoản
                               paid_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               CONSTRAINT fk_payment_fine FOREIGN KEY (fine_id) REFERENCES fines(fine_id)
);

-- 5. Đánh giá & Thông báo (Phục vụ sinh viên)
CREATE TABLE book_reviews (
                              review_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                              book_id INT UNSIGNED NOT NULL,
                              student_id INT UNSIGNED NOT NULL,
                              rating INT CHECK (rating >= 1 AND rating <= 5), --
                              comment TEXT,
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              CONSTRAINT fk_rev_book FOREIGN KEY (book_id) REFERENCES books(book_id),
                              CONSTRAINT fk_rev_student FOREIGN KEY (student_id) REFERENCES users(user_id)
);