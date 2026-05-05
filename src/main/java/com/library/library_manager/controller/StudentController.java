package com.library.library_manager.controller;

import com.library.library_manager.dto.ApiResponse;
import com.library.library_manager.dto.student.StudentRequestDTO;
import com.library.library_manager.dto.student.StudentResponseDTO;
import com.library.library_manager.service.IStudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {
    private final IStudentService studentService;

    @PostMapping
    public ApiResponse<StudentResponseDTO> create(@RequestBody @Valid StudentRequestDTO request) {
        return ApiResponse.<StudentResponseDTO>builder()
                .data(studentService.create(request))
                .build();
    }

    @GetMapping("/{studentId}")
    public ApiResponse<StudentResponseDTO> getDetail(@PathVariable Long studentId) {
        return ApiResponse.<StudentResponseDTO>builder()
                .data(studentService.getById(studentId))
                .build();
    }

    @PatchMapping("/{studentId}/status")
    public ApiResponse<String> updateStatus(
            @PathVariable Long studentId,
            @RequestParam String status) {
        studentService.updateStatus(studentId, status);
        return ApiResponse.<String>builder().data("Update status successfully").build();
    }
}
