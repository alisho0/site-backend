package com.dircomercio.site_backend.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.dircomercio.site_backend.entities.Denuncia;
import com.dircomercio.site_backend.entities.Persona;

public interface DenunciaRepository extends CrudRepository<Denuncia, Long> {
    List<Denuncia> findByPersonaContaining(Persona persona);
}
