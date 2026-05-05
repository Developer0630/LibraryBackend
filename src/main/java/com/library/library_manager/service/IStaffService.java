package com.library.library_manager.service;

import com.library.library_manager.dto.staff.StaffResponseDTO;
import com.library.library_manager.entity.Staff;
import java.util.List;

public interface IStaffService {
    List<StaffResponseDTO> findAll();

    StaffResponseDTO createStaff(Staff staffRequest);

    StaffResponseDTO updateStaff(Long staffId, Staff staffDetails);

    void deleteStaff(Long staffId);
}