package com.example.ecommercefinal.service;

import com.example.ecommercefinal.dto.UpdateProfileRequest;
import com.example.ecommercefinal.dto.UserResponse;

public interface UserService {
    UserResponse getCurrentUser();
    UserResponse updateCurrentUser(UpdateProfileRequest request);
}
