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
@Table(name = "book_copy")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookCopy {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "copy_id")
        Long id;

        @Column(name = "barcode", unique = true, nullable = false)
        String barcode;

        @Column(name = "status")
        String status; // Ví dụ: Available, Borrowed, Lost

        @Column(name = "shelf_location")
        String shelfLocation;

        @Column(name = "entry_date")
        LocalDateTime entryDate = LocalDateTime.now();

        @ManyToOne
        @JoinColumn(name = "book_id", nullable = false)
        Book book;
}
