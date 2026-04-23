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
public class Reservation {
        @Id private Long reservation_id;
        private LocalDateTime request_date, expiration_date;
        private String status;
        @ManyToOne @JoinColumn(name = "student_id")
        private Student student;
        @ManyToOne @JoinColumn(name = "book_id")
        private Book book;
}
