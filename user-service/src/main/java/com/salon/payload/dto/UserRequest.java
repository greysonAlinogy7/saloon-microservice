package com.salon.payload.dto;

import lombok.Data;

@Data
public class UserRequest {
    private  String username;
    private  boolean enabled;
    private  String firstName;
    private  String lastName;
    private  String email;
    private  String password;
}
