package com.library.library_manager.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "book_review")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    Long id;

    @Column(name = "rating", nullable = false)
    Integer rating; // Ví dụ: 1 đến 5 sao

    @Column(name = "comment", columnDefinition = "TEXT")
    String comment;

    @Column(name = "created_at")
    LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    Book book;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    Student student;
}
