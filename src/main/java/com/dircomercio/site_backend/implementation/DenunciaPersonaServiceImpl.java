package com.dircomercio.site_backend.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dircomercio.site_backend.dtos.PersonaRolDTO;
import com.dircomercio.site_backend.entities.Denuncia;
import com.dircomercio.site_backend.entities.DenunciaPersona;
import com.dircomercio.site_backend.repositories.DenunciaPersonaRepository;
import com.dircomercio.site_backend.services.DenunciaPersonaService;

@Service
public class DenunciaPersonaServiceImpl implements DenunciaPersonaService{

    @Autowired
    DenunciaPersonaRepository denunciaPersonaRepository;

    @Override
    public void vincularPersonaDenuncia(List<PersonaRolDTO> personas, Denuncia denuncia) {
        try {
            for (PersonaRolDTO personaRol : personas) {
                DenunciaPersona dp = DenunciaPersona.builder()
                .denuncia(denuncia)
                .persona(personaRol.getPersona())
                .rol(personaRol.getRol())
                .nombreDelegado(personaRol.getNombreDelegado())
                .apellidoDelegado(personaRol.getApellidoDelegado())
                .dniDelegado(personaRol.getDniDelegado())
                .build();

                denunciaPersonaRepository.save(dp);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al vincular personas a la denuncia", e);
        }
    }
    
}
