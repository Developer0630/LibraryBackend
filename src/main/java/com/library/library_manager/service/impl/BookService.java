package com.library.library_manager.service.impl;

import com.library.library_manager.dto.book.BookRequestDTO;
import com.library.library_manager.dto.book.BookResponseDTO;
import com.library.library_manager.entity.Book;
import com.library.library_manager.entity.BookCopy;
import com.library.library_manager.entity.Category;
import com.library.library_manager.entity.Shelf;
import com.library.library_manager.exception.AppException;
import com.library.library_manager.exception.ErrorCode;
import com.library.library_manager.repository.IBookCopyRepository;
import com.library.library_manager.repository.IBookRepository;
import com.library.library_manager.repository.ICategoryRepository;
import com.library.library_manager.repository.IShelfRepository;
import com.library.library_manager.service.IBookService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookService implements IBookService {

    IBookRepository bookRepository;
    IBookCopyRepository bookCopyRepository;
    ICategoryRepository categoryRepository;
    IShelfRepository shelfRepository;

    @Override
    public List<BookResponseDTO> findAll() {
        return bookRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<BookResponseDTO> findByAttr(String keyword, Pageable pageable) {
        return bookRepository.searchBooks(keyword, pageable)
                .map(this::mapToResponseDTO);
    }

    @Override
    public BookResponseDTO findById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
        return mapToResponseDTO(book);
    }

    @Override
    @Transactional
    public BookResponseDTO create(BookRequestDTO request) {
        // 1. Khởi tạo đối tượng Book từ Request gửi lên
        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .price(request.getPrice())
                .description(request.getDescription())
                .status("Available")
                .categories(new HashSet<>())
                .shelves(new HashSet<>())
                .build();

        // 2. Xử lý lưu Category (Nếu chưa có trong DB thì tự động tạo mới, tránh bị null)
        if (request.getGenre() != null && !request.getGenre().isBlank()) {
            Category category = categoryRepository.findByCategoryName(request.getGenre())
                    .orElseGet(() -> categoryRepository.save(
                            Category.builder().categoryName(request.getGenre()).build()
                    ));
            book.getCategories().add(category);
        }

        // 3. Xử lý lưu Shelf Location (Nếu chưa có trong DB tự tạo luôn cho tiện)
        if (request.getShelfLocation() != null && !request.getShelfLocation().isBlank()) {
            java.util.Optional<Shelf> shelfOpt = shelfRepository.findByShelfCode(request.getShelfLocation());
            if (shelfOpt.isPresent()) {
                book.getShelves().add(shelfOpt.get());
            } else {
                // Cách 1: Nếu không tìm thấy kệ, tự động tạo mới an toàn bằng Setter (Không dùng .location())
                Shelf newShelf = new Shelf();
                newShelf.setShelfCode(request.getShelfLocation());
                Shelf savedShelf = shelfRepository.save(newShelf);
                book.getShelves().add(savedShelf);

                // Cách 2: Nếu không muốn tự tạo mà muốn báo lỗi thẳng thì dùng dòng dưới này thay thế:
                // throw new AppException(ErrorCode.INVALID_SHELF_LOCATION); 
            }
        }
        // 4. Lưu Book để có ID chạy cho bản in
        Book savedBook = bookRepository.save(book);

        // 5. Tạo tự động các bản in lẻ (BookCopy)
        int quantityToCreate = request.getQuantity() > 0 ? request.getQuantity() : 1;
        for (int i = 0; i < quantityToCreate; i++) {
            BookCopy copy = new BookCopy();
            copy.setBook(savedBook);
            copy.setStatus("AVAILABLE");
            
            String generatedBarcode = "BC-" + savedBook.getId() + "-" + System.currentTimeMillis() + "-" + i;
            copy.setBarcode(generatedBarcode);
            
            bookCopyRepository.save(copy);
        }

        return mapToResponseDTO(savedBook);
    }

    @Override
    @Transactional
    public BookResponseDTO update(Long id, BookRequestDTO request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setPrice(request.getPrice());
        book.setDescription(request.getDescription());

        // Cập nhật lại mối quan hệ Category
        if (request.getGenre() != null && !request.getGenre().isBlank()) {
            Category category = categoryRepository.findByCategoryName(request.getGenre())
                    .orElseGet(() -> categoryRepository.save(
                            Category.builder().categoryName(request.getGenre()).build()
                    ));
            book.setCategories(new HashSet<>(Collections.singletonList(category)));
        }

        // Cập nhật lại mối quan hệ Shelf
        if (request.getShelfLocation() != null && !request.getShelfLocation().isBlank()) {
            java.util.Optional<Shelf> shelfOpt = shelfRepository.findByShelfCode(request.getShelfLocation());
            if (shelfOpt.isPresent()) {
                book.setShelves(new java.util.HashSet<>(java.util.Collections.singletonList(shelfOpt.get())));
            } else {
                Shelf newShelf = new Shelf();
                newShelf.setShelfCode(request.getShelfLocation());
                Shelf savedShelf = shelfRepository.save(newShelf);
                book.setShelves(new java.util.HashSet<>(java.util.Collections.singletonList(savedShelf)));
            }
        }

        return mapToResponseDTO(bookRepository.save(book));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));

        boolean hasActiveLoans = book.getBookCopies().stream()
                .anyMatch(copy -> !copy.getStatus().equalsIgnoreCase("AVAILABLE"));

        if (hasActiveLoans) {
            throw new AppException(ErrorCode.CANNOT_DELETE_BOOK);
        }
        bookRepository.delete(book);
    }

    @Override
    @Transactional
    public void updateShelf(Long id, String shelfName) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
        if (shelfName != null && !shelfName.isBlank()) {
            Shelf shelf = shelfRepository.findByShelfCode(shelfName)
                    .orElseGet(() -> {
                        Shelf newShelf = new Shelf();
                        newShelf.setShelfCode(shelfName);
                        return shelfRepository.save(newShelf);
                    });
            book.setShelves(new java.util.HashSet<>(java.util.Collections.singletonList(shelf)));
            bookRepository.save(book);
        }
    }

    @Override
    @Transactional
    public void updateStock(Long id, int adjustment) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));

        if (adjustment > 0) {
            for (int i = 0; i < adjustment; i++) {
                BookCopy copy = new BookCopy();
                copy.setBook(book);
                copy.setStatus("AVAILABLE");
                String generatedBarcode = "BC-" + book.getId() + "-" + System.currentTimeMillis() + "-" + i;
                copy.setBarcode(generatedBarcode);
                bookCopyRepository.save(copy);
            }
        } else if (adjustment < 0) {
            List<BookCopy> availableCopies = bookCopyRepository.findByBookAndStatus(book, "AVAILABLE");
            if (availableCopies.size() < Math.abs(adjustment)) {
                throw new AppException(ErrorCode.INSUFFICIENT_STOCK);
            }
            for (int i = 0; i < Math.abs(adjustment); i++) {
                bookCopyRepository.delete(availableCopies.get(i));
            }
        }
    }

    // Hàm ánh xạ nội bộ chống lặp đệ quy và lọc dữ liệu null
    private BookResponseDTO mapToResponseDTO(Book book) {
        String genreName = null;
        if (book.getCategories() != null && !book.getCategories().isEmpty()) {
            genreName = book.getCategories().iterator().next().getCategoryName();
        }

        String shelfCode = null;
        if (book.getShelves() != null && !book.getShelves().isEmpty()) {
            shelfCode = book.getShelves().iterator().next().getShelfCode();
        }

        int totalStock = 0;
        if (book.getBookCopies() != null) {
            totalStock = book.getBookCopies().size();
        }

        return BookResponseDTO.builder()
                .bookId(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .genre(genreName)
                .price(book.getPrice())
                .description(book.getDescription())
                .shelfLocation(shelfCode)
                .totalStock(totalStock)
                .build();
    }
}