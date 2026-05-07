package com.library.library_manager.repository;

import com.library.library_manager.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IReservationRepository extends JpaRepository<Reservation, Long> {
    // Tìm qua: Reservation -> Student -> User -> UserName
    List<Reservation> findByStudent_User_UserNameOrderByRequestDateDesc(String username);
}