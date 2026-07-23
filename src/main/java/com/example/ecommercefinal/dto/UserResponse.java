package com.example.ecommercefinal.dto;

import com.example.ecommercefinal.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Integer id;
    private String name;
    private String email;
    private Role role;

}
