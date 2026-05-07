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
public class HistoryResponse {
    String bookTitle;
    LocalDateTime borrowDate;
    LocalDateTime returnDate;
    String status; // Trạng thái khi trả
    String note;
}