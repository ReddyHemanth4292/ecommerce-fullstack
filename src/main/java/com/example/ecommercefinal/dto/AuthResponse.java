package com.example.ecommercefinal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Authentication response")
public class AuthResponse {
    @Schema(description = "JWT access token")
    private String token;
    public AuthResponse(String token) {
        this.token = token;
    }
}
