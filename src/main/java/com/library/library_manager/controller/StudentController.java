package com.library.library_manager.controller;

import com.library.library_manager.dto.ApiResponse;
import com.library.library_manager.dto.BorrowHistoryDTO;
import com.library.library_manager.dto.PageResponse;
import com.library.library_manager.dto.PasswordResetRequest;
import com.library.library_manager.dto.ViolationDTO;
import com.library.library_manager.dto.student.StudentProfileResponseDTO;
import com.library.library_manager.dto.student.StudentRequestDTO;
import com.library.library_manager.dto.student.StudentResponseDTO;
import com.library.library_manager.entity.Student;
import com.library.library_manager.entity.User;
import com.library.library_manager.repository.IStudentRepository;
import com.library.library_manager.service.IStudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {
    private final IStudentService studentService;
    private final IStudentRepository studentRepository;

    @GetMapping
    public ApiResponse<PageResponse<StudentResponseDTO>> getAll(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "studentCode", required = false) String studentCode,
            @RequestParam(value = "status", required = false) String status) {
        return ApiResponse.<PageResponse<StudentResponseDTO>>builder()
                .data(studentService.getAll(page, size, studentCode, status))
                .build();
    }

    @PostMapping
    public ApiResponse<StudentResponseDTO> create(@RequestBody @Valid StudentRequestDTO request) {
        return ApiResponse.<StudentResponseDTO>builder()
                .data(studentService.create(request))
                .build();
    }

    @GetMapping("/{studentId}")
    public ApiResponse<StudentResponseDTO> getDetail(@PathVariable("studentId") Long studentId) {
        return ApiResponse.<StudentResponseDTO>builder()
                .data(studentService.getById(studentId))
                .build();
    }

    @PutMapping("/{studentId}")
    public ApiResponse<StudentResponseDTO> update(
            @PathVariable("studentId") Long studentId,
            @RequestBody @Valid StudentRequestDTO request) {
        return ApiResponse.<StudentResponseDTO>builder()
                .data(studentService.update(studentId, request))
                .build();
    }

    @PatchMapping("/{studentId}/status")
    public ApiResponse<String> updateStatus(
            @PathVariable("studentId") Long studentId,
            @RequestParam("status") String status) {
        studentService.updateStatus(studentId, status);
        return ApiResponse.<String>builder().data("Update status successfully").build();
    }

    // 6. Đặt lại mật khẩu
    @PostMapping("/{studentId}/reset-password")
    public ResponseEntity<String> resetPassword(
            @PathVariable("studentId") Long studentId,
            @RequestBody PasswordResetRequest request) {
        studentService.resetPassword(studentId, request.getNewPassword());
        return ResponseEntity.ok("Passworld is changed, student id: " + studentId); //khng cần trả về

    }

    // 7. Xem lịch sử mượn trả
    @GetMapping("/{studentId}/borrow-history")
    public ResponseEntity<List<BorrowHistoryDTO>> getBorrowHistory(@PathVariable("studentId") Long studentId) {
        return ResponseEntity.ok(studentService.getBorrowHistory(studentId));
    }

    // 8. Xem lịch sử vi phạm & công nợ
    @GetMapping("/{studentId}/violations")
    public ResponseEntity<List<ViolationDTO>> getViolations(@PathVariable("studentId") Long studentId) {
        return ResponseEntity.ok(studentService.getViolations(studentId));
    }
    
    @GetMapping("/profile")
    public StudentProfileResponseDTO getProfile(@RequestParam("username") String username) {
        Student student = studentRepository.findByUser_UserName(username)
                .orElseThrow(() -> new RuntimeException("Don't get student with this username"));

        User user = student.getUser();

        return new StudentProfileResponseDTO(
                user.getFullName(),
                student.getStudentCode(),
                user.getEmail(),
                user.getPhoneNumber(),
                student.getClazz(),
                student.getMajor(),
                student.getStatus()
        );
    }
}
