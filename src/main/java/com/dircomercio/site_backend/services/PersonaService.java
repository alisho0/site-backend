package com.dircomercio.site_backend.services;

import java.util.List;
import java.util.Optional;

import com.dircomercio.site_backend.entities.Denuncia;
import com.dircomercio.site_backend.entities.Persona;

public interface PersonaService {

    void guardarPersonas(List<Persona> personas, Denuncia denuncia);

    Optional<Persona> buscarPersonaPorId(Long id);

    List<Persona> buscarPersonasPorDenuncia(Long idDenuncia); // POR AHORA NO SE IMPLEMENTA

    List<Persona> listarPersonas();

    Optional<Persona> buscarPorDni(String tipoDocumento, String numeroDocumento);

    //Persona actualizarContacto(Long id, Persona nuevosDatos); PARA REVISAR

    void modificarPersona(Long id, Persona persona);
}
