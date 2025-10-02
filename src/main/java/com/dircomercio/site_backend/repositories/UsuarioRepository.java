package com.dircomercio.site_backend.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.dircomercio.site_backend.entities.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

    // Usar método de Spring Data JPA estándar para evitar problemas de mapeo
    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

}
