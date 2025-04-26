package com.dircomercio.site_backend.implementation;

import com.dircomercio.site_backend.entities.Audiencia;
import com.dircomercio.site_backend.exceptions.RecursoNoEncontradoException;
import com.dircomercio.site_backend.repositories.AudienciaRepository;
import com.dircomercio.site_backend.services.AudienciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AudienciaServiceImpl implements AudienciaService {

    @Autowired
    private AudienciaRepository audienciaRepository;

    @Override
    public Audiencia crearAudiencia(Audiencia audiencia) {
        if (audiencia == null || audiencia.getFecha() == null || audiencia.getLugar() == null) {
            throw new IllegalArgumentException("La audiencia no puede ser nula y debe tener fecha y lugar");
        }
        return audienciaRepository.save(audiencia);
    }

    @Override
    public List<Audiencia> obtenerTodasLasAudiencias() {
        return (List<Audiencia>) audienciaRepository.findAll();
    }

    @Override
    public Audiencia obtenerAudienciaPorId(Long id) {
        return audienciaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Audiencia no encontrada con id: " + id));
    }

    @Override
    public Audiencia actualizarAudiencia(Long id, Audiencia datosActualizados) {
        Audiencia audiencia = obtenerAudienciaPorId(id);
        if (datosActualizados.getFecha() != null) {
            audiencia.setFecha(datosActualizados.getFecha());
        }
        if (datosActualizados.getLugar() != null) {
            audiencia.setLugar(datosActualizados.getLugar());
        }
        if (datosActualizados.getHora() != null) {
            audiencia.setHora(datosActualizados.getHora());
        }
        if (datosActualizados.getPersona() != null) {
            audiencia.setPersona(datosActualizados.getPersona());
        }
        return audienciaRepository.save(audiencia);
    }

    @Override
    public void eliminarAudiencia(Long id) {
        if (!audienciaRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("No se puede eliminar la audiencia con id " + id);
        }
        audienciaRepository.deleteById(id);
    }
}