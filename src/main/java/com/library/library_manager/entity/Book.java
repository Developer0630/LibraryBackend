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

        @Column(name = "price")
        Double price;

        @Column(name = "description", columnDefinition = "TEXT")
        String description;

        // Quan hệ n-n với Category
        @ManyToMany
        @JoinTable(
                name = "book_category",
                joinColumns = @JoinColumn(name = "book_id"),
                inverseJoinColumns = @JoinColumn(name = "category_id")
        )
        Set<Category> categories = new HashSet<>();

        // Quan hệ n-n với Shelves
        @ManyToMany
        @JoinTable(
                name = "book-shelf",
                joinColumns = @JoinColumn(name = "book_id"),
                inverseJoinColumns = @JoinColumn(name = "shelf_id")
        )
        Set<Shelf> shelves = new HashSet<>();

        @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
        @Builder.Default
        List<BookCopy> bookCopies = new ArrayList<>();
}
