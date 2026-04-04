package com.salon.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KeycloakConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final String REALM_ACCESS = "realm_access";
    private static final String RESOURCE_ACCESS = "resource_access";
    private static final String ROLES = "roles";

    @Override
    @SuppressWarnings("unchecked")
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        // Default scope-based authorities (if any)
        Collection<GrantedAuthority> authorities = Stream.of(jwt)
                .map(Jwt::getClaims)
                .map(claims -> (Collection<String>) claims.getOrDefault("scope", ""))
                .flatMap(scope -> Stream.of(scope.toString().split(" ")))
                .filter(s -> !s.isBlank())
                .map(scope -> "SCOPE_" + scope)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // Extract realm roles (most common for your case)
        Map<String, Object> realmAccess = (Map<String, Object>) jwt.getClaims().get(REALM_ACCESS);
        if (realmAccess != null) {
            Collection<String> realmRoles = (Collection<String>) realmAccess.get(ROLES);
            if (realmRoles != null) {
                authorities.addAll(realmRoles.stream()
                        .map(role -> "ROLE_" + role.toUpperCase())  // Keycloak roles are usually uppercase, but safe
                        .map(SimpleGrantedAuthority::new)
                        .toList());
            }
        }

        // Optional: Extract client-specific roles (replace "your-client-id" with your actual client)
        Map<String, Object> resourceAccess = (Map<String, Object>) jwt.getClaims().get(RESOURCE_ACCESS);
        if (resourceAccess != null) {
            resourceAccess.forEach((clientId, clientAccess) -> {
                Map<String, Object> clientRolesMap = (Map<String, Object>) clientAccess;
                Collection<String> clientRoles = (Collection<String>) clientRolesMap.get(ROLES);
                if (clientRoles != null) {
                    authorities.addAll(clientRoles.stream()
                            .map(role -> "ROLE_" + role.toUpperCase())
                            .map(SimpleGrantedAuthority::new)
                            .toList());
                }
            });
        }

        return authorities;
    }
}