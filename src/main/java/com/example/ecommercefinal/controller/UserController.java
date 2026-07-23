package com.example.ecommercefinal.controller;

import com.example.ecommercefinal.dto.UpdateProfileRequest;
import com.example.ecommercefinal.dto.UserResponse;
import com.example.ecommercefinal.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateCurrentUser(@Valid @RequestBody UpdateProfileRequest request){
        UserResponse response= userService.updateCurrentUser(request);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

}
