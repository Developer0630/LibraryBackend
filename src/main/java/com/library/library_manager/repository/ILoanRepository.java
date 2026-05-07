package com.library.library_manager.repository;

import com.library.library_manager.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ILoanRepository extends JpaRepository<Loan, Long> {
    // Lấy danh sách sách đang mượn (chưa trả) của một sinh viên
    List<Loan> findByUser_UserNameAndReturnedAtIsNull(String username);

    // 2. Thêm hàm này để StudentService gọi count (Fix lỗi Cannot resolve)
    long countByUser_UserNameAndReturnedAtIsNull(String username);
}

