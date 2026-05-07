package com.library.library_manager.repository;

import com.library.library_manager.entity.Violation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IViolationRepository extends JpaRepository<Violation, Long> {
    // Lấy danh sách vi phạm dựa trên username thông qua bảng Loan
    List<Violation> findByLoan_User_UserName(String username);
}
