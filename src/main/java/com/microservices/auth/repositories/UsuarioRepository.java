package com.microservices.auth.repositories;

import com.microservices.auth.entities.Usuario;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @EntityGraph(attributePaths = "roles")
    Optional<Usuario> findByEmail(String email);
}
