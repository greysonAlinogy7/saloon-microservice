package com.salon.controller;

import com.salon.entity.User;
import com.salon.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private  final UserRepository userRepository;

    @PostMapping
    public User createUser(@RequestBody User user){
        return userRepository.save(user);
    }

    @GetMapping()
    public User getUser(){
        User user = new User();
        user.setEmail("greysonshawa@gmail.com");
        user.setFullName("greyson");
        user.setPhone("o675238360");
        user.setRole("Customer");
        return user;

    }
}
