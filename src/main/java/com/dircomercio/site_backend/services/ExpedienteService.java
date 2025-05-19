package com.dircomercio.site_backend.services;

import com.dircomercio.site_backend.entities.Expediente;

import java.util.List;

public interface ExpedienteService {

    Expediente crearExpediente(Expediente expediente);

    Expediente obtenerExpedientePorId(Long id);

    List<Expediente> listarExpedientes();

    Expediente actualizarExpediente(Long id, Expediente expediente);

    void eliminarExpediente(Long id);
}
