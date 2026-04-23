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
public class Loan {
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long loan_id;
        private LocalDateTime borrow_date, due_date, returned_at;
        private String status;
        @ManyToOne @JoinColumn(name = "student_id")
        private Student student;
        @ManyToOne @JoinColumn(name = "copy_id")
        private BookCopy bookCopy;
}
