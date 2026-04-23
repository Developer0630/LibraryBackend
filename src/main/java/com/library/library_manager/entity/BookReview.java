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
public class BookReview {
        @Id private Long review_id;
        private Integer rating;
        private String comment;
        private LocalDateTime created_at;
        @ManyToOne @JoinColumn(name = "book_id")
        private Book book;
        @ManyToOne @JoinColumn(name = "student_id")
        private Student student;
}
