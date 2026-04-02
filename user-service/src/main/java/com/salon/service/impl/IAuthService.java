package com.salon.service.impl;

import com.salon.payload.dto.SignupDTO;
import com.salon.payload.response.AuthResponse;

public interface IAuthService {
    AuthResponse login(String username, String password) throws Exception;
    AuthResponse signup(SignupDTO req) throws Exception;
    AuthResponse getAccessTokenFromRefreshToken(String refreshToken) throws Exception;
}
