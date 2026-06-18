package com.library.library_manager.dto.loan;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoanResponseDTO {
    Long loanId;
    String studentName;
    String studentCode;
    String bookTitle;
    String barcode; // Số định danh bản in
    LocalDateTime borrowDate;
    LocalDateTime dueDate;
    LocalDateTime returnedAt;
    String status;
    String returnStatus;
    String staffNote;
}