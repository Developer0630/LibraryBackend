package com.library.library_manager.service;

import com.library.library_manager.dto.loan.EligibilityResponseDTO;
import com.library.library_manager.dto.loan.LoanRequestDTO;
import com.library.library_manager.dto.loan.LoanResponseDTO;
import com.library.library_manager.dto.student.StudentSummaryResponseDTO;

import java.util.List;

public interface ILoanService {
    EligibilityResponseDTO checkLoanEligibility(Long studentId, Long copyId);
    LoanResponseDTO createNewLoan(LoanRequestDTO request);
    LoanResponseDTO createLoanFromReservation(Long reservationId);
    LoanResponseDTO confirmLoanPickup(Long loanId);
    List<LoanResponseDTO> getAllLoans(Long studentId, String status);
    LoanResponseDTO getById(Long loanId);
    void cancelLoanBeforePickup(Long loanId);
    void markLoanIssue(Long loanId, String issueType);
    StudentSummaryResponseDTO getStudentBorrowSession(Long studentId);
}