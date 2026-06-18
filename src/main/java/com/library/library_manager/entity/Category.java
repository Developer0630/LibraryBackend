package com.library.library_manager.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "category")
@Data
public class Category {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "category_id")
        Long id;

        @Column(name = "category_name", nullable = false, unique = true, length = 100)
        String categoryName;

        @Column(name = "description", columnDefinition = "TEXT")
        String description;

        @Builder.Default
        @ManyToMany(mappedBy = "categories")
        Set<Book> books = new HashSet<>();
}
