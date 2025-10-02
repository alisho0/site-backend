package com.dircomercio.site_backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.dircomercio.site_backend.entities.Persona;

public interface PersonaRepository extends CrudRepository<Persona, Long> {
    
    @Query(value = "SELECT * FROM personas WHERE documento = :documento", nativeQuery = true)
    Optional<Persona> findByDocumento(@Param("documento") String documento);

    boolean existsByDocumento(String documento);
    boolean existsByEmail(String email);
}
