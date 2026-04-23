package com.library.library_manager.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Book {

        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String title, author, description;
        private Double price;
        @ManyToOne @JoinColumn(name = "category_id")
        private Category category;
}
