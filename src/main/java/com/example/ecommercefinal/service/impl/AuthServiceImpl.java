package com.example.ecommercefinal.service.impl;

import com.example.ecommercefinal.dto.RegisterRequest;
import com.example.ecommercefinal.entity.Role;
import com.example.ecommercefinal.entity.User;
import com.example.ecommercefinal.repository.UserRepository;
import com.example.ecommercefinal.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder=passwordEncoder;
    }

    @Override
    public String register(RegisterRequest request) {
        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already registered");
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.Customer);
        userRepository.save(user);
        return "User registered successfully";
    }
}
