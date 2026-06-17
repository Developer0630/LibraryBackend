package com.library.library_manager.repository;

import com.library.library_manager.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IBookRepository extends JpaRepository<Book, Long> { 

    @Query("SELECT b FROM Book b WHERE b.title LIKE %:kw% OR b.author LIKE %:kw%")
    Page<Book> searchBooks(@Param("kw") String keyword, Pageable pageable);
}
