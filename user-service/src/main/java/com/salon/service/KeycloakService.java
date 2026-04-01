package com.salon.service;


import com.salon.payload.dto.Credentials;
import com.salon.payload.dto.KeycloackRole;
import com.salon.payload.dto.KeycloackUserDTO;
import com.salon.payload.dto.SignupDTO;
import com.salon.payload.request.UserRequest;
import com.salon.payload.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class KeycloakService {
    private  static  final String KEYCLOAK_BASE_URL="http://localhost:8080";
    private  static  final  String KEYCLOAK_ADMIN_API=KEYCLOAK_BASE_URL+"/admin/realms/master/users";

    private static  final String TOKEN_URL=KEYCLOAK_BASE_URL+"realms/master/protocol/openid-connect/token";
    private static  final String CLIENT_ID = "salon-booking-clients";
    private  static final  String CLIENT_SECRET = "NAppynsbwwqDwyjb1jZRPc0WpJmPje8X";
    private  static  final String GRANT_TYPE="password";
    private  static  final String scope = "openid profile email";
    private  static  final String username = "admin";
    private  static  final String password = "admin";
    private static  final String clientId = "4e08b689-41bd-4880-8472-2103daf8a184";


    private final RestTemplate restTemplate;

    public  void createUser(SignupDTO signupDTO) throws Exception {

        String ACCESS_TOKEN=getAdminAccessToken(username, password,GRANT_TYPE, null).getAccessToken();

        Credentials credentials = new Credentials();
        credentials.setTemporary(false);
        credentials.setType("password");
        credentials.setValue(signupDTO.getPassword());

        UserRequest userRequest = new UserRequest();
        userRequest.setUsername(signupDTO.getUsername());
        userRequest.setEmail(signupDTO.getEmail());
        userRequest.setEnabled(true);
        userRequest.setLastName(signupDTO.getLastName());
        userRequest.setFirstName(signupDTO.getFirstName());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBasicAuth(ACCESS_TOKEN);

        HttpEntity<UserRequest> requestEntity = new HttpEntity<>(userRequest, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                KEYCLOAK_ADMIN_API,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        if (response.getStatusCode()==HttpStatus.CREATED){
            System.out.println("user created successfully");

            KeycloackUserDTO user = fetchFirstUserByUsername(signupDTO.getUsername(), ACCESS_TOKEN);
            KeycloackRole role = getRoleByName(clientId, ACCESS_TOKEN, signupDTO.getRoles().toString());

            List<KeycloackRole> roles = new ArrayList<>();
            roles.add(role);

            assignRoleToUser(user.getId(), clientId, roles, ACCESS_TOKEN);
        } else {
            System.out.println("user creation failed");
            throw  new Exception(response.getBody());
        }

    }

    public TokenResponse getAdminAccessToken(String username, String password, String grantType, String refreshToken){
        return new TokenResponse();
    }

    public KeycloackRole getRoleByName(String clientId, String token, String role){
        return null;
    }

    public KeycloackUserDTO fetchFirstUserByUsername(String username, String token){
        return null;
    }
    public  void assignRoleToUser(String userId, String clientId, List<KeycloackRole> roles, String token){

    }

}
