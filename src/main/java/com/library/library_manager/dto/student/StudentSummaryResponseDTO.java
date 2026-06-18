package com.library.library_manager.dto.student;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentSummaryResponseDTO {
    Long id;
    String studentCode;
    String fullName;
    long pendingReservations; // Số đơn đặt trước chờ nhận
    long activeLoans;         // Số sách đang cầm về nhà chưa trả
    Double totalDebt;         // Tổng nợ phạt hiện tại
    long remainingLimit;      // Hạn mức mượn còn lại (Tối đa 5)
    String status;            // Trạng thái thẻ sinh viên (ACTIVE/LOCKED)
}