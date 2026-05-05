package com.library.library_manager.service;

import com.library.library_manager.dto.BorrowHistoryDTO;
import com.library.library_manager.dto.PageResponse;
import com.library.library_manager.dto.ViolationDTO;
import com.library.library_manager.dto.student.StudentRequestDTO;
import com.library.library_manager.dto.student.StudentResponseDTO;

import java.util.List;

public interface IStudentService {
    // Sử dụng PageResponse thay vì PageDataResponse
    PageResponse<StudentResponseDTO> getAll(int page, int size, String studentCode, String status);

    StudentResponseDTO getById(Long id);

    StudentResponseDTO create(StudentRequestDTO request);

    StudentResponseDTO update(Long id, StudentRequestDTO request);

    void updateStatus(Long id, String status);

    void resetPassword(Long id);

    List<BorrowHistoryDTO> getBorrowHistory(Long id);

    List<ViolationDTO> getViolations(Long id);
}
