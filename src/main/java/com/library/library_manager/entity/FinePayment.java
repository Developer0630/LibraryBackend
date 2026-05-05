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
@Table(name = "fine_payment")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FinePayment {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "payment_id")
        Long id;

        @Column(name = "amount_paid", nullable = false)
        Double amountPaid;

        @Column(name = "payment_date")
        LocalDateTime paymentDate = LocalDateTime.now();

        @Column(name = "payment_method")
        String paymentMethod; // Cash, Banking...

        @ManyToOne
        @JoinColumn(name = "violation_id")
        Violation violation;
}
