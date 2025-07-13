package com.dircomercio.site_backend.implementation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dircomercio.site_backend.dtos.DenunciaDTO;
import com.dircomercio.site_backend.dtos.DenunciaEstadoRespuestaDTO;
import com.dircomercio.site_backend.dtos.DenunciaRespuestaDTO;
import com.dircomercio.site_backend.dtos.DenunciaUpdateDTO;
import com.dircomercio.site_backend.dtos.PersonaConRolDTO;
import com.dircomercio.site_backend.dtos.PersonaRolDTO;
import com.dircomercio.site_backend.entities.Denuncia;
import com.dircomercio.site_backend.entities.DenunciaEstado;
import com.dircomercio.site_backend.entities.DenunciaPersona;
import com.dircomercio.site_backend.entities.Expediente;
import com.dircomercio.site_backend.entities.Persona;
import com.dircomercio.site_backend.repositories.DenunciaEstadoRepository;
import com.dircomercio.site_backend.repositories.DenunciaRepository;
import com.dircomercio.site_backend.repositories.ExpedienteRepository;
import com.dircomercio.site_backend.services.DenunciaPersonaService;
import com.dircomercio.site_backend.services.DenunciaService;
import com.dircomercio.site_backend.services.DocumentoService;
import com.dircomercio.site_backend.services.EmailService;
import com.dircomercio.site_backend.services.ExpedienteService;
import com.dircomercio.site_backend.services.PersonaService;

@Service
public class DenunciaServiceImpl implements DenunciaService {

    @Autowired
    DenunciaRepository denunciaRepository;

    @Autowired
    PersonaService personaService;

    @Autowired
    DocumentoService documentoService;

    @Autowired
    DenunciaPersonaService dPersonaService;

    @Autowired
    EmailService emailService;
    // agrego esto mostrar a ale, lo que hice aqui primero fue inyectar el servicio
    // de expediente
    @Autowired
    ExpedienteService expedienteService;

    @Autowired
    DenunciaEstadoRepository denunciaEstadoRepository;

    @Autowired
    ExpedienteRepository expedienteRepository;

    @Override
    public void guardarDenuncia(DenunciaDTO denunciaDTO, List<MultipartFile> files) {

        if (denunciaDTO == null)
            throw new IllegalArgumentException("La denuncia no puede ser nula.");
        if (files == null)
            throw new IllegalArgumentException("Los archivos no pueden ser nulos.");
        if (denunciaDTO.getPersonas() == null)
            throw new IllegalArgumentException("Las personas no deben ser nulas.");

        try {
            // Aquí se crea la denuncia
            Denuncia denuncia = new Denuncia();
            denuncia.setDescripcion(denunciaDTO.getDescripcion());
            denuncia.setObjeto(denunciaDTO.getObjeto());
            denuncia.setMotivo(denunciaDTO.getMotivo());
            denunciaRepository.save(denuncia);

            // Aquí se crean (si es que no existen) y vinculan las personas a la denuncia
            List<Persona> personasPersistidas = personaService.guardarPersonas(denunciaDTO.getPersonas());
            // 3. Mapear roles a personas persistidas
            List<PersonaRolDTO> personasRolPersistidas = new ArrayList<>();
            for (int i = 0; i < denunciaDTO.getPersonas().size(); i++) {
                PersonaRolDTO original = denunciaDTO.getPersonas().get(i);
                Persona personaPersistida = personasPersistidas.get(i);
                PersonaRolDTO dto = new PersonaRolDTO();
                dto.setPersona(personaPersistida);
                dto.setRol(original.getRol());
                dto.setNombreDelegado(original.getNombreDelegado());
                dto.setApellidoDelegado(original.getApellidoDelegado());
                dto.setDniDelegado(original.getDniDelegado());
                personasRolPersistidas.add(dto);
            }
            dPersonaService.vincularPersonaDenuncia(personasRolPersistidas, denuncia);

            // Aquí se guardan los documentos, sigue igual
            documentoService.guardarDocumentos(files, denuncia, null);

        } catch (Exception e) {
            e.getMessage();
            throw new RuntimeException("Ocurrió un error al guardar una nueva denuncia");
        }
    }

    @Override
    public List<DenunciaRespuestaDTO> traerDenuncias() throws NotFoundException {
        try {
            List<Denuncia> denuncias = (List<Denuncia>) denunciaRepository.findAll();
            List<DenunciaRespuestaDTO> respuesta = new ArrayList<>();

            for (Denuncia denuncia : denuncias) {
                DenunciaRespuestaDTO dto = new DenunciaRespuestaDTO();
                dto.setId(denuncia.getId());
                dto.setDescripcion(denuncia.getDescripcion());
                dto.setEstado(denuncia.getEstado());
                dto.setMotivo(denuncia.getMotivo());
                dto.setObjeto(denuncia.getObjeto());

                List<PersonaConRolDTO> personas = new ArrayList<>();
                for (DenunciaPersona dp : denuncia.getDenunciaPersonas()) {
                    PersonaConRolDTO p = new PersonaConRolDTO(); // Crimenes perfecto, los aviones, donde manda marinero
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
                respuesta.add(dto);
            }
            return respuesta;
        } catch (Exception e) {
            e.getMessage();
            throw new NotFoundException();
        }
    }

    @Override
    public DenunciaRespuestaDTO traerDenunciaPorId(Long id) throws Exception {
        try {
            Denuncia denuncia = denunciaRepository.findById(id).orElseThrow();
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
            return dto;
        } catch (Exception e) {
            throw new Exception("No se encontró la denuncia con el ID proporcionado");
        }
    }

    @Override
    public Denuncia actualizarEstadoDenuncia(Long id, DenunciaUpdateDTO dto) throws Exception {
        try {
            Denuncia denuncia = denunciaRepository.findById(id)
                    .orElseThrow(() -> new Exception("No se encontró la denuncia con el ID proporcionado"));

            List<DenunciaEstado> historial = denunciaEstadoRepository.findByDenunciaOrderByFechaAsc(denuncia);
            String nuevoEstado = dto.getEstado();
            String nuevaObs = dto.getMotivo();
            System.out.println("Entrando a actualizarEstadoDenuncia, usuario autenticado: " + SecurityContextHolder.getContext().getAuthentication());
            // Validación de transición robusta
            boolean tuvoRechazado = historial.stream().anyMatch(
                    e -> "RECHAZADA".equalsIgnoreCase(e.getEstado()) || "NO ADMITIDA".equalsIgnoreCase(e.getEstado()));
            boolean tuvoProceso = historial.stream().anyMatch(e -> "EN PROCESO".equalsIgnoreCase(e.getEstado()));
            String ultimoEstado = historial.isEmpty() ? null : historial.get(historial.size() - 1).getEstado();
            String ultimaObs = historial.isEmpty() ? null : historial.get(historial.size() - 1).getObservacion();

            if (tuvoRechazado && "EN PROCESO".equalsIgnoreCase(nuevoEstado)) {
                throw new Exception(
                        "No se puede cambiar el estado a 'EN PROCESO' si la denuncia ya fue rechazada o no admitida.");
            }
            if (!tuvoProceso && "EN PROCESO".equalsIgnoreCase(nuevoEstado)) {
                expedienteService.crearExpedienteDesdeDenuncia(denuncia.getId());
            }
            // Evitar guardar el mismo estado dos veces seguidas salvo que la observación
            // sea diferente
            if (nuevoEstado.equalsIgnoreCase(ultimoEstado) && (nuevaObs == null || nuevaObs.equals(ultimaObs))) {
                throw new Exception(
                        "El estado ya es '" + nuevoEstado + "' y la observación no cambió. No se registra duplicado.");
            }

            denuncia.setEstado(nuevoEstado);
            String añoActual = String.valueOf(LocalDate.now().getYear());
            String prefijo = "EXP" + "-" + añoActual + "-";
            int cantExpedientes = expedienteRepository.countByNroExpStartingWith(prefijo);
            String nroExpediente = prefijo + (cantExpedientes + 1);
            DenunciaEstado nuevo = DenunciaEstado.builder()
                    .denuncia(denuncia)
                    .estado(nuevoEstado)
                    .fecha(LocalDateTime.now())
                    .observacion(nuevaObs)
                    .build();
            denunciaEstadoRepository.save(nuevo);

            String asunto = "Estado de denuncia: " + nuevoEstado;
            String destinarario = denuncia.getDenunciaPersonas().get(0).getPersona().getEmail();
            String msjHtml = "<h2>Hola " + denuncia.getDenunciaPersonas().get(0).getPersona().getNombre() + ",</h2>"
                    + "<br>"
                    + "<p>Su denuncia ha cambiado de estado a <b>" + nuevoEstado + "</b>.</p>"
                    + "<p>Podrás ver el estado de tu denuncia en el inicio con el siguiente código: " + nroExpediente
                    + "<br>"
                    + "<p><b>Motivo:</b> " + nuevaObs + "</p>";
            emailService.enviarEmail(destinarario, asunto, msjHtml);
            return denunciaRepository.save(denuncia);
        } catch (Exception e) {
            throw new Exception("Hay un error al actualizar el estado de la denuncia: " + e.getMessage());
        }
    }

    // Enviar mail sin cambiar el estado, pero registrando en historial si la
    // observación es diferente
    public void notificarEstadoSinCambio(Long denunciaId, String observacion) throws Exception {
        Denuncia denuncia = denunciaRepository.findById(denunciaId)
                .orElseThrow(() -> new Exception("No se encontró la denuncia con el ID proporcionado"));
        List<DenunciaEstado> historial = denunciaEstadoRepository.findByDenunciaOrderByFechaAsc(denuncia);
        String ultimoEstado = historial.isEmpty() ? null : historial.get(historial.size() - 1).getEstado();
        String ultimaObs = historial.isEmpty() ? null : historial.get(historial.size() - 1).getObservacion();
        if (observacion != null && !observacion.equals(ultimaObs)) {
            DenunciaEstado nuevo = DenunciaEstado.builder()
                    .denuncia(denuncia)
                    .estado(ultimoEstado)
                    .fecha(LocalDateTime.now())
                    .observacion(observacion)
                    .build();
            denunciaEstadoRepository.save(nuevo);
        }
        String asunto = "Estado de denuncia: " + denuncia.getEstado();
        String destinarario = denuncia.getDenunciaPersonas().get(0).getPersona().getEmail();
        String msjHtml = "<h2>Hola " + denuncia.getDenunciaPersonas().get(0).getPersona().getNombre() + ",</h2>"
                + "<br>"
                + "<p>El estado de su denuncia es <b>" + denuncia.getEstado() + "</b>.</p>"
                + "<br>"
                + "<p><b>Observación:</b> " + observacion + "</p>";
        emailService.enviarEmail(destinarario, asunto, msjHtml);
    }

    @Override
    public List<DenunciaRespuestaDTO> traerDenunciasPorUsuario() throws Exception {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            List<Expediente> expediente = expedienteRepository.findByUsuarios_Email(email);
            if (expediente.isEmpty()) {
                throw new Exception("No se encontraron expedientes para el usuario con email: " + email);
            }
            List<DenunciaRespuestaDTO> denunciasResp = new ArrayList<>();
            for (Expediente exp : expediente) {
                List<PersonaConRolDTO> personasDto = new ArrayList<>();
                for (DenunciaPersona dp : exp.getDenuncia().getDenunciaPersonas()) {
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
                    personasDto.add(p);
                }
                DenunciaRespuestaDTO denunciaResp = DenunciaRespuestaDTO.builder()
                    .id(exp.getDenuncia().getId())
                    .descripcion(exp.getDenuncia().getDescripcion())
                    .objeto(exp.getDenuncia().getObjeto())
                    .motivo(exp.getDenuncia().getMotivo())
                    .estado(exp.getDenuncia().getEstado())
                    .personas(personasDto)
                    .build();
                denunciasResp.add(denunciaResp);
            }
            return denunciasResp;
        } catch (Exception e) {
            throw new Exception("Error al traer las denuncias por usuario: " + e.getMessage());
        }
    }

    @Override
    public List<DenunciaEstadoRespuestaDTO> traerHistorialDenuncia(Long id) throws Exception {
    // 1) Busca la denuncia relacionada por nroExp
    Denuncia denuncia = denunciaRepository.findById(id)
            .orElseThrow(() -> new Exception("No se encontró la denuncia con el ID proporcionado"));
    List<DenunciaEstado> historial = denunciaEstadoRepository.findByDenunciaOrderByFechaAsc(denuncia);

    if (historial == null || historial.isEmpty()) {
        throw new Exception("No se encontraron estados para la denuncia con id de Denuncia: " + id);
    }

    List<DenunciaEstadoRespuestaDTO> respuesta = new ArrayList<>();
    for (DenunciaEstado estado : historial) {
        DenunciaEstadoRespuestaDTO dto = new DenunciaEstadoRespuestaDTO();
        dto.setEstado(estado.getEstado());
        dto.setFecha(estado.getFecha());
        dto.setObservacion(estado.getObservacion());
        respuesta.add(dto);
    }   

    return respuesta;
    }
    
    
    
}
