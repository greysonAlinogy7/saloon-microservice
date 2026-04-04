package com.salon.service;

import com.salon.payload.dto.*;
import com.salon.payload.request.UserRequest;
import com.salon.payload.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeycloakService {

    private static final String BASE_URL = "http://localhost:8080";
    private static final String REALM = "master";

    private static final String ADMIN_USERS_URL =
            BASE_URL + "/admin/realms/" + REALM + "/users";

    private static final String TOKEN_URL =
            BASE_URL + "/realms/" + REALM + "/protocol/openid-connect/token";

    private static final String CLIENT_ID = "salon-booking-clients";
    private static final String CLIENT_SECRET = "NAppynsbwwqDwyjb1jZRPc0WpJmPje8X";

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";

    private static final String CLIENT_UUID = "4e08b689-41bd-4880-8472-2103daf8a184";

    private final RestTemplate restTemplate;


    // CREATE USER + ASSIGN ROLE

    public void createUser(SignupDTO signupDTO) throws Exception {

        // 1. Get admin token ONCE
        String token = getAdminAccessToken().getAccessToken();

        // 2. Prepare credentials
        Credentials credentials = new Credentials();
        credentials.setTemporary(false);
        credentials.setType("password");
        credentials.setValue(signupDTO.getPassword());

        // 3. Prepare user request
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername(signupDTO.getUsername());
        userRequest.setEmail(signupDTO.getEmail());
        userRequest.setEnabled(true);
        userRequest.setFirstName(signupDTO.getFullName());
        userRequest.setLastName(signupDTO.getLastName());
        userRequest.setCredentials(Collections.singletonList(credentials));

        // 4. Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<UserRequest> request = new HttpEntity<>(userRequest, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    ADMIN_USERS_URL,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            if (response.getStatusCode() == HttpStatus.CREATED) {
                System.out.println("User created successfully in Keycloak");

                // 5. Fetch created user
                KeycloackUserDTO user = fetchUserByUsername(signupDTO.getUsername(), token);

                // 6. Assign role (safe default)
                String roleName = signupDTO.getRoles() != null
                        ? signupDTO.getRoles().toString()
                        : "USER";

                KeycloackRole role = getRoleByName(CLIENT_UUID, token, roleName);

                if (role != null) {
                    assignRoleToUser(user.getId(), CLIENT_UUID, Collections.singletonList(role), token);
                    System.out.println("Role assigned: " + role.getName());
                }

            } else {
                throw new Exception("User creation failed: " + response.getBody());
            }

        } catch (HttpClientErrorException e) {
            System.err.println("Keycloak error: " + e.getStatusCode());
            System.err.println("Response: " + e.getResponseBodyAsString());
            throw new Exception("Failed to create user: " + e.getResponseBodyAsString(), e);
        }
    }


    //  GET ADMIN TOKEN

    public TokenResponse getAdminAccessToken() throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("client_id", CLIENT_ID);
        body.add("client_secret", CLIENT_SECRET);
        body.add("username", ADMIN_USERNAME);
        body.add("password", ADMIN_PASSWORD);

        HttpEntity<MultiValueMap<String, String>> request =
                new HttpEntity<>(body, headers);

        try {
            ResponseEntity<TokenResponse> response = restTemplate.exchange(
                    TOKEN_URL,
                    HttpMethod.POST,
                    request,
                    TokenResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                System.out.println("Token obtained successfully");
                return response.getBody();
            }

            throw new Exception("Failed to get token");

        } catch (HttpClientErrorException e) {
            System.err.println("Token error: " + e.getStatusCode());
            System.err.println("Response: " + e.getResponseBodyAsString());
            throw new Exception("Token request failed", e);
        }
    }


    // FETCH USER

    public KeycloackUserDTO fetchUserByUsername(String username, String token) throws Exception {

        String url = BASE_URL + "/admin/realms/" + REALM + "/users?username=" + username;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<KeycloackUserDTO[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                KeycloackUserDTO[].class
        );

        KeycloackUserDTO[] users = response.getBody();

        if (users != null && users.length > 0) {
            return users[0];
        }

        throw new Exception("User not found");
    }


    // GET ROLE
    public KeycloackRole getRoleByName(String clientId, String token, String roleName) {

        String url = BASE_URL + "/admin/realms/" + REALM +
                "/clients/" + clientId + "/roles/" + roleName;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<KeycloackRole> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                KeycloackRole.class
        );

        return response.getBody();
    }


    // ASSIGN ROLE

    public void assignRoleToUser(String userId, String clientId,
                                 List<KeycloackRole> roles, String token) throws Exception {

        String url = BASE_URL + "/admin/realms/" + REALM +
                "/users/" + userId + "/role-mappings/clients/" + clientId;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List<KeycloackRole>> request =
                new HttpEntity<>(roles, headers);

        try {
            restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        } catch (Exception e) {
            throw new Exception("Failed to assign role", e);
        }
    }

    public TokenResponse getNewUserToken(String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("client_id", CLIENT_ID);
        body.add("client_secret", CLIENT_SECRET);
        body.add("username", username); // Pass the actual username
        body.add("password", password); // Pass the actual password

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        // This calls Keycloak directly for a NEW session for THIS user
        return restTemplate.postForObject(TOKEN_URL, request, TokenResponse.class);
    }

    public KeycloackUserDTO fetchUserProfileByJwt(String token) throws Exception {

        String url = BASE_URL + "/realms/" + REALM + "/protocol/openid-connect/userinfo";

        HttpHeaders headers = new HttpHeaders();

        // Normalize token
        String cleanToken = token.replace("Bearer ", "");

        headers.setBearerAuth(cleanToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<KeycloackUserDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    KeycloackUserDTO.class
            );

            return response.getBody();

        } catch (HttpClientErrorException e) {
            System.err.println("Status: " + e.getStatusCode());
            System.err.println("Response: " + e.getResponseBodyAsString());

            throw new Exception("Keycloak error: " + e.getResponseBodyAsString(), e);
        }
    }
}