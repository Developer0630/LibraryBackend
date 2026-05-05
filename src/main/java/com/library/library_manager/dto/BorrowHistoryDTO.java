package com.library.library_manager.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BorrowHistoryDTO {
    Long loanId;
    String bookTitle;
    String barcode;
    LocalDate borrowDate;
    LocalDate dueDate;
    LocalDate returnDate;
    String status; // e.g., "BORROWED", "RETURNED"
}