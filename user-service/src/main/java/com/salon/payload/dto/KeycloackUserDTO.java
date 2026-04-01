package com.salon.payload.dto;

import com.salon.domain.UserRole;
import lombok.Data;

@Data
public class KeycloackUserDTO {
    private  String id;
    private  String firstName;
    private  String lastName;
    private String email;
    private  String username;

}
