package com.library.library_manager.dto.returns;

import lombok.Data;

@Data
public class ReturnRequestDTO {
    Long loanId;
    String conditionStatus; // NORMAL, DAMAGED_LIGHT, DAMAGED_MEDIUM, DAMAGED_HEAVY, LOST
    Double damageFee;
    String staffNote;
}