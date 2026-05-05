package com.library.library_manager.repository;

import com.library.library_manager.entity.Book;
import com.library.library_manager.entity.BookCopy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBookCopyRepository extends JpaRepository<BookCopy, Long> {
    List<BookCopy> findByBookAndStatus(Book book, String status);

    @Query("SELECT bc FROM BookCopy bc WHERE bc.book.id = :bookId")
    List<BookCopy> findByBookId(@Param("bookId") Long bookId);

    // Tìm bản in theo ID đầu sách VÀ trạng thái (Ví dụ: AVAILABLE, BORROWED)
    List<BookCopy> findByBookIdAndStatus(Long bookId, String status);
}
