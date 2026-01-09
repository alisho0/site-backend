package com.dircomercio.site_backend.repositories;

import com.dircomercio.site_backend.entities.AuditoriaLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditoriaLogRepository extends JpaRepository<AuditoriaLog, Long> {

    //metodos personalizados 

    //para buscar todos los logs de un usuario 
    List<AuditoriaLog> findByUsuario(String usuario);

    //para buscar todos los logs de una accion especifica
    List<AuditoriaLog> findByAccion(String accion);

    //para buscar todos los logs entre dos fechas
    List<AuditoriaLog> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);

    //para buscar logs que fallaraon
    List<AuditoriaLog> findByResultado(String resultado);

    //para buscar logs por entidad afectada
    List<AuditoriaLog> findByEntidadAfectada(String entidadAfectada);
}

