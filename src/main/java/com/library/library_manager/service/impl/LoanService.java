package com.library.library_manager.service.impl;

import com.library.library_manager.dto.loan.EligibilityResponseDTO;
import com.library.library_manager.dto.loan.LoanRequestDTO;
import com.library.library_manager.dto.loan.LoanResponseDTO;
import com.library.library_manager.dto.returns.ReturnRequestDTO;
import com.library.library_manager.dto.returns.ReturnScanResponseDTO;
import com.library.library_manager.dto.student.StudentSummaryResponseDTO;
import com.library.library_manager.entity.*;
import com.library.library_manager.repository.*;
import com.library.library_manager.service.ILoanService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LoanService implements ILoanService {

    ILoanRepository loanRepository;
    IStudentRepository studentRepository;
    IBookCopyRepository bookCopyRepository;
    IReservationRepository reservationRepository;

    @Override
    public EligibilityResponseDTO checkLoanEligibility(Long studentId, Long copyId) {
        Student student = studentRepository.findById(studentId).orElse(null);
        BookCopy copy = bookCopyRepository.findById(copyId).orElse(null);

        if (student == null) return new EligibilityResponseDTO(false, "Sinh viên không tồn tại trên hệ thống.");
        if (copy == null) return new EligibilityResponseDTO(false, "Mã bản sao sách không hợp lệ.");
        
        // Chấp nhận cả trạng thái AVAILABLE và LOANED_PENDING (để phục vụ bước xác nhận tiếp theo không bị chặn)
        String status = copy.getStatus();
        if (!"AVAILABLE".equalsIgnoreCase(status) && !"LOANED_PENDING".equalsIgnoreCase(status)) {
            return new EligibilityResponseDTO(false, "Bản sao sách không sẵn sàng (Đang mượn hoặc lỗi).");
        }
        
        if ("LOCKED".equalsIgnoreCase(student.getStatus())) {
            return new EligibilityResponseDTO(false, "Tài khoản sinh viên đang bị khóa.");
        }
        if (student.getTotalDebt() != null && student.getTotalDebt() > 50000) {
            return new EligibilityResponseDTO(false, "Sinh viên vượt quá hạn mức nợ phạt cho phép (>50k).");
        }

        // Chỉ đếm các đơn thực sự đang cầm trên tay (Active), loại bỏ đơn Pending ảo khỏi bộ lọc điều kiện mượn tự do
        long activeLoans = loanRepository.findAll().stream()
                .filter(l -> l.getUser().getUserName().equalsIgnoreCase(student.getUser().getUserName()))
                .filter(l -> l.getReturnedAt() == null && "Active".equalsIgnoreCase(l.getStatus()))
                .count();

        long activeRes = reservationRepository.countByStudent_User_UserNameAndStatus(student.getUser().getUserName(), "Đang giữ");
        if (activeLoans + activeRes >= 5) {
            return new EligibilityResponseDTO(false, "Sinh viên vượt quá giới hạn mượn + đặt trước (tối đa 5 cuốn).");
        }

        return new EligibilityResponseDTO(true, "Đủ điều kiện mượn sách.");
    }

    @Override
    @Transactional
    public LoanResponseDTO createNewLoan(LoanRequestDTO request) {
        EligibilityResponseDTO eligibility = checkLoanEligibility(request.getStudentId(), request.getCopyId());
        if (!eligibility.isPass()) {
            throw new RuntimeException("Không đạt điều kiện mượn: " + eligibility.getReason());
        }

        Student student = studentRepository.findById(request.getStudentId()).orElseThrow();
        BookCopy copy = bookCopyRepository.findById(request.getCopyId()).orElseThrow();

        copy.setStatus("LOANED_PENDING"); // Chờ sinh viên đến lấy tại quầy
        bookCopyRepository.save(copy);

        Loan loan = Loan.builder()
                .user(student.getUser())
                .bookCopy(copy)
                .borrowDate(LocalDateTime.now())
                .dueDate(request.getDueDate() != null ? request.getDueDate() : LocalDateTime.now().plusDays(14))
                .status("Pending")
                .staffNote(request.getNote())
                .build();

        return mapToResponse(loanRepository.save(loan));
    }

    @Override
    @Transactional
    public LoanResponseDTO createLoanFromReservation(Long reservationId) {
        Reservation res = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn đặt trước."));

        if (!"Đang giữ".equalsIgnoreCase(res.getStatus())) {
            throw new RuntimeException("Phiếu giữ chỗ không hợp lệ hoặc đã xử lý rồi.");
        }

        res.setStatus("Đã nhận sách");
        reservationRepository.save(res);

        BookCopy copy = res.getBookCopy();
        copy.setStatus("LOANED"); // Sách chính thức được mang đi
        bookCopyRepository.save(copy);

        Loan loan = Loan.builder()
                .user(res.getStudent().getUser())
                .bookCopy(copy)
                .borrowDate(LocalDateTime.now())
                .dueDate(LocalDateTime.now().plusDays(14))
                .status("Active")
                .staffNote("Mượn chuyển tiếp từ đơn đặt giữ chỗ ID: " + reservationId)
                .build();

        return mapToResponse(loanRepository.save(loan));
    }

    @Override
    @Transactional
    public LoanResponseDTO confirmLoanPickup(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu mượn cần xác nhận."));

        loan.setStatus("Active");
        loan.setBorrowDate(LocalDateTime.now());
        loan.setDueDate(LocalDateTime.now().plusDays(14));
        loan.getBookCopy().setStatus("LOANED");
        
        bookCopyRepository.save(loan.getBookCopy());
        return mapToResponse(loanRepository.save(loan));
    }

    @Override
    public List<LoanResponseDTO> getAllLoans(Long studentId, String status) {
        return loanRepository.findAll().stream()
                .filter(l -> studentId == null || l.getUser().getUserId().equals(studentId))
                .filter(l -> status == null || l.getStatus().equalsIgnoreCase(status))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public LoanResponseDTO getById(Long loanId) {
        return loanRepository.findById(loanId)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu mượn ID: " + loanId));
    }

    @Override
    @Transactional
    public void cancelLoanBeforePickup(Long loanId) {
        Loan loan = loanRepository.findById(loanId).orElseThrow();
        loan.setStatus("Cancelled");
        loan.getBookCopy().setStatus("AVAILABLE");
        bookCopyRepository.save(loan.getBookCopy());
        loanRepository.save(loan);
    }

    @Override
    @Transactional
    public void markLoanIssue(Long loanId, String issueType) {
        Loan loan = loanRepository.findById(loanId).orElseThrow();
        
        loan.setStatus(issueType); 
        loan.setReturnStatus(issueType);
        loan.setReturnedAt(LocalDateTime.now());
        
        loan.getBookCopy().setStatus(issueType.toUpperCase());
        
        bookCopyRepository.save(loan.getBookCopy());
        loanRepository.save(loan);
    }

    private LoanResponseDTO mapToResponse(Loan loan) {
        String sCode = "";
        if (loan.getUser() != null) {
            sCode = loan.getUser().getUserName(); 
        }
        return LoanResponseDTO.builder()
                .loanId(loan.getId())
                .studentName(loan.getUser() != null ? loan.getUser().getFullName() : "N/A")
                .studentCode(sCode)
                .bookTitle(loan.getBookCopy() != null ? loan.getBookCopy().getBook().getTitle() : "N/A")
                .barcode(loan.getBookCopy() != null ? loan.getBookCopy().getBarcode() : null)
                .borrowDate(loan.getBorrowDate())
                .dueDate(loan.getDueDate())
                .returnedAt(loan.getReturnedAt())
                .status(loan.getStatus())
                .returnStatus(loan.getReturnStatus())
                .staffNote(loan.getStaffNote())
                .build();
    }

    @Override
    public StudentSummaryResponseDTO getStudentBorrowSession(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Sinh viên không tồn tại trên hệ thống."));

        String username = student.getUser().getUserName();

        // Đếm chuẩn những đơn sách thực tế đang được mượn cầm về nhà (Active), bỏ qua Pending tạm thời để không bị khóa nút
        long activeLoans = loanRepository.findAll().stream()
                .filter(l -> l.getUser().getUserName().equalsIgnoreCase(username))
                .filter(l -> l.getReturnedAt() == null && "Active".equalsIgnoreCase(l.getStatus()))
                .count();

        long pendingReservations = reservationRepository.countByStudent_User_UserNameAndStatus(username, "Đang giữ");

        long remainingLimit = 5 - (activeLoans + pendingReservations);
        if (remainingLimit < 0) remainingLimit = 0;

        return new StudentSummaryResponseDTO(
                student.getId(),
                student.getStudentCode(), 
                student.getUser().getFullName(),
                pendingReservations,
                activeLoans,
                student.getTotalDebt() != null ? student.getTotalDebt() : 0.0,
                remainingLimit,
                student.getStatus() 
        );
    }
    
}