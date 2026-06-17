package com.library.library_manager.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginRequestDTO {
    @NotBlank(message = "USERNAME_REQUIRED")
    String username;

    @NotBlank(message = "PASSWORD_REQUIRED")
    String password;
}