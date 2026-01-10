package com.dircomercio.site_backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query; // <-- Importante
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param; // <-- Importante

import com.dircomercio.site_backend.entities.Documento;

public interface DocumentoRepository extends CrudRepository<Documento, Long> {

    List<Documento> findAllByDenunciaId(Long denunciaId);

    // 🚀 NUEVO MÉTODO PARA EL ZIP
    // Busca los documentos asociados a la denuncia que pertenece al expediente dado.
    @Query("SELECT d FROM Documento d WHERE d.denuncia.id = (SELECT e.denuncia.id FROM Expediente e WHERE e.id = :expedienteId)")
    List<Documento> buscarPorExpedienteId(@Param("expedienteId") Long expedienteId);

}