package com.dircomercio.site_backend.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.dircomercio.site_backend.dtos.PaseCreateDTO;
import com.dircomercio.site_backend.dtos.PaseRespuestaDTO;

public interface PaseService {

    void crearPase(PaseCreateDTO pase, List<MultipartFile> file);
    
    List<PaseRespuestaDTO> traerPases();
    
    PaseRespuestaDTO traerPasePorId(Long id); 
    
    void eliminarPase(Long id);
    
    List<PaseRespuestaDTO> traerPasesPorExpediente(Long expedienteId);
    
    // ✅ CAMBIO CLAVE: Se añade el parámetro para el archivo, igual que en crearPase
    PaseRespuestaDTO editarPase(Long id, PaseCreateDTO dto, List<MultipartFile> file);
}
