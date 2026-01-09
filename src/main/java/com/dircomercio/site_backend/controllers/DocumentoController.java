package com.dircomercio.site_backend.controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dircomercio.site_backend.dtos.DocumentoRespuestaDTO;
import com.dircomercio.site_backend.dtos.OrdenCreateDTO;
import com.dircomercio.site_backend.dtos.OrdenRespuestaDTO;
import com.dircomercio.site_backend.entities.Documento;
import com.dircomercio.site_backend.repositories.DenunciaRepository;
import com.dircomercio.site_backend.repositories.PaseRepository;
import com.dircomercio.site_backend.services.DocumentoService;
import com.dircomercio.site_backend.services.AuditoriaService;
import com.dircomercio.site_backend.utils.IpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.servlet.http.HttpServletRequest;



@RestController
@NoArgsConstructor
@AllArgsConstructor
@RequestMapping("/doc")
public class DocumentoController {

    @Autowired
    DocumentoService documentoService;

    @Autowired
    DenunciaRepository denunciaRepository;

    @Autowired
    PaseRepository paseRepository;

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

    @GetMapping("/traerPorDenuncia/{id}")
    public ResponseEntity<?> obtenerPdfDenuncia(@PathVariable Long id) throws Exception {
        List<DocumentoRespuestaDTO> docs;
        try {
            docs = documentoService.traerDocumentosPorDenuncia(id);
            return ResponseEntity.ok(docs);
        } catch (Exception e) {
            throw new Exception("Problema en el controlador de pdf: " + e.getMessage());
        }
    }

    @GetMapping("/traerPorId/{id}")
    public ResponseEntity<byte[]> obtenerPdf(@PathVariable Long id) {
        try {
            Documento doc = documentoService.obtenerPorId(id);
            Path path = Paths.get(doc.getRuta());
            byte[] contenido = Files.readAllBytes(path);
            return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "inline; filename=\"" + doc.getNombre() + "\"")
                .body(contenido);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/traerOrdenesPorExpediente/{expedienteId}")
    public ResponseEntity<?> obtenerOrdenesPorExpediente(@PathVariable Long expedienteId) throws Exception {
        try {
            List<OrdenRespuestaDTO> ordenes = documentoService.traerOrdenesPorExpediente(expedienteId);
            return ResponseEntity.ok(ordenes);
        } catch (Exception e) {
            throw new Exception("Error al obtener las órdenes: " + e.getMessage());
        }
    }

    // registrar auditoria en eliminacion
    @DeleteMapping("/eliminarDoc/{id}")
    public ResponseEntity<?> eliminarDocumento(@PathVariable Long id, HttpServletRequest request) {
        try {
            Documento documento = documentoService.obtenerPorId(id);
            if (documento != null) {
                Path path = Paths.get(documento.getRuta());
                Files.deleteIfExists(path);
                auditoriaService.registrarAccion(obtenerUsuarioActual(), "ELIMINAR_DOCUMENTO", 
                    "Documento ID " + id + " (" + documento.getNombre() + ") eliminado", IpUtil.obtenerIP(request), 
                    "SUCCESS", "Documento", id);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            auditoriaService.registrarAccion(obtenerUsuarioActual(), "ELIMINAR_DOCUMENTO", 
                "Error al eliminar documento ID " + id + ": " + e.getMessage(), IpUtil.obtenerIP(request), 
                "FAILURE", "Documento", id);
            return ResponseEntity.status(500).body(null);
        }
    }

    // registrar auditoria en creacion
    @PostMapping("/crearOrden")
    public ResponseEntity<?> crearOrden(@RequestPart("orden") String ordenInfoJson, @RequestPart("file") List<MultipartFile> files, HttpServletRequest request) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            OrdenCreateDTO oDto = mapper.readValue(ordenInfoJson, OrdenCreateDTO.class);
            documentoService.crearOrden(files, oDto);
            auditoriaService.registrarAccion(obtenerUsuarioActual(), "CREAR_ORDEN", 
                "Orden creada - Expediente: " + oDto.getExpedienteId(), IpUtil.obtenerIP(request), 
                "SUCCESS", "Orden", null);
            return ResponseEntity.ok("Orden creada correctamente");
        } catch (Exception e) {
            auditoriaService.registrarAccion(obtenerUsuarioActual(), "CREAR_ORDEN", 
                "Error al crear orden: " + e.getMessage(), IpUtil.obtenerIP(request), 
                "FAILURE", "Orden", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear la orden: " + e.getMessage());
        }
    }

}
