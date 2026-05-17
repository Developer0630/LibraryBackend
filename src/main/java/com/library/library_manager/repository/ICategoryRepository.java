package com.library.library_manager.repository;

import com.library.library_manager.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {
    // Hàm này giúp tìm Category theo tên gửi từ Android lên
    Optional<Category> findByCategoryName(String categoryName);
}