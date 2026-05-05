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
    @Id
    @Column(name = "student_id")
    Long studentId;

    @Column(name = "major")
    String major;

    @Column(name = "class", nullable = false)
    String clazz;

    @Column(name = "total_debt")
    Double totalDebt = 0.0;

    @OneToOne
    @MapsId
    @JoinColumn(name = "student_id")
    User user;
}