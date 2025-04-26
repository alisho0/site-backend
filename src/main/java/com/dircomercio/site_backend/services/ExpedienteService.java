package com.dircomercio.site_backend.services;

import com.dircomercio.site_backend.models.Expediente;
import com.dircomercio.site_backend.repositories.ExpedienteRepository;
import com.dircomercio.site_backend.services.ExpedienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExpedienteService implements ExpedienteService {

    @Autowired
    private ExpedienteRepository expedienteRepository;

    @Override
    public Expedient crearExpediente(Expediente expediente) {
        return expedienteRepository.save(expediente);
    }

    @Override
    public Expediente obtenerExpedientePorId(Long id) {
        Optional<Expediente> expediente = expedienteRepository.findById(id);
        return expediente.orElseThrow(() -> new RuntimeException("Expediente no encontrado"));
    }

    @Override
    public List<Expediente> listarExpedientes() {
        return expedienteRepository.findAll();
    }

    @Override
    public Expediente actualizarExpediente(Long id, Expediente expediente) {
        Expediente existente = obtenerExpedientePorId(id);
        existente.setNombre(expediente.getNombre()); // Ejemplo de campo
        existente.setDescripcion(expediente.getDescripcion()); // Ejemplo de campo
        return expedienteRepository.save(existente);
    }

    @Override
    public void eliminarExpediente(Long id) {
        expedienteRepository.deleteById(id);
    }
}
