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
@Table(name = "loan")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Loan {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "loan_id")
        Long id;

        @Column(name = "borrow_date", nullable = false)
        LocalDateTime borrowDate = LocalDateTime.now();

        @Column(name = "due_date", nullable = false)
        LocalDateTime dueDate;

        @Column(name = "returned_at")
        LocalDateTime returnedAt;

        @Column(name = "status")
        String status; // Ví dụ: Active, Returned, Overdue

        @ManyToOne
        @JoinColumn(name = "user_id", nullable = false)
        User user;

        @ManyToOne
        @JoinColumn(name = "copy_id", nullable = false)
        BookCopy bookCopy;

        @ManyToOne
        @JoinColumn(name = "staff_id")
        Staff staff;
}
