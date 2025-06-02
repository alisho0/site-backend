package com.dircomercio.site_backend.services;

import com.dircomercio.site_backend.dtos.ExpedienteCreateDTO;
import com.dircomercio.site_backend.entities.Expediente;
import com.dircomercio.site_backend.dtos.ExpedienteCreateMinimalDTO;
import java.util.List;


public interface ExpedienteService {

    // Crear expediente directamente desde un DTO recibido por el controller
    Expediente crearExpedienteDesdeDTO(ExpedienteCreateDTO dto);

    // Crear expediente solo si la denuncia fue aceptada
    Expediente crearExpedienteDesdeDenuncia(Long idDenuncia) throws Exception;

    Expediente obtenerExpedientePorId(Long id);

    List<Expediente> listarExpedientes();

    Expediente actualizarExpediente(Long id, Expediente expediente);

    void eliminarExpediente(Long id);

    Expediente crearExpedienteDesdeMinimalDTO(ExpedienteCreateMinimalDTO dto) throws Exception;
}
