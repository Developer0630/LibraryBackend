package com.library.library_manager.repository;

import com.library.library_manager.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface INotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUser_UserNameOrderByCreatedAtDesc(String username);
}
