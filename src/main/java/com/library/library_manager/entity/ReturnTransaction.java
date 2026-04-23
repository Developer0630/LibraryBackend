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
public class ReturnTransaction {
        @Id private Long return_id;
        private LocalDateTime return_date;
        private String actual_condition;
        private Double late_fee;
        @OneToOne @JoinColumn(name = "loan_id")
        private Loan loan;
        @ManyToOne @JoinColumn(name = "processed_by")
        private Staff staff;
}
