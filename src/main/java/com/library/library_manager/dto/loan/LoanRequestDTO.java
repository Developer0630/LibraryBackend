package com.library.library_manager.dto.loan;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoanRequestDTO {
    Long studentId;      // ID sinh viên mượn tự do
    Long copyId;         // ID bản in sách
    Long reservationId;  // ID phiếu đặt trước (nếu mượn từ luồng đặt trước)
    LocalDateTime dueDate;
    String note;
}