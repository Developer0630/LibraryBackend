package com.library.library_manager.repository;

import com.library.library_manager.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBorrowHistoryRepository extends JpaRepository<Loan, Long> {
    // Lấy tất cả lịch sử (cả đang mượn và đã trả)
    List<Loan> findByUser_UserNameOrderByBorrowDateDesc(String username);
}
