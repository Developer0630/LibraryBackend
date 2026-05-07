ALTER TABLE fine_payment
    ADD COLUMN student_id BIGINT NOT NULL,
    ADD CONSTRAINT fk_payment_student FOREIGN KEY (student_id) REFERENCES student (student_id);