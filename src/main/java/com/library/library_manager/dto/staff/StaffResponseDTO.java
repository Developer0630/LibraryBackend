package com.library.library_manager.dto.staff;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffResponseDTO {
    Long staffId;
    String fullName;
    String email;
    String positionName;
}