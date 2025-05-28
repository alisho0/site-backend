package com.dircomercio.site_backend.controllers;

import com.dircomercio.site_backend.dtos.ExpedienteCreateDTO;
import com.dircomercio.site_backend.entities.Denuncia;
import com.dircomercio.site_backend.entities.Expediente;
import com.dircomercio.site_backend.entities.Usuario;
import com.dircomercio.site_backend.repositories.DenunciaRepository;
import com.dircomercio.site_backend.repositories.ExpedienteRepository;
import com.dircomercio.site_backend.repositories.UsuarioRepository;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/expediente")
public class ExpedienteController {

    @Autowired
    private DenunciaRepository denunciaRepository;

    @Autowired
    private ExpedienteRepository expedienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity<?> crearExpediente(@Valid @RequestBody ExpedienteCreateDTO dto) {
        try {
            Optional<Denuncia> denunciaOpt = denunciaRepository.findById(dto.getId());

            if (denunciaOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Denuncia no encontrada");
            }

            Denuncia denuncia = denunciaOpt.get();

            if (!"ACEPTADA".equalsIgnoreCase(denuncia.getEstado())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La denuncia no fue aceptada");
            }

            Expediente expediente = new Expediente();
            expediente.setNro_exp(dto.getNroExp());
            expediente.setCant_folios(dto.getCantFolios());
            expediente.setFecha_inicio(dto.getFechaInicio());
            expediente.setFecha_finalizacion(dto.getFechaFinalizacion());
            expediente.setHipervulnerable(dto.getHipervulnerable());
            expediente.setDelegacion(dto.getDelegacion());

            if (dto.getUsuarioId() != null) {
                Optional<Usuario> usuarioOpt = usuarioRepository.findById(dto.getUsuarioId());
                if (usuarioOpt.isPresent()) {
                    expediente.setUsuario(usuarioOpt.get());
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuario no encontrado");
                }
            }

            expediente = expedienteRepository.save(expediente);

            denuncia.setExpediente(expediente);
            denunciaRepository.save(denuncia);

            return ResponseEntity.status(HttpStatus.CREATED).body(expediente);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el expediente: " + e.getMessage());
        }
    }
}

