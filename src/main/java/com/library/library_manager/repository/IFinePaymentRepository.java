package com.library.library_manager.repository;

import com.library.library_manager.entity.FinePayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IFinePaymentRepository extends JpaRepository<FinePayment, Long> {
    // Truy vấn: FinePayment -> Student -> User -> UserName
    List<FinePayment> findByStudent_User_UserNameOrderByPaymentDateDesc(String username);
}
