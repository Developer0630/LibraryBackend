package com.library.library_manager.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "return_transaction")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReturnTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "return_id")
    Long id;

    @Builder.Default
    @Column(name = "return_date", nullable = false)
    LocalDateTime returnDate = LocalDateTime.now();

    @Column(name = "actual_condition")
    String actualCondition; // Tình trạng sách khi trả

    @Builder.Default
    @Column(name = "late_fee")
    Double lateFee = 0.0;

    @OneToOne
    @JoinColumn(name = "loan_id", nullable = false)
    Loan loan;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    Staff staff;
}
