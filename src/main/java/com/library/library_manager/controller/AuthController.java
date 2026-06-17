package com.library.library_manager.controller;

import com.library.library_manager.dto.ApiResponse;
import com.library.library_manager.dto.auth.LoginRequestDTO;
import com.library.library_manager.dto.auth.LoginResponseDTO;
import com.library.library_manager.dto.auth.RegisterRequestDTO;
import com.library.library_manager.service.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;

    // Cổng Đăng nhập: POST /api/auth/login
    @PostMapping("/login")
    public ApiResponse<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO request) {
        return ApiResponse.<LoginResponseDTO>builder()
                .data(authService.login(request))
                .build();
    }

    // Cổng Đăng ký: POST /api/auth/register
    @PostMapping("/register")
    public ApiResponse<String> register(@RequestBody @Valid RegisterRequestDTO request) {
        return ApiResponse.<String>builder()
                .data(authService.register(request))
                .build();
    }
}