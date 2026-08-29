package com.example.ecommercefinal.controller;

import com.example.ecommercefinal.dto.AuthResponse;
import com.example.ecommercefinal.dto.LoginRequest;
import com.example.ecommercefinal.dto.RegisterRequest;
import com.example.ecommercefinal.service.AuthService;
import com.example.ecommercefinal.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request){
        String response= authService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request){
        AuthResponse response= authService.login(request);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

//    @GetMapping("/token")
//    public String token() {
//        JwtService jwtService = new JwtService();
//        return jwtService.generateToken("hemanth@gmail.com");
//    }

//    @GetMapping("/username")
//    public String username() {
//
//        JwtService jwtService = new JwtService();
//        String token =
//                jwtService.generateToken("hemanth@gmail.com");
//
//        return jwtService.extractExpiration(token).toString();
//    }
}
