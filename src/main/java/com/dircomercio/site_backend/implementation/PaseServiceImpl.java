package com.dircomercio.site_backend.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dircomercio.site_backend.auth.config.AuthUtil;
import com.dircomercio.site_backend.dtos.PaseCreateDTO;
import com.dircomercio.site_backend.dtos.PaseRespuestaDTO;
import com.dircomercio.site_backend.entities.Expediente;
import com.dircomercio.site_backend.entities.Pase;
import com.dircomercio.site_backend.entities.Usuario;
import com.dircomercio.site_backend.repositories.ExpedienteRepository;
import com.dircomercio.site_backend.repositories.PaseRepository;
import com.dircomercio.site_backend.repositories.UsuarioRepository;
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
    private AuthUtil authUtil;

    public void usuarioTienePermisos(){};

    public void crearPase(PaseCreateDTO dto) {
        Pase pase = new Pase();
        pase.setAccion(dto.getAccion());
        pase.setFechaAccion(dto.getFechaAccion());
        pase.setAreaAccion(dto.getAreaAccion());
        pase.setTipoTramite(dto.getTipoTramite());
        pase.setDescripcion(dto.getDescripcion());
        Expediente expediente = expedienteRepository.findById(dto.getExpedienteId())
            .orElseThrow(() -> new IllegalArgumentException("No se encontró el expediente con ID: " + dto.getExpedienteId()));
        pase.setExpediente(expediente);
        if (dto.getUsuarioId() != null) {
            Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el usuario con ID: " + dto.getUsuarioId()));
            pase.setUsuario(usuario);
        } else{
            pase.setUsuario(null);
        }
        //pase.setUsuario(usuario);
        paseRepository.save(pase);
    }

    @Override
    public List<PaseRespuestaDTO> traerPases() {
        List<Pase> pases = (List<Pase>) paseRepository.findAll();
        List<PaseRespuestaDTO> respuesta = new ArrayList<>();
        for (Pase pase : pases) {
            PaseRespuestaDTO dto = new PaseRespuestaDTO();
            dto.setId(pase.getId());
            dto.setAccion(pase.getAccion());
            dto.setFechaAccion(pase.getFechaAccion());
            dto.setAreaAccion(pase.getAreaAccion());
            dto.setTipoTramite(pase.getTipoTramite());
            dto.setDescripcion(pase.getDescripcion());
            dto.setNroExpediente(pase.getExpediente() != null ? pase.getExpediente().getNroExp() : null);
            dto.setNombreUsuario(pase.getUsuario() != null ? pase.getUsuario().getNombre() : null);
            respuesta.add(dto);
        }
        return respuesta;
    }

    @Override
    public PaseRespuestaDTO traerPasePorId(Long id) {
        Pase pase = paseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el pase con ID: " + id));
        PaseRespuestaDTO dto = new PaseRespuestaDTO();
        dto.setId(pase.getId());
        dto.setAccion(pase.getAccion());
        dto.setFechaAccion(pase.getFechaAccion());
        dto.setAreaAccion(pase.getAreaAccion());
        dto.setTipoTramite(pase.getTipoTramite());
        dto.setDescripcion(pase.getDescripcion());
        dto.setNroExpediente(pase.getExpediente() != null ? pase.getExpediente().getNroExp() : null);
        dto.setNombreUsuario(pase.getUsuario() != null ? pase.getUsuario().getNombre() : null);
        return dto;
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
        try {
        List<Pase> pases = paseRepository.findByExpediente_Id(expedienteId);
        List<PaseRespuestaDTO> respuesta = new ArrayList<>();
        for (Pase pase : pases) {
            PaseRespuestaDTO dto = new PaseRespuestaDTO();
            dto.setId(pase.getId());
            dto.setAccion(pase.getAccion());
            dto.setFechaAccion(pase.getFechaAccion());
            dto.setAreaAccion(pase.getAreaAccion());
            dto.setTipoTramite(pase.getTipoTramite());
            dto.setDescripcion(pase.getDescripcion());
            dto.setNroExpediente(pase.getExpediente() != null ? pase.getExpediente().getNroExp() : null);
            dto.setNombreUsuario(pase.getUsuario() != null ? pase.getUsuario().getNombre() : null);
            respuesta.add(dto);
        }
        return respuesta;
        }
         catch (Exception e) {
            throw new IllegalArgumentException("No se encontraron pases para el expediente con ID: " + e.getMessage());
        }};

    @Override
    public PaseRespuestaDTO editarPase(Long id, PaseCreateDTO dto) {
        Pase pase = paseRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No se encontró el pase con ID: " + id));
        pase.setAccion(dto.getAccion());
        pase.setFechaAccion(dto.getFechaAccion());
        pase.setAreaAccion(dto.getAreaAccion());
        pase.setTipoTramite(dto.getTipoTramite());
        pase.setDescripcion(dto.getDescripcion());
        if (dto.getExpedienteId() != null) {
            Expediente expediente = expedienteRepository.findById(dto.getExpedienteId())
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el expediente con ID: " + dto.getExpedienteId()));
            pase.setExpediente(expediente);
        }
        if (dto.getUsuarioId() != null) {
            Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el usuario con ID: " + dto.getUsuarioId()));
            pase.setUsuario(usuario);
        } else {
            pase.setUsuario(null);
        }
        paseRepository.save(pase);
        PaseRespuestaDTO respuesta = new PaseRespuestaDTO();
        respuesta.setId(pase.getId());
        respuesta.setAccion(pase.getAccion());
        respuesta.setFechaAccion(pase.getFechaAccion());
        respuesta.setAreaAccion(pase.getAreaAccion());
        respuesta.setTipoTramite(pase.getTipoTramite());
        respuesta.setDescripcion(pase.getDescripcion());
        respuesta.setNroExpediente(pase.getExpediente() != null ? pase.getExpediente().getNroExp() : null);
        respuesta.setNombreUsuario(pase.getUsuario() != null ? pase.getUsuario().getNombre() : null);
        return respuesta;
    }
}
