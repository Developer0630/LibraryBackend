package com.library.library_manager.controller;

import com.library.library_manager.dto.loan.EligibilityResponseDTO;
import com.library.library_manager.dto.loan.LoanRequestDTO;
import com.library.library_manager.dto.loan.LoanResponseDTO;
import com.library.library_manager.dto.student.StudentSummaryResponseDTO;
import com.library.library_manager.service.ILoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final ILoanService loanService;

    // 1. Kiểm tra điều kiện mượn tự do: POST /api/loans/check-eligibility
    @PostMapping("/check-eligibility")
    public ResponseEntity<EligibilityResponseDTO> checkEligibility(
            @RequestParam Long studentId, 
            @RequestParam Long copyId) {
        return ResponseEntity.ok(loanService.checkLoanEligibility(studentId, copyId));
    }

    // 2. Tạo mới phiếu mượn tự do: POST /api/loans
    @PostMapping
    public ResponseEntity<LoanResponseDTO> createLoan(@RequestBody LoanRequestDTO request) {
        return ResponseEntity.ok(loanService.createNewLoan(request));
    }

    // 3. Thủ thư xác nhận sinh viên đến nhận sách đặt trước: POST /api/reservations/{reservationId}/confirm-pickup
    // Đặt endpoint tại đây để Thủ thư gọi chuyển tiếp luồng xử lý
    @PostMapping("/from-reservation/{reservationId}")
    public ResponseEntity<LoanResponseDTO> createLoanFromReservation(@PathVariable Long reservationId) {
        return ResponseEntity.ok(loanService.createLoanFromReservation(reservationId));
    }

    // 4. Xác nhận đã giao sách tận tay sinh viên (Mượn tự do): POST /api/loans/{loanId}/confirm-pickup
    @PostMapping("/{loanId}/confirm-pickup")
    public ResponseEntity<LoanResponseDTO> confirmLoanPickup(@PathVariable Long loanId) {
        return ResponseEntity.ok(loanService.confirmLoanPickup(loanId));
    }

    // 5. Lấy danh sách phiếu mượn (Lọc theo sinh viên hoặc trạng thái): GET /api/loans
    @GetMapping
    public ResponseEntity<List<LoanResponseDTO>> getAllLoans(
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(loanService.getAllLoans(studentId, status));
    }

    // 6. Xem chi tiết phiếu mượn: GET /api/loans/{loanId}
    @GetMapping("/{loanId}")
    public ResponseEntity<LoanResponseDTO> getLoanDetail(@PathVariable Long loanId) {
        return ResponseEntity.ok(loanService.getById(loanId));
    }

    // 7. Hủy/Gỡ phiếu mượn trước khi giao: DELETE /api/loans/{loanId}
    @DeleteMapping("/{loanId}")
    public ResponseEntity<String> cancelLoanBeforePickup(@PathVariable Long loanId) {
        loanService.cancelLoanBeforePickup(loanId);
        return ResponseEntity.ok("Đã hủy phiếu mượn thành công. Bản in sách đã được khôi phục về trạng thái sẵn sàng.");
    }

    // 8. Đánh dấu mất/hư hỏng trong quá trình mượn: PATCH /api/loans/{loanId}/mark-issue
    @PatchMapping("/{loanId}/mark-issue")
    public ResponseEntity<String> markBookIssue(
            @PathVariable Long loanId, 
            @RequestParam String issueType) { // Gửi chuỗi: "Lost" hoặc "Damaged"
        loanService.markLoanIssue(loanId, issueType);
        return ResponseEntity.ok("Cập nhật sự cố sách thành công. Hệ thống đã khóa bản in ở trạng thái: " + issueType);
    }
    @GetMapping("/students/{studentId}/borrow-session")
    public ResponseEntity<StudentSummaryResponseDTO> getStudentBorrowSession(@PathVariable Long studentId) {
        return ResponseEntity.ok(loanService.getStudentBorrowSession(studentId));
    }
}