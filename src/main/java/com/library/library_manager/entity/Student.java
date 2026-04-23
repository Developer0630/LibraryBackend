package com.library.library_manager.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity

public class Student {
    @Id private String student_id;
    private String major, clazz;
    private Double total_debt;
    @OneToOne @JoinColumn(name = "user_id")
    private User user;
}