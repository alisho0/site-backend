package com.dircomercio.site_backend.services;

import java.util.List;
import java.util.Optional;

import com.dircomercio.site_backend.entities.Denuncia;
import com.dircomercio.site_backend.entities.Persona;

public interface PersonaService {

    void guardarPersonas(List<Persona> personas, Denuncia denuncia);
    Optional<Persona> buscarPersonaPorId(Long id);
    List<Persona> buscarPersonasPorDenuncia(Long idDenuncia);
}
