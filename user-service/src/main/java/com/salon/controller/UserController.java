package com.salon.controller;

import com.salon.entity.User;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

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
