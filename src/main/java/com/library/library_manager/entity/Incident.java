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
@Table(name = "incident")
public class Incident {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "incident_id")
        Long id;

        @Column(name = "title", nullable = false, length = 200)
        String title;

        @Column(name = "description", columnDefinition = "TEXT")
        String description;

        @Column(name = "priority", length = 20)
        String priority; // Ví dụ: Low, Medium, High, Critical

        @Column(name = "status", length = 50)
        String status; // Ví dụ: Pending, In Progress, Resolved, Closed

        @Builder.Default
        @Column(name = "created_at")
        LocalDateTime createdAt = LocalDateTime.now();

        // Người gửi sự cố (User)
        @ManyToOne
        @JoinColumn(name = "user_id", nullable = false)
        User user;

        // Người xử lý sự cố (Staff)
        @ManyToOne
        @JoinColumn(name = "staff_id") // Có thể null lúc mới tạo vì chưa có ai nhận xử lý
                Staff staff;
}
