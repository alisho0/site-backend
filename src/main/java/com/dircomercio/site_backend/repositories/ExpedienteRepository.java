package com.dircomercio.site_backend.repositories;

import org.springframework.data.repository.CrudRepository;

import com.dircomercio.site_backend.entities.Expediente;

public interface ExpedienteRepository extends CrudRepository<Expediente, Long> {
    

}
