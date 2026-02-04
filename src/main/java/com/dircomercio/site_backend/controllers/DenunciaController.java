package com.dircomercio.site_backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dircomercio.site_backend.dtos.DenunciaDTO;
import com.dircomercio.site_backend.dtos.DenunciaEstadoRespuestaDTO;
import com.dircomercio.site_backend.dtos.DenunciaRespuestaDTO;
import com.dircomercio.site_backend.dtos.DenunciaUpdateDTO;
import com.dircomercio.site_backend.services.DenunciaService;
import com.dircomercio.site_backend.services.DocumentoService;
import com.dircomercio.site_backend.services.AuditoriaService;
import com.dircomercio.site_backend.utils.IpUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/denuncia")
// http://localhost:8080/denuncia/
public class DenunciaController {

    @Autowired
    DenunciaService denunciaService;

    @Autowired
    DocumentoService documentoService;

    @Autowired
    private AuditoriaService auditoriaService;

    private String obtenerUsuarioActual() {
        try {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                return authentication.getName();
            }
        } catch (Exception e) {
        }
        return "ANONIMO";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECCION', 'MESA_DE_ENTRADA', 'ABOGADOS')")
    @GetMapping("/historial/{id}")
    public ResponseEntity<?> getHistorialDenuncia(@PathVariable Long id) {
        try {
            List<DenunciaEstadoRespuestaDTO> historial = denunciaService.traerHistorialDenuncia(id);
            return ResponseEntity.ok(historial);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("No se pudo obtener el historial para el id: " + id);
        }
    }

    // registrar auditoria en creacion
    @PostMapping("/subirDenuncia")
    public ResponseEntity<?> subirDenuncia(
            @RequestPart("denuncia") String denunciaJson,
            @RequestPart("file") List<MultipartFile> files,
            HttpServletRequest request) throws JsonProcessingException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            DenunciaDTO denunciaDTO = mapper.readValue(denunciaJson, DenunciaDTO.class);
            denunciaService.guardarDenuncia(denunciaDTO, files);
            auditoriaService.registrarAccion(obtenerUsuarioActual(), "CREAR_DENUNCIA",
                    "Denuncia creada con descripcion: " + denunciaDTO.getDescripcion(), IpUtil.obtenerIP(request),
                    "SUCCESS", "Denuncia", null);
            return ResponseEntity.ok().body("La denuncia fue subida correctamente desde el controlador.");
        } catch (Exception e) {
            e.printStackTrace();
            auditoriaService.registrarAccion(obtenerUsuarioActual(), "CREAR_DENUNCIA",
                    "Error al crear denuncia: " + e.getMessage(), IpUtil.obtenerIP(request),
                    "FAILURE", "Denuncia", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Algo salio mal en el controlador");
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECCION', 'MESA_DE_ENTRADA', 'ABOGADOS')")
    @GetMapping("/traerDenuncia")
    public ResponseEntity<?> traerDenuncia() {
        try {
            return ResponseEntity.ok().body(denunciaService.traerDenuncias());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("No se pudo traer la denuncia.");
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECCION', 'MESA_DE_ENTRADA', 'ABOGADOS')")
    @GetMapping("/traerDenunciaPorId/{id}")
    public ResponseEntity<?> traerDenunciaPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok().body(denunciaService.traerDenunciaPorId(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("No se pudo traer la denuncia.");
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECCION', 'MESA_DE_ENTRADA', 'ABOGADOS')")
    @GetMapping("/traerDenunciasPorUsuario")
    public ResponseEntity<?> traerPorUsu() {
        System.out.println("DEBUG: Entering traerDenunciasPorUsuario");
        System.out.println("DEBUG: Auth: " + SecurityContextHolder.getContext().getAuthentication());
        System.out.println(
                "DEBUG: Authorities: " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        try {
            List<DenunciaRespuestaDTO> denuncias = denunciaService.traerDenunciasPorUsuario();
            return ResponseEntity.ok().body(denuncias);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo traer las denuncias por usuario: " + e.getMessage());
        }
    }

    // registrar auditoria en cambio de estado
    @PutMapping("/actualizarEstado/{id}")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long id, @Valid @RequestBody DenunciaUpdateDTO dto,
            HttpServletRequest request) {
        try {
            denunciaService.actualizarEstadoDenuncia(id, dto);
            auditoriaService.registrarAccion(obtenerUsuarioActual(), "ACTUALIZAR_DENUNCIA",
                    "Estado de denuncia ID " + id + " actualizado a: " + dto.getEstado(), IpUtil.obtenerIP(request),
                    "SUCCESS", "Denuncia", id);
            return ResponseEntity.ok().body("El estado de la denuncia fue actualizada correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            auditoriaService.registrarAccion(obtenerUsuarioActual(), "ACTUALIZAR_DENUNCIA",
                    "Error al actualizar denuncia ID " + id + ": " + e.getMessage(), IpUtil.obtenerIP(request),
                    "FAILURE", "Denuncia", id);
            throw new RuntimeException("No se pudo actualizar el estado de la denuncia." + e.getMessage());
        }
    }

    @PostMapping("/mandarCorreo/{id}")
    public ResponseEntity<?> mandarCorreo(@PathVariable Long id, @RequestBody String observacion) throws Exception {
        try {
            denunciaService.notificarEstadoSinCambio(id, observacion);
            return ResponseEntity.ok("Correo enviado correctamente");
        } catch (Exception e) {
            throw new Exception("Hubo un problema al mandar el correo: " + e.getMessage());
        }
    }
}
