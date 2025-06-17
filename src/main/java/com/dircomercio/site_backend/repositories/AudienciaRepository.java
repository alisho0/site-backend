package com.dircomercio.site_backend.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.dircomercio.site_backend.entities.Audiencia;

public interface AudienciaRepository extends CrudRepository<Audiencia, Long> {
    List<Audiencia> findByExpediente_Id(Long expedienteId);
}
