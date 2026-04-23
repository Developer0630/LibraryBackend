package com.library.library_manager.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Violation {
        @Id private Long violation_id;
        private String type;
        private Double fine_amount;
        private Boolean is_paid;
        @ManyToOne @JoinColumn(name = "loan_id")
        private Loan loan;
}