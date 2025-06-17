package com.dircomercio.site_backend.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.dircomercio.site_backend.entities.Rol;

public interface RolRepository extends CrudRepository<Rol, Long> {
    Optional<Rol> findByNombre(String nombre);
}
