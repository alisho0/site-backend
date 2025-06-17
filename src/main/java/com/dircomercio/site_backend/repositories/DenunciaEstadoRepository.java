package com.dircomercio.site_backend.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.dircomercio.site_backend.entities.DenunciaEstado;
import com.dircomercio.site_backend.entities.Denuncia;

public interface DenunciaEstadoRepository extends CrudRepository<DenunciaEstado, Long> {
    List<DenunciaEstado> findByDenunciaOrderByFechaAsc(Denuncia denuncia);
}
