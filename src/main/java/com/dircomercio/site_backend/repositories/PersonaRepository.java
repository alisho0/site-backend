package com.dircomercio.site_backend.repositories;

import org.springframework.data.repository.CrudRepository;

import com.dircomercio.site_backend.entities.Persona;

public interface PersonaRepository extends CrudRepository<Persona, Long> {
    
}
