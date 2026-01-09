package com.dircomercio.site_backend.services;

import com.dircomercio.site_backend.entities.AuditoriaLog;
import com.dircomercio.site_backend.repositories.AuditoriaLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditoriaService {
    @Autowired
    private AuditoriaLogRepository auditoriaLogRepository;

    /**
     * Registra una actividad en auditoría
     * @param usuario Email del usuario (ej: juan@sde.gob.ar)
     * @param accion Tipo de acción (ej: LOGIN, CREATE, DELETE)
     * @param descripcion Detalles específicos de la acción
     * @param ipOrigen IP de origen de la petición
     * @param resultado SUCCESS o FAILURE
     * @param entidadAfectada Entidad modificada (EXPEDIENTE, DOCUMENTO, etc)
     * @param idEntidad ID del registro afectado
     */
    public void registrarAccion(
        String usuario, 
        String accion, 
        String descripcion, 
        String ipOrigen,
        String resultado, 
        String entidadAfectada, 
        Long idEntidad
    ) {
        AuditoriaLog log = AuditoriaLog.builder()  //objeto.builder se creae
            .usuario(usuario)
            .accion(accion)
            .descripcion(descripcion)
            .ipOrigen(ipOrigen)
            .fechaHora(LocalDateTime.now())
            .resultado(resultado)
            .entidadAfectada(entidadAfectada)
            .idEntidad(idEntidad)
            .build();

        auditoriaLogRepository.save(log); //se guarda en la bd
    }

    //para obtener los logs de los users
    public List<AuditoriaLog> obtenerLogsPorUsuario(String usuario) {
        return auditoriaLogRepository.findByUsuario(usuario);
    }

    //para obtener todos los logs de una accion (ejemplo todos los deltes)
    public List<AuditoriaLog> obtenerLogsPorAccion(String accion) {
        return auditoriaLogRepository.findByAccion(accion);
    }

    // para obtener los logs de un rango de fechas (los de hoy por ej)
    public List<AuditoriaLog> obtenerLogsPorRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        return auditoriaLogRepository.findByFechaHoraBetween(inicio, fin);
    }

    //para obtener los logs que fallaron
    public List<AuditoriaLog> obtenerLogsFallidos() {
        return auditoriaLogRepository.findByResultado("FAILURE");
    }

    //para obtener todos los cambios en una entidad especifico (expediente por ejemplo)
    public List<AuditoriaLog> obtenerLogsPorEntidad(String entidadAfectada) {
        return auditoriaLogRepository.findByEntidadAfectada(entidadAfectada);
    }

    // para obtener todos los logs registrados
    public List<AuditoriaLog> obtenerTodosLosLogs() {
        return auditoriaLogRepository.findAll();
    }

    // para obtener logs por rango de fechas (alias para el método anterior)
    public List<AuditoriaLog> obtenerLogsPorFecha(LocalDateTime inicio, LocalDateTime fin) {
        return auditoriaLogRepository.findByFechaHoraBetween(inicio, fin);
    }
}
                    
     