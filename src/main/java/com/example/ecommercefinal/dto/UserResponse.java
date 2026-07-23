package com.example.ecommercefinal.dto;

import com.example.ecommercefinal.entity.Role;
import lombok.Data;

@Data
public class UserResponse {
    private Integer id;
    private String name;
    private String email;
    private Role role;

}
