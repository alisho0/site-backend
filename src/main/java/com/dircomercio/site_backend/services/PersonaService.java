package com.dircomercio.site_backend.services;

import java.util.List;

import com.dircomercio.site_backend.dtos.PersonaRolDTO;
import com.dircomercio.site_backend.entities.Denuncia;
import com.dircomercio.site_backend.entities.Persona;

public interface PersonaService {

    List<Persona> guardarPersonas(List<PersonaRolDTO> personas);

    void modificarPersona(Long id, Persona persona);
}
