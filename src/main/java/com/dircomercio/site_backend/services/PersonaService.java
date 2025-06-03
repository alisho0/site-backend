package com.dircomercio.site_backend.services;

import java.util.List;
import java.util.Optional;

import com.dircomercio.site_backend.dtos.PersonaRolDTO;
import com.dircomercio.site_backend.entities.Persona;

public interface PersonaService {

    List<Persona> guardarPersonas(List<PersonaRolDTO> personas);

    Optional<Persona> buscarPersonaPorId(Long id);
    //Persona actualizarContacto(Long id, Persona nuevosDatos); PARA REVISAR


    List<Persona> traerPersonas();
}
