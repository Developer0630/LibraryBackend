package com.library.library_manager.dto.returns;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
public class ReturnResponseDTO {
    Long returnId;
    Long loanId;
    String studentName;
    String bookTitle;
    LocalDateTime returnDate;
    String actualCondition;
    Double lateFee;
    Double damageFee;
    Double totalFine;
}