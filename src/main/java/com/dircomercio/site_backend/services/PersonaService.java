package com.dircomercio.site_backend.services;

import java.util.List;

import com.dircomercio.site_backend.entities.Denuncia;
import com.dircomercio.site_backend.entities.Persona;

public interface PersonaService {

    void guardarPersonas(List<Persona> personas, Denuncia denuncia);

    void modificarPersona(Long id, Persona persona);
}
