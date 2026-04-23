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
public class Notification {
        @Id private Long notification_id;
        private String title, content, type;
        private Boolean is_read;
        private LocalDateTime created_at;
        @ManyToOne @JoinColumn(name = "user_id")
        private User user;
}
