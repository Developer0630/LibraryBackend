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
@Table(name = "student")
public class Student {
    @Id
    @Column(name = "student_id")
    Long id;

    @Column(name = "student_code", unique = true)
    String studentCode;
    @Column(name = "major")
    String major;

    @Column(name = "class", nullable = false)
    String clazz;

    @Column(name = "dob")
    LocalDate dob;

    @Column(name = "status")
    String status;

    @Column(name = "total_debt")
    Double totalDebt = 0.0;

    @OneToOne
    @MapsId
    @JoinColumn(name = "student_id")
    User user;
}