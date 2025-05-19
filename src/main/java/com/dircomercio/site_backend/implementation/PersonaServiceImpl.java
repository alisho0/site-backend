package com.dircomercio.site_backend.implementation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dircomercio.site_backend.entities.Denuncia;
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

    @Override
    public Optional<Persona> buscarPersonaPorId(Long id) {
        return personaRepository.findById(id);
    } 

    @Override
    public List<Persona> buscarPersonasPorDenuncia(Long idDenuncia) {
        return denunciaRepository.findById(idDenuncia)
                .map(Denuncia::getPersonas)
                .orElse(Collections.emptyList());
    }

    @Override
    public List<Persona> listarPersonas() {
        return (List<Persona>) personaRepository.findAll();
    }

    @Override
    public Optional<Persona> buscarPorDni(String tipoDocumento, String numeroDocumento) {
        return personaRepository.findByDocumento(tipoDocumento, numeroDocumento);
    }

    @Override
    public void modificarPersona(Long id, Persona persona) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'modificarPersona'");
    }

}
