package com.example.ecommercefinal.controller;

import com.example.ecommercefinal.dto.UserResponse;
import com.example.ecommercefinal.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(){
        UserResponse user=userService.getCurrentUser();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
