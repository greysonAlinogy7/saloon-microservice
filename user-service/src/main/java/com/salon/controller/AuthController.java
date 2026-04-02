package com.salon.controller;


import com.salon.payload.dto.LoginDTO;
import com.salon.payload.dto.SignupDTO;
import com.salon.payload.response.AuthResponse;
import com.salon.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private  final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody SignupDTO signupDTO) throws Exception {
        AuthResponse response = authService.signup(signupDTO);
        return ResponseEntity.ok(response);

    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginDTO loginDTO) throws Exception {
        AuthResponse response = authService.login(loginDTO.getUsername(), loginDTO.getPassword());
        return ResponseEntity.ok(response);

    }

    @GetMapping("/token/{refreshToken}")
    public ResponseEntity<AuthResponse> refreshToken(@PathVariable String refreshToken) throws Exception {
        AuthResponse response = authService.getAccessTokenFromRefreshToken(refreshToken);
        return ResponseEntity.ok(response);

    }
}
