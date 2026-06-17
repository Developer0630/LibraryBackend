package com.library.library_manager.service;

import com.library.library_manager.dto.auth.LoginRequestDTO;
import com.library.library_manager.dto.auth.LoginResponseDTO;
import com.library.library_manager.dto.auth.RegisterRequestDTO; 

public interface IAuthService {
    LoginResponseDTO login(LoginRequestDTO request);
    String register(RegisterRequestDTO request); 
}