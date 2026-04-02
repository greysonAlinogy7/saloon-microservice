package com.salon.payload.request;

import com.salon.payload.dto.Credentials;
import lombok.Data;

import java.util.List;

@Data
public class UserRequest {
    private  String username;
    private  boolean enabled;
    private  String firstName;
    private  String lastName;
    private  String email;
    private List<Credentials> credentials;

}
