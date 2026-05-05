package com.library.library_manager.repository;

import com.library.library_manager.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IStaffRepository extends JpaRepository<Staff, Long> {
}
