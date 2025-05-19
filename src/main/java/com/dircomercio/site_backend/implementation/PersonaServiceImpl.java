package com.dircomercio.site_backend.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dircomercio.site_backend.dtos.PersonaRolDTO;
import com.dircomercio.site_backend.entities.Persona;
import com.dircomercio.site_backend.repositories.PersonaRepository;
import com.dircomercio.site_backend.services.PersonaService;

@Service
public class PersonaServiceImpl implements PersonaService {

    @Autowired
    PersonaRepository personaRepository;

    @Override
    public List<Persona> guardarPersonas(List<PersonaRolDTO> personas) {
        List<Persona> listaPersonas = new ArrayList<>();
        for (PersonaRolDTO personaRol : personas) {
            Persona persona = personaRol.getPersona();
            if (persona != null) {
                Persona existente = personaRepository.findByDocumento(persona.getDocumento());
                if (existente != null) {
                    listaPersonas.add(existente);
                } else {
                    listaPersonas.add(personaRepository.save(persona));
                }
            } else {
                System.out.println("Persona nula encontrada, no se guardará en la base de datos.");
            }
        }

        return listaPersonas;

    }

    @Override
    public void modificarPersona(Long id, Persona persona) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'modificarPersona'");
    }

}
