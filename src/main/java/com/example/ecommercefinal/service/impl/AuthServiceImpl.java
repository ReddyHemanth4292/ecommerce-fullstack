package com.example.ecommercefinal.service.impl;

import com.example.ecommercefinal.dto.AuthResponse;
import com.example.ecommercefinal.dto.LoginRequest;
import com.example.ecommercefinal.dto.RegisterRequest;
import com.example.ecommercefinal.entity.Role;
import com.example.ecommercefinal.entity.User;
import com.example.ecommercefinal.exception.EmailAlreadyExistsException;
import com.example.ecommercefinal.repository.UserRepository;
import com.example.ecommercefinal.service.AuthService;
import com.example.ecommercefinal.service.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder=passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public String register(RegisterRequest request) {
        if(userRepository.existsByEmail(request.getEmail())){
            throw new EmailAlreadyExistsException("Email already registered");
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.Customer);
        userRepository.save(user);
        return "User registered successfully";
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication=authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
        System.out.println(authentication.getName());
        System.out.println(authentication.getAuthorities());
        JwtService jwtService=new JwtService();
        AuthResponse authResponse=new AuthResponse(jwtService.generateToken(authentication.getName()));
        return authResponse;
    }
}
