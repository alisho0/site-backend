package com.dircomercio.site_backend.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.dircomercio.site_backend.entities.Persona;

public interface PersonaRepository extends CrudRepository<Persona, Long> {
    
    @Query(value = "SELECT * FROM personas WHERE documento = :documento", nativeQuery = true)
    Persona findByDocumento(@Param("documento") String documento);
}
