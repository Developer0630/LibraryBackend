package com.library.library_manager.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ViolationDTO {
    Long id;
    String violationType;
    String description;
    Double fineAmount;
    LocalDate createdAt;
    String paymentStatus;
}