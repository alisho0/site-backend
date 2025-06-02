package com.dircomercio.site_backend.implementation;

import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dircomercio.site_backend.dtos.DenunciaDTO;
import com.dircomercio.site_backend.dtos.DenunciaRespuestaDTO;
import com.dircomercio.site_backend.dtos.DenunciaUpdateDTO;
import com.dircomercio.site_backend.dtos.PersonaConRolDTO;
import com.dircomercio.site_backend.dtos.PersonaRolDTO;
import com.dircomercio.site_backend.entities.Denuncia;
import com.dircomercio.site_backend.entities.DenunciaPersona;
import com.dircomercio.site_backend.entities.Persona;
import com.dircomercio.site_backend.repositories.DenunciaRepository;
import com.dircomercio.site_backend.services.DenunciaPersonaService;
import com.dircomercio.site_backend.services.DenunciaService;
import com.dircomercio.site_backend.services.DocumentoService;
import com.dircomercio.site_backend.services.EmailService;
import com.dircomercio.site_backend.services.PersonaService;

@Service
public class DenunciaServiceImpl implements DenunciaService{

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

    @Override
    public void guardarDenuncia(DenunciaDTO denunciaDTO, List<MultipartFile> files) {

        if (denunciaDTO == null) throw new IllegalArgumentException("La denuncia no puede ser nula.");
        if (files == null) throw new IllegalArgumentException("Los archivos no pueden ser nulos.");
        if (denunciaDTO.getPersonas() == null) throw new IllegalArgumentException("Las personas no deben ser nulas.");

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
            documentoService.guardarDocumentos(files, denuncia);

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
            Denuncia denuncia = denunciaRepository.findById(id).orElseThrow();
            denuncia.setEstado(dto.getEstado());
            String asunto = "";
            switch (denuncia.getEstado().toUpperCase()) {
                case "EN PROCESO":
                    asunto = "DENUNCIA EN PROCESO";
                    // Aquí podrías crear el expediente
                    break;
                case "NO ADMITIDA":
                    asunto = "DENUNCIA NO ADMITIDA";
                    break;
                case "RECHAZADA":
                    asunto = "DENUNCIA RECHAZADA";
                    break;
                default:
                    asunto = "ACTUALIZACIÓN DE DENUNCIA";
            }
            String destinarario = denuncia.getDenunciaPersonas().get(0).getPersona().getEmail();
            String msjHtml = "<h2>Hola " + denuncia.getDenunciaPersonas().get(0).getNombreDelegado() + ",</h2>"
            + "<br>"
            + "<p>Su denuncia ha cambiado de estado a <b>"+asunto+"</b>.</p>"
            + "<br>"
            + "<p><b>Motivo:</b> " + dto.getMotivo() + "</p>";
            // Este emailService no iría comentado, pero por el momento lo dejo comentado
            emailService.enviarEmail(destinarario, asunto, msjHtml); 
            return denunciaRepository.save(denuncia);
        } catch (Exception e) {
            throw new Exception("No se encontró la denuncia con el ID proporcionado");
        }
    }
    // Esto ya no iría, pero lo dejo para que vean 
    @Override
    public void rechazarDenuncia(Long id, String motivoRechazo) throws Exception {
        /* try {
            Denuncia denuncia = denunciaRepository.findById(id).orElseThrow();
            denuncia.setEstado("RECHAZADA");
            denunciaRepository.save(denuncia);

            // Mandar un email al denunciante informando del rechazo
            String destinarario = denuncia.getDenunciaPersonas().get(0).getPersona().getEmail();
            String asunto = "Denuncia Rechazada";
            emailService.enviarEmail(destinarario, asunto, motivoRechazo);
        } catch (Exception e) {
            throw new Exception("No se encontró la denuncia con el ID proporcionado");
        } */
    } 
}
