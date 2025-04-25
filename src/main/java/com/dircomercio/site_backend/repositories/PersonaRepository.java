package com.dircomercio.site_backend.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.dircomercio.site_backend.entities.Persona;

public interface PersonaRepository extends CrudRepository<Persona, Long> {
    
    Optional<Persona> findByDocumento(String tipoDocumento, String documento);
}
