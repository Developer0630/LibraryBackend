package com.library.library_manager.repository;

import com.library.library_manager.entity.BookReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBookReviewRepository extends JpaRepository<BookReview, Long> {
    // Lấy danh sách đánh giá của một đầu sách
    List<BookReview> findByBook_IdOrderByCreatedAtDesc(Long bookId);

    // Kiểm tra xem sinh viên đã đánh giá cuốn sách này chưa (để tránh đánh giá nhiều lần)
    boolean existsByBook_IdAndStudent_User_UserName(Long bookId, String username);
}