package com.microservices.auth.controller;

import com.microservices.auth.client.UserServiceClient;
import com.microservices.auth.dto.LoginRequest;
import com.microservices.auth.dto.RegisterRequest;
import com.microservices.auth.dto.UserProfileDto;
import com.microservices.auth.entities.Rol;
import com.microservices.auth.entities.Usuario;
import com.microservices.auth.repositories.RolRepository;
import com.microservices.auth.repositories.UsuarioRepository;
import com.microservices.auth.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;
    private final RolRepository rolRepository;

    private final UserServiceClient userServiceClient;

    @Operation(summary = "Login with email and password",
            description = "Returns a JWT if the credentials are valid")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        log.info("🔐 Login attempt for email: {}", request.email());
        UsernamePasswordAuthenticationToken a =   new UsernamePasswordAuthenticationToken(request.email(), request.password());
        authenticationManager.authenticate(a);

        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("User no found with the given credentials."));

        String token = jwtService.generateToken(usuario);
        log.debug("Generated token: {}", token);
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/register")
    @Operation(summary = "Register new user", description = "Creates a user and returns a JWT")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (usuarioRepository.findByEmail(request.email()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("This email is already in use.");
        }

        Set<Rol> roles = request.roles().stream()
                .map(rol -> rolRepository.findByName(rol)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + rol)))
                .collect(Collectors.toSet());

        Usuario nuevo = Usuario.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .roles(roles)
                .build();

        usuarioRepository.save(nuevo);

        // 👇 Llamar al user-service para crear perfil
        userServiceClient.sendProfile(new UserProfileDto(
                nuevo.getName(),
                nuevo.getEmail()
        ));

        String token = jwtService.generateToken(nuevo);
        return ResponseEntity.ok(Map.of("token", token));
    }


}

