package com.library.library_manager.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "staff")
public class Staff {
    @Id
    @Column(name = "staff_id")
    Long staffId;

    // Đã chuyển từ String sang đối tượng Position
    @ManyToOne
    @JoinColumn(name = "position_id")
    Position position;

    @OneToOne
    @MapsId
    @JoinColumn(name = "staff_id")
    User user;

    @OneToMany(mappedBy = "staff")
    List<Incident> assignedIncidents;
}