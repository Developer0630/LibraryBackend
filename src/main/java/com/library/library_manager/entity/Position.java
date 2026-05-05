package com.library.library_manager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "position")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "position_id")
    private Long positionId;

    @Column(name = "position_name", length = 100, nullable = false, unique = true)
    private String positionName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "position", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Staff> staffs;
}