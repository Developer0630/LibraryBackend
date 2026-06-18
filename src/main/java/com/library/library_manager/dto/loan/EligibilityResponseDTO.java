package com.library.library_manager.dto.loan;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EligibilityResponseDTO {
    boolean pass;
    String reason;
}