package com.example.ecommercefinal.service;

import com.example.ecommercefinal.dto.AuthResponse;
import com.example.ecommercefinal.dto.LoginRequest;
import com.example.ecommercefinal.dto.RegisterRequest;

public interface AuthService {
    String register(RegisterRequest request);
    AuthResponse login(LoginRequest Request);
}
