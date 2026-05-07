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
@Data
@Entity
@Table(name = "fine_payment")
public class FinePayment {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "payment_id")
        Long id;

        @Column(name = "amount_paid")
        Double amountPaid;

        @Column(name = "payment_date")
        LocalDateTime paymentDate;

        @Column(name = "payment_method")
        String paymentMethod;

        @ManyToOne
        @JoinColumn(name = "student_id", nullable = false)
        Student student;

        @ManyToOne
        @JoinColumn(name = "violation_id")
        Violation violation;
}
