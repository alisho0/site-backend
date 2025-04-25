package com.dircomercio.site_backend.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dircomercio.site_backend.entities.Denuncia;
import com.dircomercio.site_backend.entities.Persona;
import com.dircomercio.site_backend.repositories.PersonaRepository;
import com.dircomercio.site_backend.services.PersonaService;

public class PersonaServiceImpl implements PersonaService {

    @Autowired
    PersonaRepository personaRepository;

    @Override
    public void guardarPersonas(List<Persona> personas, Denuncia denuncia) {
        List<Persona> listaPersonas = new ArrayList<>();
        for (Persona persona : personas) {
            if (persona != null) {
                listaPersonas.add(persona);
                persona.setDenuncias(List.of(denuncia)); // Asignar la denuncia a la persona
            } else {
                System.out.println("Persona nula encontrada, no se guardará en la base de datos.");
            }
        }

        personaRepository.saveAll(listaPersonas);

    }

}
