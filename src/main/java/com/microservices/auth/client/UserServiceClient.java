package com.microservices.auth.client;

import com.microservices.auth.dto.UserProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class UserServiceClient {

    @Value("${userservice.url}")
    private String userServiceUrl;

    @Value("${internal.token}")
    private String internalToken;

    private final RestTemplate restTemplate;

    public void sendProfile(UserProfileDto dto) {
        String url = userServiceUrl + "/users/internal/users";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Internal-Token", internalToken);
        HttpEntity<UserProfileDto> request = new HttpEntity<>(dto, headers);

        try {
            restTemplate.postForEntity(url, request, Void.class);
            System.out.println("✅ Perfil enviado a user-service: " + dto.email());
        } catch (Exception e) {
            System.err.println("⚠️ Error al enviar perfil a user-service: " + e.getMessage());
        }
    }
}
