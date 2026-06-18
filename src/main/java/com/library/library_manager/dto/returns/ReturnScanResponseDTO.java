package com.library.library_manager.dto.returns;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReturnScanResponseDTO {
    Long loanId;
    Long studentId;
    String studentName;
    String bookTitle;
    String barcode;
    LocalDateTime borrowDate;
    LocalDateTime dueDate;
    long overdueDays;
    double overdueFine; // Phí trễ hạn dự kiến
    String status;      // Trạng thái phiếu mượn hiện tại (Active)
}