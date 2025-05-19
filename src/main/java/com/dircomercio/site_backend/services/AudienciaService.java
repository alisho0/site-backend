package com.dircomercio.site_backend.services;

import com.dircomercio.site_backend.entities.Audiencia;

import java.util.List;

public interface AudienciaService {
    Audiencia crearAudiencia(Audiencia audiencia);
    List<Audiencia> obtenerTodasLasAudiencias();
    Audiencia obtenerAudienciaPorId(Long id);
    Audiencia actualizarAudiencia(Long id, Audiencia datosActualizados);
    void eliminarAudiencia(Long id);
}
