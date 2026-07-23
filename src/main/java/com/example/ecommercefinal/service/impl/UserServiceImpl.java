package com.example.ecommercefinal.service.impl;

import com.example.ecommercefinal.config.SecurityConfig;
import com.example.ecommercefinal.dto.UpdateProfileRequest;
import com.example.ecommercefinal.dto.UserResponse;
import com.example.ecommercefinal.entity.User;
import com.example.ecommercefinal.repository.UserRepository;
import com.example.ecommercefinal.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse getCurrentUser() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String email= authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("user not found"));
        UserResponse response= new UserResponse(user.getId(),user.getName(),user.getEmail(),user.getRole());
        return response;
    }

    @Override
    public UserResponse updateCurrentUser(UpdateProfileRequest request) {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String email= authentication.getName();
        User user=userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User not Found"));
        user.setName(request.getName());
        userRepository.save(user);
        UserResponse response=new UserResponse(user.getId(),user.getName(),user.getEmail(),user.getRole());
        return response;
    }
}
