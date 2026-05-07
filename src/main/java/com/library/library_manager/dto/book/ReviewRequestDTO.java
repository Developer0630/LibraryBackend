package com.library.library_manager.dto.book;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewRequestDTO {

    @NotNull(message = "Rate star can not empty")
    @Min(value = 1, message = "Min star is 1")
    @Max(value = 5, message = "Max star is 5")
    Integer rating;

    @NotBlank(message = "Comment is not empty")
    String comment;
}