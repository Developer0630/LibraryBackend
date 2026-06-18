package com.library.library_manager.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReservationResponse {
    private Long id;
    private String bookTitle;
    private String createdAt;
    private String status; // Đang giữ, Hết hạn, Bị hủy
}
