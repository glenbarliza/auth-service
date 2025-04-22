package com.microservices.auth;

import com.microservices.auth.entities.Rol;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestLombok {
    public static void main(String[] args) {

        // Usando el builder generado por Lombok
        Rol rol = Rol.builder()
                .id(1L)
                .name("ROLE_USER")
                .build();

        // Usando getters generados por Lombok
        System.out.println("ID: " + rol.getId());
        System.out.println("Nombre: " + rol.getName());

        // Usando setters generados por Lombok
        rol.setName("ROLE_ADMIN");
        System.out.println("Nuevo nombre: " + rol.getName());
    }

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void testPassword() {
        String raw = "user123";
        String encoded = "$2a$10$CSdOy46Jc2QShzqFzY6uJe3rpPUF2UoUSlP38Jz7NeYuQjCEtDFNe";
        assertTrue(passwordEncoder.matches(raw, encoded));
    }
}
