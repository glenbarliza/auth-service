package com.microservices.auth.dto;

import com.microservices.auth.entities.Usuario;

import java.util.List;

public record UserDto(Long id, String name, String email, List<String> roles) {
    public static UserDto fromEntity(Usuario usuario) {
        return new UserDto(
                usuario.getId(),
                usuario.getName(),
                usuario.getEmail(),
                usuario.getRoles().stream()
                        .map(rol -> rol.getName())
                        .toList()
        );
    }
}
