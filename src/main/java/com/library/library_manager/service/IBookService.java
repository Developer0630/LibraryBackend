package com.library.library_manager.service;

import com.library.library_manager.dto.book.BookRequestDTO;
import com.library.library_manager.dto.book.BookResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface IBookService {
    List<BookResponseDTO> findAll();

    Page<BookResponseDTO> findByAttr(String keyword, Pageable pageable);

    BookResponseDTO findById(Long id);

    BookResponseDTO create(BookRequestDTO request);

    BookResponseDTO update(Long id, BookRequestDTO request);

    void delete(Long id);

    void updateShelf(Long id, String shelfName);

    void updateStock(Long id, int adjustment);
}