package com.library.library_manager.dto.auth;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginResponseDTO {
    Long userId;
    String username;
    String fullName;
    String email;
    Set<String> roles; // Trả về danh sách quyền cho Android check: ["ADMIN"], ["STAFF"], hoặc ["STUDENT"]
    String message;
}