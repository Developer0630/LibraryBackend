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
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class FinePayment {
        @Id private Long payment_id;
        private Double amount_paid;
        private LocalDateTime payment_date;
        private String payment_method;
        @ManyToOne @JoinColumn(name = "violation_id")
        private Violation violation;
        @ManyToOne @JoinColumn(name = "processed_by")
        private Staff staff;
}
