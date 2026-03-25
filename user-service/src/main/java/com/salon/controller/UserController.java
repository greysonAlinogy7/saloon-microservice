package com.salon.controller;

import com.salon.entity.User;
import com.salon.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private  final UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Valid User user){
        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<User>> getUser(){
        List<User> userList = userService.getAllUser();
        return new ResponseEntity<>(userList, HttpStatus.OK);

    }

    @GetMapping("/{userId}")
    public  ResponseEntity<User> getUserById(@PathVariable("userId") Long userId) throws Exception {
        User user = userService.getUserById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable("userId") Long userId) throws Exception {
        User updatedUser = userService.getUserById(userId);
       return new ResponseEntity<>(updatedUser, HttpStatus.OK);

    }


    @DeleteMapping("/{userId}")
    public  ResponseEntity<String> deleteUserById(@PathVariable("userId") Long userId) throws Exception {
        userService.deleteUser(userId);
        return new ResponseEntity<>("user deleted successfully", HttpStatus.OK);
    }
}
