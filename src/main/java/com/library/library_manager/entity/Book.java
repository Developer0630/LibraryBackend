package com.library.library_manager.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "book")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Book {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "book_id")
        Long id;

        @Column(name = "title", nullable = false, length = 255)
        String title;

        @Column(name = "author", length = 150)
        String author;

        @Column(name = "publisher", length = 255) // Mới thêm
        String publisher;

        @Column(name = "isbn", length = 50)      // Mới thêm
        String isbn;

        @Column(name = "price")
        Double price;

        @Column(name = "description", columnDefinition = "TEXT")
        String description;

        @Column(name = "status", length = 50)    // Mới thêm
        String status;

        // Quan hệ n-n với Category
        @ManyToMany
        @JoinTable(
                name = "book_category",
                joinColumns = @JoinColumn(name = "book_id"),
                inverseJoinColumns = @JoinColumn(name = "category_id")
        )
        @Builder.Default
        Set<Category> categories = new HashSet<>();

        // Quan hệ n-n với Shelves
        @ManyToMany
        @JoinTable(
                name = "book_shelf",
                joinColumns = @JoinColumn(name = "book_id"),
                inverseJoinColumns = @JoinColumn(name = "shelf_id")
        )
        @Builder.Default
        Set<Shelf> shelves = new HashSet<>();

        @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
        @Builder.Default
        List<BookCopy> bookCopies = new ArrayList<>();
}