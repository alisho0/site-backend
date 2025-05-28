package com.dircomercio.site_backend.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dircomercio.site_backend.entities.Denuncia;
import com.dircomercio.site_backend.entities.Expediente;
import com.dircomercio.site_backend.repositories.DenunciaRepository;
import com.dircomercio.site_backend.repositories.ExpedienteRepository;
import com.dircomercio.site_backend.services.ExpedienteService;

@Service
public class ExpedienteServiceImpl implements ExpedienteService {

    @Override
    public Expediente crearExpedienteDesdeDenuncia(Long denunciaId) {
        Optional<Denuncia> denunciaOpt = denunciaRepository.findById(denunciaId);
        if (denunciaOpt.isEmpty()) {
            throw new IllegalArgumentException("La denuncia no existe");
        }

        Denuncia denuncia = denunciaOpt.get();
        if (!"ACEPTADA".equalsIgnoreCase(denuncia.getEstado())) {
            throw new IllegalArgumentException("La denuncia no está aceptada");
        }

        Expediente expediente = new Expediente();
        expediente.setDenuncia(denuncia);
        return expedienteRepository.save(expediente);
    }

    @Autowired
    private ExpedienteRepository expedienteRepository;

    @Autowired
    private DenunciaRepository denunciaRepository;

    @Override
    public Expediente crearExpediente(Expediente expediente) {
        // Validar que la denuncia asociada esté aceptada
        if (expediente.getDenuncia() == null || expediente.getDenuncia().getId() == null) {
            throw new IllegalArgumentException("La denuncia asociada es obligatoria");
        }

        Optional<Denuncia> denunciaOpt = denunciaRepository.findById(expediente.getDenuncia().getId());
        if (denunciaOpt.isEmpty()) {
            throw new IllegalArgumentException("La denuncia no existe");
        }

        Denuncia denuncia = denunciaOpt.get();
        if (!"ACEPTADA".equalsIgnoreCase(denuncia.getEstado())) {
            throw new IllegalArgumentException("La denuncia no está aceptada");
        }

        // Asociar el expediente a la denuncia
        expediente.setDenuncia(denuncia);

        // Guardar el expediente
        return expedienteRepository.save(expediente);
    }

    @Override
    public Expediente obtenerExpedientePorId(Long id) {
        return expedienteRepository.findById(id).orElseThrow(() ->
            new IllegalArgumentException("Expediente no encontrado con ID: " + id));
    }

    @Override
    public List<Expediente> listarExpedientes() {
        return (List<Expediente>) expedienteRepository .findAll();
    }

    @Override
    public Expediente actualizarExpediente(Long id, Expediente expedienteActualizado) {
        return expedienteRepository.findById(id).map(expediente -> {
            expediente.setNro_exp(expedienteActualizado.getNro_exp());
            expediente.setCant_folios(expedienteActualizado.getCant_folios());
            expediente.setFecha_inicio(expedienteActualizado.getFecha_inicio());
            expediente.setFecha_finalizacion(expedienteActualizado.getFecha_finalizacion());
            expediente.setHipervulnerable(expedienteActualizado.getHipervulnerable());
            expediente.setDelegacion(expedienteActualizado.getDelegacion());
            expediente.setUsuario(expedienteActualizado.getUsuario());
            return expedienteRepository.save(expediente);
        }).orElseThrow(() -> new IllegalArgumentException("Expediente no encontrado con ID: " + id));
    }

    @Override
    public void eliminarExpediente(Long id) {
        if (!expedienteRepository.existsById(id)) {
            throw new IllegalArgumentException("Expediente no encontrado con ID: " + id);
        }
        expedienteRepository.deleteById(id);
    }
}
