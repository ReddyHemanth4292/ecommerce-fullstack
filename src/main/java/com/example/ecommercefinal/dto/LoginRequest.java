package com.example.ecommercefinal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "User login request")
public class LoginRequest {
    @Schema(description = "Registered email address",
            example = "hemanth@example.com")
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @Schema(description = "Registered email address",
            example = "hemanth@example.com")
    @NotBlank(message = "Password is required")
    private String password;
}
