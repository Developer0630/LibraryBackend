package com.library.library_manager.dto.student;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentResponseDTO {
    Long id;
    String studentCode;
    String fullName;
    String email;
    String phoneNumber;
    LocalDate dob;
    String major;
    String clazz;
    String status;
    Double balance;
}