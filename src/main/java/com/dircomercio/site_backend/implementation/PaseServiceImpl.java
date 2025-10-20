package com.dircomercio.site_backend.implementation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dircomercio.site_backend.dtos.PaseCreateDTO;
import com.dircomercio.site_backend.dtos.PaseRespuestaDTO;
import com.dircomercio.site_backend.entities.Denuncia;
import com.dircomercio.site_backend.entities.Expediente;
import com.dircomercio.site_backend.entities.Pase;
import com.dircomercio.site_backend.entities.TipoDocumento;
import com.dircomercio.site_backend.entities.Usuario;
import com.dircomercio.site_backend.repositories.ExpedienteRepository;
import com.dircomercio.site_backend.repositories.PaseRepository;
import com.dircomercio.site_backend.repositories.UsuarioRepository;
import com.dircomercio.site_backend.services.DocumentoService;
import com.dircomercio.site_backend.services.PaseService;

@Service
public class PaseServiceImpl implements PaseService {

    @Autowired
    private PaseRepository paseRepository;
    @Autowired
    private ExpedienteRepository expedienteRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private DocumentoService documentoService;

    @Override
    public void crearPase(PaseCreateDTO dto, List<MultipartFile> file) {
        Pase pase = new Pase();
        pase.setAsunto(dto.getAsunto());
        pase.setCantFolios(dto.getCantFolios());
        pase.setFechaAccion(LocalDate.now());
        pase.setAreaOrigen(dto.getAreaOrigen());
        pase.setAreaDestino(dto.getAreaDestino());
        pase.setDescripcion(dto.getDescripcion());
        
        Expediente expediente = expedienteRepository.findById(dto.getExpedienteId())
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el expediente con ID: " + dto.getExpedienteId()));
        Denuncia denuncia = expediente.getDenuncia();
        pase.setExpediente(expediente);

        if (dto.getUsuarioId() != null) {
            Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                    .orElseThrow(() -> new IllegalArgumentException("No se encontró el usuario con ID: " + dto.getUsuarioId()));
            pase.setUsuario(usuario);
        }

        try {
            Pase paseGuardado = paseRepository.save(pase);
            if (file != null && !file.isEmpty()) {
                // ✅ CORRECCIÓN: Se asigna directamente el Enum, sin convertir.
                TipoDocumento tipoDoc = dto.getTipoDocumento();
                documentoService.guardarDocumentos(file, denuncia, paseGuardado, tipoDoc);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el pase: " + e.getMessage(), e);
        }
    }

    @Override
    public PaseRespuestaDTO editarPase(Long id, PaseCreateDTO dto, List<MultipartFile> file) {
        Pase pase = paseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el pase con ID: " + id));

        pase.setAsunto(dto.getAsunto());
        pase.setCantFolios(dto.getCantFolios());
        pase.setAreaOrigen(dto.getAreaOrigen());
        pase.setAreaDestino(dto.getAreaDestino());
        pase.setFechaAccion(LocalDate.now());
        pase.setDescripcion(dto.getDescripcion());
        
        if (dto.getUsuarioId() != null) {
            Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                    .orElseThrow(() -> new IllegalArgumentException("No se encontró el usuario con ID: " + dto.getUsuarioId()));
            pase.setUsuario(usuario);
        }
        
        Pase paseGuardado = paseRepository.save(pase);

        try {
            if (file != null && !file.isEmpty()) {
                Denuncia denuncia = paseGuardado.getExpediente().getDenuncia();
                // ✅ CORRECCIÓN: Se asigna directamente el Enum, sin convertir.
                TipoDocumento tipoDoc = dto.getTipoDocumento();
                documentoService.guardarDocumentos(file, denuncia, paseGuardado, tipoDoc);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar documentos durante la edición: " + e.getMessage(), e);
        }
        
        return convertirAPaseRespuestaDTO(paseGuardado);
    }
    
    // --- MÉTODOS RESTANTES COMPLETOS (SIN CAMBIOS) ---

    @Override
    public List<PaseRespuestaDTO> traerPases() {
        List<Pase> pases = (List<Pase>) paseRepository.findAll();
        return pases.stream().map(this::convertirAPaseRespuestaDTO).collect(Collectors.toList());
    }

    @Override
    public PaseRespuestaDTO traerPasePorId(Long id) {
        Pase pase = paseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el pase con ID: " + id));
        return convertirAPaseRespuestaDTO(pase);
    }

    @Override
    public void eliminarPase(Long id) {
        if (!paseRepository.existsById(id)) {
            throw new IllegalArgumentException("No se encontró el pase con ID: " + id);
        }
        paseRepository.deleteById(id);
    }
    
    @Override
    public List<PaseRespuestaDTO> traerPasesPorExpediente(Long expedienteId) {
        List<Pase> pases = paseRepository.findByExpediente_Id(expedienteId);
        return pases.stream().map(this::convertirAPaseRespuestaDTO).collect(Collectors.toList());
    }
    
    private PaseRespuestaDTO convertirAPaseRespuestaDTO(Pase pase) {
        PaseRespuestaDTO dto = new PaseRespuestaDTO();
        dto.setId(pase.getId());
        dto.setAsunto(pase.getAsunto());
        dto.setCantFolios(pase.getCantFolios());
        dto.setFechaAccion(pase.getFechaAccion());
        dto.setAreaOrigen(pase.getAreaOrigen());
        dto.setAreaDestino(pase.getAreaDestino());
        dto.setDescripcion(pase.getDescripcion());
        dto.setNroExpediente(pase.getExpediente() != null ? pase.getExpediente().getNroExp() : null);
        dto.setNombreUsuario(pase.getUsuario() != null ? pase.getUsuario().getNombre() : null);
        return dto;
    }
}

