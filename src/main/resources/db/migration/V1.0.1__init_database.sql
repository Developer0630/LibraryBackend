-- 1. NHÓM BẢNG ĐỘC LẬP (Không có khóa ngoại)
CREATE TABLE user (
                      user_id BIGINT NOT NULL AUTO_INCREMENT,
                      user_name VARCHAR(50) NOT NULL UNIQUE,
                      email VARCHAR(255) NOT NULL UNIQUE,
                      full_name VARCHAR(255) NOT NULL,
                      password VARCHAR(255) NOT NULL,
                      phone_number VARCHAR(20), -- THÊM DÒNG NÀY
                      PRIMARY KEY (user_id)
) ENGINE=InnoDB;

CREATE TABLE role (
                      id BIGINT NOT NULL AUTO_INCREMENT,
                      role_name VARCHAR(255) NOT NULL UNIQUE,
                      PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE permission (
                            id BIGINT NOT NULL AUTO_INCREMENT,
                            module VARCHAR(255) NOT NULL UNIQUE,
                            permission_name VARCHAR(255) NOT NULL UNIQUE,
                            description VARCHAR(255),
                            PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE book (
                      book_id BIGINT NOT NULL AUTO_INCREMENT,
                      title VARCHAR(255) NOT NULL,
                      author VARCHAR(150),
                      price FLOAT(53),
                      description TEXT,
                      PRIMARY KEY (book_id)
) ENGINE=InnoDB;

CREATE TABLE system_rule (
                             rule_id BIGINT NOT NULL AUTO_INCREMENT,
                             rule_key VARCHAR(100) NOT NULL UNIQUE,
                             rule_value VARCHAR(255) NOT NULL,
                             data_type VARCHAR(20),
                             unit VARCHAR(50),
                             description TEXT,
                             PRIMARY KEY (rule_id)
) ENGINE=InnoDB;

-- 2. NHÓM BẢNG CẤP 2 (Chỉ phụ thuộc vào nhóm 1)
CREATE TABLE student (
                         student_id BIGINT NOT NULL,
                         student_code VARCHAR(50) NOT NULL UNIQUE,
                         major VARCHAR(255),
                         class VARCHAR(255) NOT NULL,
                         dob DATE,
                         status VARCHAR(20) DEFAULT 'ACTIVE',
                         total_debt FLOAT(53) DEFAULT 0.0,
                         PRIMARY KEY (student_id),
                         CONSTRAINT fk_student_user FOREIGN KEY (student_id) REFERENCES user (user_id)
) ENGINE=InnoDB;

CREATE TABLE position (
                          position_id BIGINT NOT NULL AUTO_INCREMENT,
                          position_name VARCHAR(100) NOT NULL UNIQUE,
                          description TEXT,
                          PRIMARY KEY (position_id)
) ENGINE=InnoDB;

CREATE TABLE staff (
                       staff_id BIGINT NOT NULL,
                       position_id BIGINT,
                       PRIMARY KEY (staff_id),
                       CONSTRAINT fk_staff_user FOREIGN KEY (staff_id) REFERENCES user (user_id),
                       CONSTRAINT fk_staff_position FOREIGN KEY (position_id) REFERENCES position (position_id)
) ENGINE=InnoDB;

CREATE TABLE user_role (
                           user_id BIGINT NOT NULL,
                           role_id BIGINT NOT NULL,
                           PRIMARY KEY (role_id, user_id),
                           CONSTRAINT fk_ur_user FOREIGN KEY (user_id) REFERENCES user (user_id),
                           CONSTRAINT fk_ur_role FOREIGN KEY (role_id) REFERENCES role (id)
) ENGINE=InnoDB;

CREATE TABLE role_permission (
                                 role_id BIGINT NOT NULL,
                                 permission_id BIGINT NOT NULL,
                                 PRIMARY KEY (permission_id, role_id),
                                 CONSTRAINT fk_rp_role FOREIGN KEY (role_id) REFERENCES role (id),
                                 CONSTRAINT fk_rp_permission FOREIGN KEY (permission_id) REFERENCES permission (id)
) ENGINE=InnoDB;

CREATE TABLE book_copy (
                           copy_id BIGINT NOT NULL AUTO_INCREMENT,
                           book_id BIGINT NOT NULL,
                           barcode VARCHAR(255) NOT NULL UNIQUE,
                           status VARCHAR(255),
                           shelf_location VARCHAR(255),
                           entry_date DATETIME(6),
                           PRIMARY KEY (copy_id),
                           CONSTRAINT fk_copy_book FOREIGN KEY (book_id) REFERENCES book (book_id)
) ENGINE=InnoDB;

-- 3. NHÓM BẢNG NGHIỆP VỤ (Nối nhiều phía)
CREATE TABLE loan (
                      loan_id BIGINT NOT NULL AUTO_INCREMENT,
                      user_id BIGINT NOT NULL,
                      copy_id BIGINT NOT NULL,
                      staff_id BIGINT,
                      borrow_date DATETIME(6) NOT NULL,
                      due_date DATETIME(6) NOT NULL,
                      returned_at DATETIME(6),
                      status VARCHAR(255),
                      PRIMARY KEY (loan_id),
                      CONSTRAINT fk_loan_user FOREIGN KEY (user_id) REFERENCES user (user_id),
                      CONSTRAINT fk_loan_copy FOREIGN KEY (copy_id) REFERENCES book_copy (copy_id),
                      CONSTRAINT fk_loan_staff FOREIGN KEY (staff_id) REFERENCES staff (staff_id)
) ENGINE=InnoDB;

CREATE TABLE reservation (
                             reservation_id BIGINT NOT NULL AUTO_INCREMENT,
                             student_id BIGINT NOT NULL,
                             book_id BIGINT NOT NULL,
                             copy_id BIGINT,
                             loan_id BIGINT UNIQUE,
                             request_date DATETIME(6) NOT NULL,
                             expiration_date DATETIME(6),
                             status VARCHAR(50),
                             PRIMARY KEY (reservation_id),
                             CONSTRAINT fk_res_student FOREIGN KEY (student_id) REFERENCES student (student_id),
                             CONSTRAINT fk_res_book FOREIGN KEY (book_id) REFERENCES book (book_id),
                             CONSTRAINT fk_res_copy FOREIGN KEY (copy_id) REFERENCES book_copy (copy_id),
                             CONSTRAINT fk_res_loan FOREIGN KEY (loan_id) REFERENCES loan (loan_id)
) ENGINE=InnoDB;

CREATE TABLE violation (
                           violation_id BIGINT NOT NULL AUTO_INCREMENT,
                           loan_id BIGINT,
                           type VARCHAR(255),
                           fine_amount FLOAT(53),
                           is_paid BIT(1),
                           PRIMARY KEY (violation_id),
                           CONSTRAINT fk_violation_loan FOREIGN KEY (loan_id) REFERENCES loan (loan_id)
) ENGINE=InnoDB;

CREATE TABLE fine_payment (
                              payment_id BIGINT NOT NULL AUTO_INCREMENT,
                              violation_id BIGINT,
                              amount_paid FLOAT(53) NOT NULL,
                              payment_date DATETIME(6),
                              payment_method VARCHAR(255),
                              PRIMARY KEY (payment_id),
                              CONSTRAINT fk_payment_violation FOREIGN KEY (violation_id) REFERENCES violation (violation_id)
) ENGINE=InnoDB;

CREATE TABLE return_transaction (
                                    return_id BIGINT NOT NULL AUTO_INCREMENT,
                                    loan_id BIGINT NOT NULL UNIQUE,
                                    staff_id BIGINT,
                                    return_date DATETIME(6) NOT NULL,
                                    actual_condition VARCHAR(255),
                                    late_fee FLOAT(53),
                                    PRIMARY KEY (return_id),
                                    CONSTRAINT fk_return_loan FOREIGN KEY (loan_id) REFERENCES loan (loan_id),
                                    CONSTRAINT fk_return_staff FOREIGN KEY (staff_id) REFERENCES staff (staff_id)
) ENGINE=InnoDB;

-- 4. CÁC BẢNG PHỤ TRỢ KHÁC
CREATE TABLE category (
                          category_id BIGINT NOT NULL AUTO_INCREMENT,
                          category_name VARCHAR(100) NOT NULL UNIQUE,
                          description TEXT,
                          PRIMARY KEY (category_id)
) ENGINE=InnoDB;

CREATE TABLE book_category (
                               book_id BIGINT NOT NULL,
                               category_id BIGINT NOT NULL,
                               PRIMARY KEY (book_id, category_id),
                               CONSTRAINT fk_bc_book FOREIGN KEY (book_id) REFERENCES book (book_id),
                               CONSTRAINT fk_bc_category FOREIGN KEY (category_id) REFERENCES category (category_id)
) ENGINE=InnoDB;

CREATE TABLE shelf (
                       shelf_id BIGINT NOT NULL AUTO_INCREMENT,
                       book_id BIGINT NOT NULL,
                       shelf_code VARCHAR(50) NOT NULL UNIQUE,
                       area_zone VARCHAR(50),
                       floor_level INTEGER,
                       PRIMARY KEY (shelf_id),
                       CONSTRAINT fk_shelf_book FOREIGN KEY (book_id) REFERENCES book (book_id)
) ENGINE=InnoDB;

CREATE TABLE book_shelf (
                            shelf_id BIGINT NOT NULL AUTO_INCREMENT,
                            book_id BIGINT NOT NULL,
                            shelf_code VARCHAR(50) NOT NULL UNIQUE,
                            area_zone VARCHAR(50),
                            floor_level INTEGER,
                            PRIMARY KEY (shelf_id),
                            CONSTRAINT fk_bookshelf_book_id FOREIGN KEY (book_id) REFERENCES book (book_id)
) ENGINE=InnoDB;

CREATE TABLE book_review (
                             review_id BIGINT NOT NULL AUTO_INCREMENT,
                             book_id BIGINT NOT NULL,
                             student_id BIGINT NOT NULL,
                             rating INTEGER NOT NULL,
                             comment TEXT,
                             created_at DATETIME(6),
                             PRIMARY KEY (review_id),
                             CONSTRAINT fk_review_book FOREIGN KEY (book_id) REFERENCES book (book_id),
                             CONSTRAINT fk_review_student FOREIGN KEY (student_id) REFERENCES student (student_id)
) ENGINE=InnoDB;

CREATE TABLE incident (
                          incident_id BIGINT NOT NULL AUTO_INCREMENT,
                          user_id BIGINT NOT NULL,
                          staff_id BIGINT,
                          title VARCHAR(200) NOT NULL,
                          description TEXT,
                          priority VARCHAR(20),
                          status VARCHAR(50),
                          created_at DATETIME(6),
                          PRIMARY KEY (incident_id),
                          CONSTRAINT fk_incident_user FOREIGN KEY (user_id) REFERENCES user (user_id),
                          CONSTRAINT fk_incident_staff FOREIGN KEY (staff_id) REFERENCES staff (staff_id)
) ENGINE=InnoDB;

CREATE TABLE notification (
                              notification_id BIGINT NOT NULL AUTO_INCREMENT,
                              user_id BIGINT NOT NULL,
                              title VARCHAR(255),
                              content VARCHAR(255),
                              type VARCHAR(255) NOT NULL,
                              is_read BIT(1),
                              created_at DATETIME(6),
                              PRIMARY KEY (notification_id),
                              CONSTRAINT fk_noti_user FOREIGN KEY (user_id) REFERENCES user (user_id)
) ENGINE=InnoDB;