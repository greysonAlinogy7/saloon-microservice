package com.salon.payload.dto;

import com.salon.domain.UserRole;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignupDTO {
    private String firstName;
    private  String lastName;
    private  String email;
    private  String password;
    private  String username;
    private List<Credentials> credentials = new ArrayList<>();
    private UserRole roles;
}
