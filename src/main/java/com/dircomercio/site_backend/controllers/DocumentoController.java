package com.dircomercio.site_backend.controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dircomercio.site_backend.dtos.DocumentoRespuestaDTO;
import com.dircomercio.site_backend.dtos.OrdenRespuestaDTO;
import com.dircomercio.site_backend.entities.Documento;
import com.dircomercio.site_backend.repositories.DenunciaRepository;
import com.dircomercio.site_backend.repositories.PaseRepository;
import com.dircomercio.site_backend.services.DocumentoService;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


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
    
    // Endpoint para subir documentos a denuncia o pase existente
    // @PostMapping("/subirArchivosExp")
    // public ResponseEntity<?> subirDocumentos(
    //         @RequestPart("info") String infoJson,
    //         @RequestPart("file") List<MultipartFile> files) {
    //     try {
    //         ObjectMapper mapper = new ObjectMapper();
    //         DocumentoCargaDTO dto = mapper.readValue(infoJson, DocumentoCargaDTO.class);
    //         if (dto.getDenunciaId() != null) {
    //             Denuncia denuncia = denunciaRepository.findById(dto.getDenunciaId())
    //                     .orElseThrow(() -> new IllegalArgumentException("No se encontró la denuncia"));
    //         }
    //         if (dto.getPaseId() != null) {
    //             Pase pase = paseRepository.findById(dto.getPaseId())
    //                     .orElseThrow(() -> new IllegalArgumentException("No se encontró el pase"));
    //         }
    //         documentoService.guardarDocumentos(files, denuncia, pase, dto.getTipoDocumento());
    //         return ResponseEntity.ok("Documentos subidos correctamente");
    //     } catch (Exception e) {
    //         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al subir documentos: " + e.getMessage());
    //     }
    // }
}
