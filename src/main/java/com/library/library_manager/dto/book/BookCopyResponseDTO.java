package com.library.library_manager.dto.book;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookCopyResponseDTO {
    Long id;
    String barcode;
    String status; // AVAILABLE, BORROWED, DAMAGED, LOST
    String shelfLocation;
    String bookTitle; // Trả về tiêu đề sách để tiện hiển thị
}