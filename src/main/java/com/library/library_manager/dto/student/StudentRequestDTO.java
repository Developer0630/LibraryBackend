package com.library.library_manager.dto.student;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentRequestDTO {
    @NotBlank(message = "STUDENT_CODE_REQUIRED")
    String studentCode; // Mã số sinh viên

    @NotBlank(message = "FULL_NAME_REQUIRED")
    String fullName; // Họ tên đầy đủ (lưu vào bảng User)

    @Email(message = "INVALID_EMAIL")
    @NotBlank(message = "EMAIL_REQUIRED")
    String email;

    String phoneNumber;
    String major; // Chuyên ngành (lưu vào bảng Student)
    String clazz; // Lớp (lưu vào bảng Student)
}