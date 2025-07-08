package com.dircomercio.site_backend.services;

import com.dircomercio.site_backend.dtos.ExpedienteCreateDTO;
import com.dircomercio.site_backend.entities.Expediente;
import com.dircomercio.site_backend.dtos.ExpedienteCreateMinimalDTO;
import com.dircomercio.site_backend.dtos.ExpedienteIdRespuestaDTO;
import com.dircomercio.site_backend.dtos.ExpedienteRespuestaDTO;
import com.dircomercio.site_backend.dtos.ExpedienteUpdateDTO;

import java.util.List;


public interface ExpedienteService {

    // Crear expediente directamente desde un DTO recibido por el controller
    Expediente crearExpedienteDesdeDTO(ExpedienteCreateDTO dto);

    // Crear expediente solo si la denuncia fue aceptada
    Expediente crearExpedienteDesdeDenuncia(Long idDenuncia) throws Exception;

    ExpedienteIdRespuestaDTO traerExpedientePorId(Long id);

    List<ExpedienteRespuestaDTO> listarExpedientes();

    Expediente actualizarExpediente(Long id, ExpedienteUpdateDTO dto);

    void eliminarExpediente(Long id);

    Expediente crearExpedienteDesdeMinimalDTO(ExpedienteCreateMinimalDTO dto) throws Exception;

    List<ExpedienteRespuestaDTO> listarExpedientesPorUsuario() throws Exception;
}
