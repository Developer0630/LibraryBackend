package com.library.library_manager.dto.staff;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE) // Tự động thêm private cho các field
public class StaffResponseDTO {
    Long staffId;
    String fullName;
    String username;
    String email;
    String phoneNumber;
    String positionName;
}