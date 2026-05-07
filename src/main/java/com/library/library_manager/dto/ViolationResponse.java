package com.library.library_manager.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ViolationResponse {
    String type;
    Double fineAmount;
    Boolean isPaid;
    String bookTitle;
}
