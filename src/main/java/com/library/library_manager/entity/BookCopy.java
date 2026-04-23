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
public class BookCopy {
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String barcode, status;
        private LocalDateTime entry_date;
        @ManyToOne @JoinColumn(name = "book_id")
        private Book book;
        @ManyToOne @JoinColumn(name = "shelf_id")
        private Shelf shelf;
}
