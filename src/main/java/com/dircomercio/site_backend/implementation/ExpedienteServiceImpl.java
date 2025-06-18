package com.dircomercio.site_backend.implementation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dircomercio.site_backend.auth.config.AuthUtil;
import com.dircomercio.site_backend.dtos.DenunciaRespuestaDTO;
import com.dircomercio.site_backend.dtos.ExpedienteCreateDTO;
import com.dircomercio.site_backend.dtos.ExpedienteCreateMinimalDTO;
import com.dircomercio.site_backend.dtos.ExpedienteIdRespuestaDTO;
import com.dircomercio.site_backend.dtos.ExpedienteRespuestaDTO;
import com.dircomercio.site_backend.dtos.PersonaConRolDTO;
import com.dircomercio.site_backend.entities.Denuncia;
import com.dircomercio.site_backend.entities.DenunciaPersona;
import com.dircomercio.site_backend.entities.Expediente;
import com.dircomercio.site_backend.entities.Usuario;
import com.dircomercio.site_backend.repositories.DenunciaRepository;
import com.dircomercio.site_backend.repositories.ExpedienteRepository;
import com.dircomercio.site_backend.services.ExpedienteService;

@Service
public class ExpedienteServiceImpl implements ExpedienteService {

    @Autowired
    private ExpedienteRepository expedienteRepository;

    @Autowired
    private DenunciaRepository denunciaRepository;

    @Autowired
    private AuthUtil authUtil;

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
        //expediente.setFecha_inicio(dto.getFechaInicio());
        //expediente.setFecha_finalizacion(dto.getFechaFinalizacion());
        //expediente.setHipervulnerable(dto.getHipervulnerable());
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
        if (!"EN PROCESO".equalsIgnoreCase(denuncia.getEstado())) {
            throw new IllegalArgumentException("La denuncia no está aceptada");
        }

        Expediente expediente = new Expediente();
        expediente.setFecha_inicio(LocalDate.now());
        // expediente.setDenuncia(denuncia);
        // expediente.setNro_exp(null);
        expediente.setCant_folios("0");
        // expediente.setFecha_finalizacion(null);
        // expediente.setHipervulnerable(null);
        // expediente.setDelegacion(null);

        Expediente expedienteGuardado = expedienteRepository.save(expediente);

        // Asociar el expediente a la denuncia
        denuncia.setExpediente(expedienteGuardado);
        denunciaRepository.save(denuncia);

        return expedienteGuardado;
    }

    @Override
    public ExpedienteIdRespuestaDTO traerExpedientePorId(Long id) {
        try {
            Expediente expediente = expedienteRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Expediente no encontrado con ID: " + id));
            Denuncia denuncia = denunciaRepository.findById(expediente.getDenuncia().getId()).orElseThrow();
            ExpedienteIdRespuestaDTO respuestaDto = new ExpedienteIdRespuestaDTO();

            DenunciaRespuestaDTO dto = new DenunciaRespuestaDTO();
            
            dto.setId(denuncia.getId());
            dto.setDescripcion(denuncia.getDescripcion());
            dto.setEstado(denuncia.getEstado());
            dto.setMotivo(denuncia.getMotivo());
            dto.setObjeto(denuncia.getObjeto());
            
            List<PersonaConRolDTO> personas = new ArrayList<>();
            for (DenunciaPersona dp : denuncia.getDenunciaPersonas()) {
                PersonaConRolDTO p = new PersonaConRolDTO();
                p.setId(dp.getPersona().getId());
                p.setNombre(dp.getPersona().getNombre());
                p.setApellido(dp.getPersona().getApellido());
                p.setEmail(dp.getPersona().getEmail());
                p.setTelefono(dp.getPersona().getTelefono());
                p.setCp(dp.getPersona().getCp());
                p.setLocalidad(dp.getPersona().getLocalidad());
                p.setDocumento(dp.getPersona().getDocumento());
                p.setDomicilio(dp.getPersona().getDomicilio());
                p.setFax(dp.getPersona().getFax());
                p.setRol(dp.getRol());
                p.setNombreDelegado(dp.getNombreDelegado());
                p.setApellidoDelegado(dp.getApellidoDelegado());
                p.setDniDelegado(dp.getDniDelegado());
                personas.add(p);
            }
            dto.setPersonas(personas);
        
            respuestaDto.setCant_folios(expediente.getCant_folios());
            respuestaDto.setDelegacion(expediente.getDelegacion());
            respuestaDto.setFecha_finalizacion(expediente.getFecha_finalizacion());
            respuestaDto.setFecha_inicio(expediente.getFecha_inicio());
            respuestaDto.setHipervulnerable(expediente.getHipervulnerable());
            respuestaDto.setId(expediente.getId());
            respuestaDto.setNro_exp(expediente.getNro_exp());
            respuestaDto.setDenuncia(dto);   
            return respuestaDto;
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al traer el expediente: " + e.getMessage());
        }
        //respuestaDto.setDenuncia(expediente.getDenuncia() != null ? expediente.getDenuncia().toRespuestaDTO() : null);
        
    }

    @Override
    public List<ExpedienteRespuestaDTO> listarExpedientes() {
        var usuario = authUtil.getUsuarioAutenticado();
        boolean esAdmin = usuario.getRol() != null && usuario.getRol().getNombre().equalsIgnoreCase("ADMIN");
        List<Expediente> expedientes;
        if (esAdmin) {
            expedientes = (List<Expediente>) expedienteRepository.findAll();
        } else {
            expedientes = expedienteRepository.findByUsuarios_Id(usuario.getId());
        }
        List<ExpedienteRespuestaDTO> respuesta = new ArrayList<>();
        for (Expediente e : expedientes) {
            ExpedienteRespuestaDTO dto = new ExpedienteRespuestaDTO();
            dto.setId(e.getId());
            dto.setCant_folios(e.getCant_folios());
            dto.setDelegacion(e.getDelegacion());
            dto.setFecha_finalizacion(e.getFecha_finalizacion());
            dto.setFecha_inicio(e.getFecha_inicio());
            dto.setHipervulnerable(e.getHipervulnerable());
            dto.setNro_exp(e.getNro_exp());
            respuesta.add(dto);
        }
        return respuesta;
    }

    @Override
    public Expediente actualizarExpediente(Long id, Expediente expedienteActualizado) {
        List<Usuario> usuariosAsignados = new ArrayList<>();
        List<Usuario> usuarios = expedienteActualizado.getUsuarios();
        return expedienteRepository.findById(id).map(expediente -> {
            expediente.setNro_exp(expedienteActualizado.getNro_exp());
            expediente.setCant_folios(expedienteActualizado.getCant_folios());
            expediente.setFecha_inicio(expedienteActualizado.getFecha_inicio());
            expediente.setFecha_finalizacion(expedienteActualizado.getFecha_finalizacion());
            expediente.setHipervulnerable(expedienteActualizado.getHipervulnerable());
            expediente.setDelegacion(expedienteActualizado.getDelegacion());
            expediente.setDenuncia(expedienteActualizado.getDenuncia());
            for (Usuario usuario : usuarios) {
                usuariosAsignados.add(usuario);
            }
            expediente.setUsuarios(usuarios);
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

    // Permite a PreAuthorize verificar si el usuario puede acceder a un expediente
    public boolean usuarioPuedeAcceder(Long expedienteId) {
        var usuario = authUtil.getUsuarioAutenticado();
        var expediente = expedienteRepository.findById(expedienteId).orElse(null);
        boolean esAdmin = usuario.getRol() != null && usuario.getRol().getNombre().equalsIgnoreCase("ADMIN");
        return expediente != null && (esAdmin || expediente.getUsuarios().contains(usuario));
    }
}
