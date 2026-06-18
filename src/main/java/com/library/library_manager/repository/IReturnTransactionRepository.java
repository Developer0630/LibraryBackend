package com.library.library_manager.repository;

import com.library.library_manager.entity.ReturnTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IReturnTransactionRepository extends JpaRepository<ReturnTransaction, Long> {
    // Tìm kiếm giao dịch trả dựa vào ID phiếu mượn
    Optional<ReturnTransaction> findByLoan_Id(Long loanId);
}