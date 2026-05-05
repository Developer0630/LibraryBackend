package com.library.library_manager.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "permission_name",unique = true, nullable = false)
    String name;

    @Column(name = "module", unique = true, nullable = false)
    String module;

    @Column(name = "description")
    String description;

    @ManyToMany(mappedBy = "permissions")
    Set<Role> roles = new HashSet<>();
}
