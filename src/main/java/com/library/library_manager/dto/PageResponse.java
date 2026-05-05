package com.library.library_manager.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageResponse<T> {
    PageCustom<T> pageable;
    List<T> content;

    public PageResponse(Page<T> page) {
        this.pageable = new PageCustom<>(page);
        this.content = page.getContent();
    }
}
