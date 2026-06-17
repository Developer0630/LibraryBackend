package com.library.library_manager.service.impl;

import com.library.library_manager.dto.auth.LoginRequestDTO;
import com.library.library_manager.dto.auth.LoginResponseDTO;
import com.library.library_manager.dto.auth.RegisterRequestDTO;
import com.library.library_manager.entity.Role;
import com.library.library_manager.entity.User;
import com.library.library_manager.entity.Student;
import com.library.library_manager.repository.IRoleRepository;
import com.library.library_manager.repository.IUserRepository;
import com.library.library_manager.repository.IStudentRepository;
import com.library.library_manager.service.IAuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService implements IAuthService {

    IUserRepository userRepository;
    IRoleRepository roleRepository;
    IStudentRepository studentRepository;

    @Override
    public LoginResponseDTO login(LoginRequestDTO request) {
        User user = userRepository.findByUserName(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Tài khoản hoặc mật khẩu không chính xác!"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Tài khoản hoặc mật khẩu không chính xác!");
        }

        Set<String> roles = user.getRoles().stream()
                .map(Role::getRoleName)
                .collect(Collectors.toSet());

        return LoginResponseDTO.builder()
                .userId(user.getUserId())
                .username(user.getUserName())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .roles(roles)
                .message("Đăng nhập thành công!")
                .build();
    }

    @Override
    @Transactional
    public String register(RegisterRequestDTO request) {
        // 1. Check trùng tên tài khoản
        if (userRepository.findByUserName(request.getUsername()).isPresent()) {
            throw new RuntimeException("Tên tài khoản này đã tồn tại!");
        }

        // 2. Lấy Role tương ứng trong DB (STUDENT hoặc STAFF)
        String targetRole = request.getRole().toUpperCase();
        Role roleEntity = roleRepository.findByRoleName(targetRole)
                .orElseThrow(() -> new RuntimeException("Quyền " + targetRole + " không tồn tại trong hệ thống!"));

        Set<Role> roles = new HashSet<>();
        roles.add(roleEntity);

        // 3. Lưu thông tin tài khoản chung vào bảng User
        User newUser = User.builder()
                .userName(request.getUsername())
                .password(request.getPassword()) 
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .roles(roles)
                .build();

        User savedUser = userRepository.save(newUser);

        // 4. Nếu là Sinh viên -> Tự động tạo thêm bản ghi bên bảng Student để đồng bộ @OneToOne
        if ("STUDENT".equals(targetRole)) {
            Student studentProfile = Student.builder()
                    .user(savedUser)
                    .studentCode(request.getUsername()) 
                    .clazz("Chưa xếp lớp")
                    .major("Chưa cập nhật")
                    .status("ACTIVE")
                    .totalDebt(0.0)
                    .build();
            studentRepository.save(studentProfile);
        }

        return "Đăng ký tài khoản thành công!";
    }
}