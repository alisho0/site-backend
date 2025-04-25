package com.dircomercio.site_backend.implementation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dircomercio.site_backend.entities.Denuncia;
import com.dircomercio.site_backend.entities.Persona;
import com.dircomercio.site_backend.repositories.DenunciaRepository;
import com.dircomercio.site_backend.services.DenunciaService;
import com.dircomercio.site_backend.services.PersonaService;

@Service
public class DenunciaServiceImpl implements DenunciaService{

    @Autowired
    DenunciaRepository denunciaRepository;

    @Autowired
    PersonaService personaService;

    @Override
    public void guardarDenuncia(Denuncia denuncia) {

        List<Persona> personas = denuncia.getPersonas();

        personaService.guardarPersonas(personas, denuncia);

        denuncia.setFecha(LocalDate.now());
        denuncia.setEstado("Recibida");
        denunciaRepository.save(denuncia);
    }

    @Override
    public Optional<Denuncia> buscarDenuncia(Long id) {
        return denunciaRepository.findById(id);
    }

    @Override
    public List<Denuncia> listarDenuncias() {
        return (List<Denuncia>) denunciaRepository.findAll();
    }

    @Override
    public Denuncia actualizarEstado(Long id, String estado) {
        Optional<Denuncia> denunciaOpt = denunciaRepository.findById(id);

        if (denunciaOpt.isPresent()) {
            Denuncia denuncia = denunciaOpt.get();
            denuncia.setEstado(estado);
            return denunciaRepository.save(denuncia);
        } else {
            throw new RuntimeException("Denuncia no encontrada para el ID: " + id); // Lanza una excepción si no se encuentra la denuncia
        }
    }

    @Override
    public List<Denuncia> listarPorPersona(String tipoDocumento, String documento) {
        Optional<Persona> personaOpt = personaService.buscarPorDni(tipoDocumento, documento);
            
            if (personaOpt.isPresent()) {
                Persona persona = personaOpt.get();
                return denunciaRepository.findByPersonaContaining(persona);
            } else {
                throw new RuntimeException("Persona no encontrada para el DNI: " + tipoDocumento + " " + documento); // Lanza una excepción si no se encuentra la persona
            }
    }

    
}
