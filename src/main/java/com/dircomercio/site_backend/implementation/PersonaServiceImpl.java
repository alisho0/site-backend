package com.dircomercio.site_backend.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dircomercio.site_backend.dtos.PersonaRolDTO;
import com.dircomercio.site_backend.entities.Persona;
import com.dircomercio.site_backend.repositories.DenunciaRepository;
import com.dircomercio.site_backend.repositories.PersonaRepository;
import com.dircomercio.site_backend.services.PersonaService;

@Service
public class PersonaServiceImpl implements PersonaService {

    @Autowired
    PersonaRepository personaRepository;

    @Autowired
    DenunciaRepository denunciaRepository;

    @Override
    public List<Persona> guardarPersonas(List<PersonaRolDTO> personas) {
        List<Persona> listaPersonas = new ArrayList<>();
        for (PersonaRolDTO personaRol : personas) {
            Persona persona = personaRol.getPersona();
            if (persona != null) {
                Persona existente = personaRepository.findByDocumento(persona.getDocumento()).orElse(null);
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
    public Optional<Persona> buscarPersonaPorId(Long id) {
        return personaRepository.findById(id);
    } 

    public List<Persona> traerPersonas() {
        return (List<Persona>) personaRepository.findAll();
    }

}
