package com.library.library_manager.service.impl;

import com.library.library_manager.dto.returns.ReturnRequestDTO;
import com.library.library_manager.dto.returns.ReturnResponseDTO;
import com.library.library_manager.dto.returns.ReturnScanResponseDTO;
import com.library.library_manager.entity.*;
import com.library.library_manager.repository.*;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReturnTransactionService {

    IReturnTransactionRepository returnRepository;
    ILoanRepository loanRepository;
    IBookCopyRepository bookCopyRepository;
    IStudentRepository studentRepository;

    // Cấu hình tiền phạt cứng định danh tại Server
    private static final double FINE_PER_DAY = 5000.0;
    private static final double FINE_LIGHT = 20000.0;
    private static final double FINE_MEDIUM = 50000.0;
    private static final double FINE_HEAVY = 100000.0;
    private static final double FINE_LOST = 200000.0;

    public ReturnScanResponseDTO scanBookForReturn(Long copyId) {
        Loan loan = loanRepository.findAll().stream()
                .filter(l -> l.getBookCopy().getId().equals(copyId) && "Active".equalsIgnoreCase(l.getStatus()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu mượn 'Active' cho sách này."));

        long overdueDays = 0;
        double overdueFine = 0.0;
        if (LocalDateTime.now().isAfter(loan.getDueDate())) {
            overdueDays = Duration.between(loan.getDueDate(), LocalDateTime.now()).toDays();
            if (overdueDays == 0) overdueDays = 1;
            overdueFine = overdueDays * FINE_PER_DAY;
        }

        return ReturnScanResponseDTO.builder()
                .loanId(loan.getId())
                .studentId(loan.getUser().getUserId())
                .studentName(loan.getUser().getFullName())
                .bookTitle(loan.getBookCopy().getBook().getTitle())
                .barcode(loan.getBookCopy().getBarcode())
                .borrowDate(loan.getBorrowDate())
                .dueDate(loan.getDueDate())
                .overdueDays(overdueDays)
                .overdueFine(overdueFine)
                .status(loan.getStatus())
                .build();
    }

    @Transactional
    public ReturnResponseDTO processReturn(ReturnRequestDTO request) {
        Loan loan = loanRepository.findById(request.getLoanId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu mượn."));

        if (!"Active".equalsIgnoreCase(loan.getStatus())) {
            throw new RuntimeException("Phiếu mượn không ở trạng thái hoạt động.");
        }

        LocalDateTime now = LocalDateTime.now();
        double lateFee = 0.0;

        // 1. Tính toán tiền phạt trễ hạn
        if (now.isAfter(loan.getDueDate())) {
            long overdueDays = Duration.between(loan.getDueDate(), now).toDays();
            if (overdueDays == 0) overdueDays = 1;
            lateFee = overdueDays * FINE_PER_DAY;
        }

        // 2. Tính toán tiền phạt vật lý dựa theo trạng thái hư hại
        String condition = request.getConditionStatus().toUpperCase();
        String nextBookStatus = "AVAILABLE";
        double damageFee = 0.0;

        switch (condition) {
            case "DAMAGED_LIGHT":
                damageFee = FINE_LIGHT;
                nextBookStatus = "DAMAGED";
                break;
            case "DAMAGED_MEDIUM":
                damageFee = FINE_MEDIUM;
                nextBookStatus = "DAMAGED";
                break;
            case "DAMAGED_HEAVY":
                damageFee = FINE_HEAVY;
                nextBookStatus = "REPAIRING";
                break;
            case "LOST":
                damageFee = FINE_LOST;
                nextBookStatus = "LOST";
                break;
            default:
                // Nếu Android có truyền damageFee tùy biến lên thì ưu tiên dùng, không thì bằng 0.0
                damageFee = request.getDamageFee() != null ? request.getDamageFee() : 0.0;
                break;
        }

        // 3. Cập nhật bảng Loan & BookCopy
        loan.setStatus("Returned");
        loan.setReturnedAt(now);
        loan.setReturnStatus(condition);
        loanRepository.save(loan);

        BookCopy copy = loan.getBookCopy();
        copy.setStatus(nextBookStatus);
        bookCopyRepository.save(copy);

        // 4. Khởi tạo và Lưu lịch sử giao dịch vào bảng return_transaction của bro
        ReturnTransaction transaction = ReturnTransaction.builder()
                .loan(loan)
                .returnDate(now)
                .actualCondition(condition + " - " + (request.getStaffNote() != null ? request.getStaffNote() : ""))
                .lateFee(lateFee + damageFee) // Tổng chi phí phạt lưu trữ vào trường late_fee
                .build();
        returnRepository.save(transaction);

        // 5. Tích lũy số dư nợ phạt vào tài khoản sinh viên
        double totalFine = lateFee + damageFee;
        if (totalFine > 0) {
            Student student = studentRepository.findAll().stream()
                    .filter(s -> s.getUser().getUserName().equalsIgnoreCase(loan.getUser().getUserName()))
                    .findFirst().orElse(null);
            if (student != null) {
                double currentDebt = student.getTotalDebt() != null ? student.getTotalDebt() : 0.0;
                student.setTotalDebt(currentDebt + totalFine);
                if (student.getTotalDebt() > 50000) {
                    student.setStatus("LOCKED");
                }
                studentRepository.save(student);
            }
        }

        return ReturnResponseDTO.builder()
                .returnId(transaction.getId())
                .loanId(loan.getId())
                .studentName(loan.getUser().getFullName())
                .bookTitle(copy.getBook().getTitle())
                .returnDate(now)
                .actualCondition(condition)
                .lateFee(lateFee)
                .damageFee(damageFee)
                .totalFine(totalFine)
                .build();
    }
}