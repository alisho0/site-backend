package com.dircomercio.site_backend.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dircomercio.site_backend.dtos.ExpedienteCreateDTO;
import com.dircomercio.site_backend.dtos.ExpedienteCreateMinimalDTO;
import com.dircomercio.site_backend.entities.Denuncia;
import com.dircomercio.site_backend.entities.Expediente;
import com.dircomercio.site_backend.repositories.DenunciaRepository;
import com.dircomercio.site_backend.repositories.ExpedienteRepository;
import com.dircomercio.site_backend.repositories.UsuarioRepository;
import com.dircomercio.site_backend.services.ExpedienteService;

@Service
public class ExpedienteServiceImpl implements ExpedienteService {

    @Autowired
    private ExpedienteRepository expedienteRepository;

    @Autowired
    private DenunciaRepository denunciaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Crear expediente desde DTO completo
    @Override
    public Expediente crearExpedienteDesdeDTO(ExpedienteCreateDTO dto) {
        Optional<Denuncia> denunciaOpt = denunciaRepository.findById(dto.getId());
        if (denunciaOpt.isEmpty()) {
            throw new IllegalArgumentException("La denuncia no existe");
        }

        Denuncia denuncia = denunciaOpt.get();
        if (!"ACEPTADA".equalsIgnoreCase(denuncia.getEstado())) {
            throw new IllegalArgumentException("La denuncia no está aceptada");
        }

        Expediente expediente = new Expediente();
        expediente.setNro_exp(dto.getNroExp());
        expediente.setCant_folios(dto.getCantFolios());
        expediente.setFecha_inicio(dto.getFechaInicio());
        expediente.setFecha_finalizacion(dto.getFechaFinalizacion());
        expediente.setHipervulnerable(dto.getHipervulnerable());
        expediente.setDelegacion(dto.getDelegacion());
        expediente.setDenuncia(denuncia);

        return expedienteRepository.save(expediente);
    }

    // Crear expediente con datos mínimos
    @Override
    public Expediente crearExpedienteDesdeMinimalDTO(ExpedienteCreateMinimalDTO dto) {
        Expediente expediente = new Expediente();

        expediente.setNro_exp(dto.getNro_exp()); // 
        expediente.setCant_folios(null); // Se completará más adelante
        expediente.setFecha_inicio(null);
        expediente.setFecha_finalizacion(null);
        expediente.setHipervulnerable(null);
        expediente.setDelegacion(null);
        expediente.setDenuncia(null); // Se asociará después

        return expedienteRepository.save(expediente);
    }

    // Crear expediente a partir de una denuncia aceptada
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
        expediente.setNro_exp(null);
        expediente.setCant_folios(null);
        expediente.setFecha_inicio(null);
        expediente.setFecha_finalizacion(null);
        expediente.setHipervulnerable(null);
        expediente.setDelegacion(null);

        Expediente expedienteGuardado = expedienteRepository.save(expediente);

        // Asociar el expediente a la denuncia
        denuncia.setExpediente(expedienteGuardado);
        denunciaRepository.save(denuncia);

        return expedienteGuardado;
    }

    @Override
    public Expediente obtenerExpedientePorId(Long id) {
        return expedienteRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Expediente no encontrado con ID: " + id));
    }

    @Override
    public List<Expediente> listarExpedientes() {
        return (List<Expediente>) expedienteRepository.findAll();
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
            expediente.setDenuncia(expedienteActualizado.getDenuncia());
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
