package com.dircomercio.site_backend.services;

import com.dircomercio.site_backend.dtos.AudienciaCreateDTO;
import com.dircomercio.site_backend.entities.Audiencia;

import java.util.List;

public interface AudienciaService {
    Audiencia crearAudiencia(Audiencia audiencia);
    Audiencia crearAudienciaDesdeDTO(AudienciaCreateDTO dto);
    List<Audiencia> obtenerTodasLasAudiencias();
    Audiencia obtenerAudienciaPorId(Long id);
    Audiencia actualizarAudiencia(Long id, Audiencia datosActualizados);
    Audiencia actualizarAudienciaDesdeDTO(Long id, AudienciaCreateDTO dto);
    void eliminarAudiencia(Long id);
}
