package com.library.library_manager.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentHistoryResponse {
    private LocalDateTime paymentDate;
    private Double amount;
    private String method; // Tiền mặt, Chuyển khoản...
}
