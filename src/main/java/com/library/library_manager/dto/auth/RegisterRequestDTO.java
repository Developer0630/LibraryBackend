package com.library.library_manager.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRequestDTO {
    @NotBlank(message = "USERNAME_REQUIRED")
    String username;

    @NotBlank(message = "PASSWORD_REQUIRED")
    String password;

    @NotBlank(message = "FULL_NAME_REQUIRED")
    String fullName;

    @Email(message = "INVALID_EMAIL")
    @NotBlank(message = "EMAIL_REQUIRED")
    String email;

    String phoneNumber;
    
    // Client truyền lên: "STUDENT" hoặc "STAFF" để hệ thống biết đường gán quyền
    @NotBlank(message = "ROLE_REQUIRED")
    String role; 
}