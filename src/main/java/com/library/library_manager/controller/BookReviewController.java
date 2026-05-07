package com.library.library_manager.controller;

import com.library.library_manager.dto.book.ReviewRequestDTO;
import com.library.library_manager.service.impl.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookReviewController {

    private final StudentService studentService;
    private final String CURRENT_USER = "SV001"; // Giả lập user đang đăng nhập

    // POST /api/books/{bookId}/reviews: Gửi đánh giá sách
    @PostMapping("/books/{bookId}/reviews")
    public ResponseEntity<String> sendReview(
            @PathVariable Long bookId,
            @Valid @RequestBody ReviewRequestDTO dto) {
        studentService.postReview(bookId, dto, CURRENT_USER);
        return ResponseEntity.ok("Gửi đánh giá thành công!");
    }

    // PUT /api/book/reviews/{reviewId}: Cập nhật đánh giá đã gửi
    @PutMapping("/book/reviews/{reviewId}")
    public ResponseEntity<String> updateReview(
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewRequestDTO dto) {
        studentService.updateReview(reviewId, dto, CURRENT_USER);
        return ResponseEntity.ok("Cập nhật đánh giá thành công!");
    }
}