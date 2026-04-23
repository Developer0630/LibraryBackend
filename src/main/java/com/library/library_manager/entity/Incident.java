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
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Incident {
        @Id private Long incident_id;
        private String title, description, priority, status;
        private LocalDateTime created_at;
        @ManyToOne @JoinColumn(name = "reported_by")
        private User user;
        @ManyToOne @JoinColumn(name = "processed_by")
        private Staff staff;
}
