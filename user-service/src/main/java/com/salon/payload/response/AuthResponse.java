package com.salon.payload.response;

import com.salon.domain.UserRole;
import lombok.Data;

@Data
public class AuthResponse {
    private String jwt;
    private String refresh_token;
    private  String message;
    private UserRole role;
}
