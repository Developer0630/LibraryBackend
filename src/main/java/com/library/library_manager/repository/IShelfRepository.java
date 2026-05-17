package com.library.library_manager.repository;

import com.library.library_manager.entity.Shelf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface IShelfRepository extends JpaRepository<Shelf, Long> {
    // Hàm này giúp tìm Kệ sách theo mã code gửi từ Android lên
    Optional<Shelf> findByShelfCode(String shelfCode);
}