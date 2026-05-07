package com.library.library_manager.dto.student;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentProfileResponseDTO {
    String fullName;
    String studentCode;
    String email;
    String phoneNumber;
    String clazz;
    String major;
    String status;
}