package com.library.library_manager.service.impl;

import com.library.library_manager.dto.BorrowHistoryDTO;
import com.library.library_manager.dto.PageResponse;
import com.library.library_manager.dto.PasswordResetRequest;
import com.library.library_manager.dto.ViolationDTO;
import com.library.library_manager.dto.student.StudentRequestDTO;
import com.library.library_manager.dto.student.StudentResponseDTO;
import com.library.library_manager.entity.Role;
import com.library.library_manager.entity.Student;
import com.library.library_manager.entity.User;
import com.library.library_manager.exception.AppException;
import com.library.library_manager.exception.ErrorCode;
import com.library.library_manager.repository.IRoleRepository;
import com.library.library_manager.repository.IStudentRepository;
import com.library.library_manager.repository.IUserRepository;
import com.library.library_manager.service.IStudentService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StudentService implements IStudentService {
    IStudentRepository studentRepository;
    IUserRepository userRepository;
    IRoleRepository roleRepository;

    @Override
    public PageResponse<StudentResponseDTO> getAll(int page, int size, String studentCode, String status) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());
        Page<Student> studentPage = studentRepository.findAllWithFilters(studentCode, status, pageable);
        Page<StudentResponseDTO> responsePage = studentPage.map(this::mapToResponse);
        return new PageResponse<>(responsePage);
    }

    @Override
    public StudentResponseDTO getById(Long id) {
        return studentRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));
    }

    @Override
    @Transactional
    public StudentResponseDTO create(StudentRequestDTO request) {
        if (studentRepository.existsByStudentCode(request.getStudentCode())) {
            throw new AppException(ErrorCode.STUDENT_ALREADY_EXISTS);
        }

        // 1. Tìm Role Entity từ database
        Role studentRole = roleRepository.findByRoleName("STUDENT")
                .orElseThrow(() -> new RuntimeException("Role STUDENT not found in database"));

        // 2. Tạo User (Auth)
        Set<Role> roles = new HashSet<>();
        roles.add(studentRole);

        User user = User.builder()
                .userName(request.getStudentCode()) // Đảm bảo đúng tên field trong User entity (username/userName)
                .password("123456")
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .roles(roles) // Gán Set roles (n-n)
                .build();
        user = userRepository.save(user);

        // 3. Tạo Student profile
        Student student = Student.builder()
                .user(user)
                .studentCode(request.getStudentCode())
                .major(request.getMajor())
                .clazz(request.getClazz())
                .status("ACTIVE")
                .totalDebt(0.0)
                .build();

        return mapToResponse(studentRepository.save(student));
    }

    @Override
    @Transactional
    public StudentResponseDTO update(Long id, StudentRequestDTO request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));

        student.setMajor(request.getMajor());
        student.setClazz(request.getClazz());

        User user = student.getUser();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        userRepository.save(user);

        return mapToResponse(studentRepository.save(student));
    }

    @Override
    @Transactional
    public void updateStatus(Long id, String status) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));

        if (!List.of("ACTIVE", "LOCKED").contains(status.toUpperCase())) {
            throw new AppException(ErrorCode.INVALID_STUDENT_STATUS);
        }

        student.setStatus(status.toUpperCase());
        studentRepository.save(student);
    }

    public void resetPassword(Long studentId, String newPassword) {
        // 1. Kiểm tra sinh viên tồn tại
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("STUDENT_NOT_FOUND"));

        // 2. Lấy User và cập nhật mật khẩu tùy chỉnh
        User user = student.getUser();

        user.setPassword(newPassword);

        // 3. Lưu vào DB
        userRepository.save(user);
    }

    @Override
    public List<BorrowHistoryDTO> getBorrowHistory(Long id) {
        return Collections.emptyList();
    }

    @Override
    public List<ViolationDTO> getViolations(Long id) {
        return Collections.emptyList();
    }

    private StudentResponseDTO mapToResponse(Student student) {
        return StudentResponseDTO.builder()
                .id(student.getId())
                .studentCode(student.getStudentCode())
                .fullName(student.getUser().getFullName())
                .email(student.getUser().getEmail())
                .phoneNumber(student.getUser().getPhoneNumber())
                .major(student.getMajor())
                .clazz(student.getClazz())
                .status(student.getStatus())
                .balance(student.getTotalDebt())
                .build();
    }
}