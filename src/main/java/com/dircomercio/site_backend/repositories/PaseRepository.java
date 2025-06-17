package com.dircomercio.site_backend.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.dircomercio.site_backend.entities.Expediente;
import com.dircomercio.site_backend.entities.Pase;

public interface PaseRepository extends CrudRepository<Pase, Long> {
    List<Pase> findByExpediente_Id(Long expedienteId);
}
