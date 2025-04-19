package com.dircomercio.site_backend.repositories;

import org.springframework.data.repository.CrudRepository;

import com.dircomercio.site_backend.entities.Documento;

public interface DocumentoRepository extends CrudRepository<Documento, Long> {

}
