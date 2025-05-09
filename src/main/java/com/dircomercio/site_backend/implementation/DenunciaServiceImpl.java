package com.dircomercio.site_backend.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dircomercio.site_backend.entities.Denuncia;
import com.dircomercio.site_backend.entities.Persona;
import com.dircomercio.site_backend.repositories.DenunciaRepository;
import com.dircomercio.site_backend.services.DenunciaService;
import com.dircomercio.site_backend.services.DocumentoService;
import com.dircomercio.site_backend.services.PersonaService;

@Service
public class DenunciaServiceImpl implements DenunciaService{

    @Autowired
    DenunciaRepository denunciaRepository;

    @Autowired
    PersonaService personaService;

    @Autowired
    DocumentoService documentoService;

    @Override
    public void guardarDenuncia(Denuncia denuncia, List<MultipartFile> files) {

        if (denuncia == null) {
            throw new IllegalArgumentException("La denuncia no puede ser nula.");
        }
        if (files == null) {
            throw new IllegalArgumentException("Los archivos no pueden ser nulos.");
        }
        if (denuncia.getPersonas() == null) {
            throw new IllegalArgumentException("Las personas no deben ser nulas.");
        }

        try {
            List<Persona> personas = denuncia.getPersonas();
            personaService.guardarPersonas(personas, denuncia);
            documentoService.guardarDocumentos(files, denuncia);
            denunciaRepository.save(denuncia);
        } catch (Exception e) {
            e.getMessage();
            throw new RuntimeException("Ocurrió un error al guardar una nueva denuncia");
        }
    }

    
}
