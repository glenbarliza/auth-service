package com.microservices.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class PasswordEncoderTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void testPasswordMatch() {
        String rawPassword = "admin123";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        System.out.println("Nuevo hash generado: " + encodedPassword);

        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword),
                "La contraseña no coincide");
    }
}
