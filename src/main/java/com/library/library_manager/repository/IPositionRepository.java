package com.library.library_manager.repository;

import com.library.library_manager.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPositionRepository extends JpaRepository<Position, Long> {
    // JpaRepository đã có sẵn hàm findById(Long id) nên bạn không cần viết gì thêm ở đây.
}