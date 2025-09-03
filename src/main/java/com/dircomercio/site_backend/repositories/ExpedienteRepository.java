package com.dircomercio.site_backend.repositories;

import org.springframework.data.repository.CrudRepository;

import com.dircomercio.site_backend.entities.Expediente;
import java.util.List;

public interface ExpedienteRepository extends CrudRepository<Expediente, Long> {
    // Buscar expedientes por usuario asignado
    List<Expediente> findByUsuarios_Id(Long usuarioId);
    List<Expediente> findByUsuarios_Email(String email);
    int countByNroExpStartingWith(String prefijo);
    // Buscar el último número de expediente del año actual
    Expediente findFirstByNroExpStartingWithOrderByNroExpDesc(String prefijo);
}
