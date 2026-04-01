package com.salon.service;


import com.salon.payload.dto.Credentials;
import com.salon.payload.dto.SignupDTO;
import com.salon.payload.dto.UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;



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

    public  void createUser(SignupDTO signupDTO){

        String ACCESS_TOKEN="";

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
        }

    }

}
