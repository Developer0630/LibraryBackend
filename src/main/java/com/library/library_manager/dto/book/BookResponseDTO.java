package com.library.library_manager.dto.book;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookResponseDTO {
    Long bookId;
    String title;
    String author;
    String isbn;
    String genre;
    Double price;
    String description;
    Integer publishYear;
    String shelfLocation;
    Integer totalStock;
    // Trả về danh sách bản in tối giản
    List<BookCopyResponseDTO> copies;
}