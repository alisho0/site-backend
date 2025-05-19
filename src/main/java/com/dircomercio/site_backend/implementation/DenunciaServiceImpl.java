package com.dircomercio.site_backend.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dircomercio.site_backend.dtos.DenunciaDTO;
import com.dircomercio.site_backend.entities.Denuncia;
import com.dircomercio.site_backend.entities.DenunciaPersona;
import com.dircomercio.site_backend.entities.Persona;
import com.dircomercio.site_backend.repositories.DenunciaRepository;
import com.dircomercio.site_backend.services.DenunciaPersonaService;
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

    @Autowired
    DenunciaPersonaService dPersonaService;

    @Override
    public void guardarDenuncia(DenunciaDTO denunciaDTO, List<MultipartFile> files) {

        if (denunciaDTO == null) throw new IllegalArgumentException("La denuncia no puede ser nula.");
        if (files == null) throw new IllegalArgumentException("Los archivos no pueden ser nulos.");
        if (denunciaDTO.getPersonas() == null) throw new IllegalArgumentException("Las personas no deben ser nulas.");

        try {
            // Aquí se crea la denuncia
            Denuncia denuncia = new Denuncia();
            denuncia.setDescripcion(denuncia.getDescripcion());
            denuncia.setObjeto(denuncia.getObjeto());
            denuncia.setMotivo(denuncia.getMotivo());
            denunciaRepository.save(denuncia);

            // Aquí se crean (si es que no existen) y vinculan las personas a la denuncia
            List<Persona> personasPersistidas = personaService.guardarPersonas(denunciaDTO.getPersonas());
            dPersonaService.vincularPersonaDenuncia(denunciaDTO.getPersonas(), denuncia);

            // Aquí se guardan los documentos, sigue igual
            documentoService.guardarDocumentos(files, denuncia);

        } catch (Exception e) {
            e.getMessage();
            throw new RuntimeException("Ocurrió un error al guardar una nueva denuncia");
        }
    }

    @Override
    public List<Denuncia> traerDenuncias() throws NotFoundException {
        List<Denuncia> denuncias = new ArrayList<>();
        try {
            denuncias = (List<Denuncia>) denunciaRepository.findAll();
            return denuncias;
        } catch (Exception e) {
            e.printStackTrace();
            throw new NotFoundException();
        }
    }
}
