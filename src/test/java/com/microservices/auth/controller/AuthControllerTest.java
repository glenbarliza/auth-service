package com.microservices.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.auth.dto.LoginRequest;
import com.microservices.auth.dto.RegisterRequest;
import com.microservices.auth.entities.Usuario;
import com.microservices.auth.repositories.UsuarioRepository;
import com.microservices.auth.security.JwtService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UsuarioRepository usuarioRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    void testLoginSuccess() throws Exception {
        LoginRequest request = new LoginRequest("test@example.com", "password123");
        Usuario usuario = Usuario.builder()
                .email("test@example.com")
                .password("encodedPass")
                .roles(Set.of())
                .build();

        Mockito.when(usuarioRepository.findByEmail("test@example.com")).thenReturn(Optional.of(usuario));
        Mockito.when(jwtService.generateToken(usuario)).thenReturn("mocked-token");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked-token"));
    }

    @Test
    void testLoginUserNotFound() throws Exception {
        LoginRequest request = new LoginRequest("missing@example.com", "password123");

        Mockito.when(usuarioRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testRegisterSuccess() throws Exception {
        RegisterRequest request = new RegisterRequest(
                "New User",
                "newuser@example.com",
                "securepass",
                List.of("ROLE_USER")
        );

        Mockito.when(usuarioRepository.findByEmail("newuser@example.com"))
                .thenReturn(Optional.empty());

        Usuario nuevo = Usuario.builder()
                .name(request.name())
                .email(request.email())
                .password("encodedPass")
                .roles(Set.of())
                .build();

        Mockito.when(usuarioRepository.save(Mockito.any(Usuario.class)))
                .thenReturn(nuevo);

        Mockito.when(jwtService.generateToken(Mockito.any(Usuario.class)))
                .thenReturn("generated-jwt-token");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("generated-jwt-token"));
    }

    @Test
    void testRegisterConflict() throws Exception {
        RegisterRequest request = new RegisterRequest(
                "Existing User",
                "existing@example.com",
                "securepass",
                List.of("ROLE_USER")
        );

        Mockito.when(usuarioRepository.findByEmail("existing@example.com"))
                .thenReturn(Optional.of(new Usuario()));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

}
