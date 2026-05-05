package com.library.library_manager.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "violation")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Violation {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "violation_id")
        Long id;

        @Column(name = "type")
        String type; // Ví dụ: Late Return, Damaged Book

        @Column(name = "fine_amount")
        Double fineAmount = 0.0;

        @Column(name = "is_paid")
        Boolean isPaid = false;

        @ManyToOne
        @JoinColumn(name = "loan_id")
        Loan loan;
}