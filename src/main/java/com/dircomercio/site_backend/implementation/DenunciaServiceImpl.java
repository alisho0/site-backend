package com.dircomercio.site_backend.implementation;

import java.util.List;

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

        personaService.guardarPersonas(null, denuncia);
    }

    
}
