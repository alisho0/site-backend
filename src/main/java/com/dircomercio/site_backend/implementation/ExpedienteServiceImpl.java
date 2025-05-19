package com.dircomercio.site_backend.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.dircomercio.site_backend.entities.Expediente;
import com.dircomercio.site_backend.repositories.ExpedienteRepository;
import com.dircomercio.site_backend.services.ExpedienteService;

public class ExpedienteServiceImpl implements ExpedienteService {

    @Autowired
    ExpedienteRepository expedienteRepository;

    @Override
    public Expediente crearExpediente(Expediente expediente) {
        return expedienteRepository.save(expediente);
    }

    @Override
    public Expediente obtenerExpedientePorId(Long id) {
        Optional<Expediente> expediente = expedienteRepository.findById(id);
        return expediente.orElseThrow(() -> new RuntimeException("Expediente no encontrado"));
    }

    @Override
    public List<Expediente> listarExpedientes() {
        return (List<Expediente>) expedienteRepository.findAll();
    }

    @Override
    public Expediente actualizarExpediente(Long id, Expediente expediente) {
        Expediente existente = obtenerExpedientePorId(id);
        existente.setCant_folios(expediente.getCant_folios()); // Ejemplo de campo
        existente.setFecha_finalizacion(expediente.getFecha_finalizacion()); // Ejemplo de campo
        return expedienteRepository.save(existente);
    }

    @Override
    public void eliminarExpediente(Long id) {
        expedienteRepository.deleteById(id);
    }

}
