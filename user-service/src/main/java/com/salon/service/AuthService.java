package com.salon.service;


import com.salon.config.JwtProvider;
import com.salon.entity.User;
import com.salon.repository.UserRepository;
import com.salon.service.impl.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {
    private  final UserRepository userRepository;


    @Override
    public User getUserByProfile(String jwt) {
        String email = JwtProvider.getEmailFromToken(jwt);
        return userRepository.findByEmail(email);
    }


}
