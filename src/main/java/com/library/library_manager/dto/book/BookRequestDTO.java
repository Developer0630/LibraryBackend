package com.library.library_manager.dto.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookRequestDTO {

    @NotBlank(message = "Title cannot be blank")
    String title;

    @NotBlank(message = "Author cannot be blank")
    String author;

    // Entity mới chưa có ISBN nhưng Service đang check trùng ISBN,
    // bạn nên giữ trường này hoặc thêm vào Entity Book sau.
    @NotBlank(message = "ISBN cannot be blank")
    String isbn;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be greater than or equal to 0")
    Double price; // THÊM TRƯỜNG NÀY ĐỂ HẾT LỖI getPrice()

    String genre;

    String description;

    @NotNull(message = "Publish year is required")
    Integer publishYear;

    String shelfLocation;

    @Min(value = 1, message = "Quantity must be at least 1")
    int quantity;
}