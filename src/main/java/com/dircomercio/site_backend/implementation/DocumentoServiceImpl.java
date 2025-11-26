package com.dircomercio.site_backend.implementation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.StreamUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dircomercio.site_backend.dtos.DocumentoRespuestaDTO;
import com.dircomercio.site_backend.dtos.OrdenCreateDTO;
import com.dircomercio.site_backend.dtos.OrdenRespuestaDTO;
import com.dircomercio.site_backend.entities.Denuncia;
import com.dircomercio.site_backend.entities.Documento;
import com.dircomercio.site_backend.entities.Expediente;
import com.dircomercio.site_backend.entities.Pase;
import com.dircomercio.site_backend.entities.TipoDocumento;
import com.dircomercio.site_backend.repositories.DocumentoRepository;
import com.dircomercio.site_backend.repositories.ExpedienteRepository;
import com.dircomercio.site_backend.services.DocumentoService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class DocumentoServiceImpl implements DocumentoService {

    @Autowired
    DocumentoRepository documentoRepository;

    @Autowired
    ExpedienteRepository expedienteRepository;

    @Override
    public List<Documento> guardarDocumentos(List<MultipartFile> files, Denuncia denuncia, Pase pase) throws Exception {
        return guardarDocumentos(files, denuncia, pase, null);
    }

    @Override
    public List<Documento> guardarDocumentos(List<MultipartFile> files, Denuncia denuncia, Pase pase, TipoDocumento tipoDocumento) throws Exception {
        List<Documento> documentos = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                Documento documento = new Documento();
                byte[] bytes = file.getBytes(); // Lee los bytes del archivo
                String fileName = UUID.randomUUID().toString(); // Genera un nombre único para el archivo
                String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

                long maxFileSize = 10 * 1024 * 1024; // 10 MB
                if (file.getSize() > maxFileSize) {
                    throw new RuntimeException("El archivo " + file.getOriginalFilename() + " excede el tamaño máximo permitido.");
                }
                if (fileExtension.equals(".exe") || fileExtension.equals(".bat")) {
                    throw new RuntimeException("El archivo " + file.getOriginalFilename() + " no es un formato permitido.");
                }

                String newFileName = fileName + fileExtension;
                File folder = new File("src/main/resources/archivos/"); 
                if (!folder.exists()) { // Crea la carpeta si no existe
                    folder.mkdirs();
                }
                Path path = Paths.get("src/main/resources/archivos/" + newFileName); // Define la ruta del archivo
                documento.setNombre(newFileName);
                documento.setFormato(fileExtension);
                documento.setRuta(path.toString());
                documento.setDenuncia(denuncia);
                documento.setPase(pase);

                String fechaNombre = LocalDate.now().toString();
                if (tipoDocumento != null) {
                    documento.setTipoDocumento(tipoDocumento);
                    String nombreVisible = tipoDocumento + "_" + (denuncia.getId() != null ? denuncia.getId() : "0") + "_" + fechaNombre + ".pdf";
                    documento.setNombrevisible(nombreVisible);
                } else {
                    documento.setTipoDocumento(TipoDocumento.DATOS_DENUNCIA); // Asigna un tipo por defecto si no se especifica
                    String nombreVisible = TipoDocumento.DATOS_DENUNCIA + "_" + (denuncia.getId() != null ? denuncia.getId() : "0") + "_" + fechaNombre + ".pdf";
                    documento.setNombrevisible(nombreVisible);
                }
                if (pase != null) {
                    documento.setReferencia("Pase");
                } else if (denuncia != null && denuncia.getExpediente() != null) {
                    documento.setReferencia(denuncia.getExpediente().getNroExp().toString());
                } else {
                    documento.setReferencia("Usuario Externo");
                }
                documento.setFechaCreacion(LocalDateTime.now());
                Files.write(path, bytes);
                
                // CORREGIDO: Era 'documento.add' -> debe ser 'documentos.add' (la lista)
                documentos.add(documento); 
            }
            return (List<Documento>) documentoRepository.saveAll(documentos);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<DocumentoRespuestaDTO> traerDocumentosPorDenuncia(Long denunciaId) throws Exception {
        try {
            List<Documento> docs = documentoRepository.findAllByDenunciaId(denunciaId);
            List<DocumentoRespuestaDTO> dtos = new ArrayList<>();
            for (Documento doc : docs) {
                dtos.add(new DocumentoRespuestaDTO(doc.getId(), doc.getNombre(), doc.getFormato(), doc.getNombrevisible()));
            }
            return dtos;
        } catch (Exception e) {
            throw new Exception("Error al traer documentos por denuncia: " + e.getMessage());
        }
    }

    @Override
    public Documento obtenerPorId(Long id) throws Exception {
        try {
            return documentoRepository.findById(id)
                .orElseThrow(() -> new Exception("No se encontró el documento con id: " + id));
        } catch (Exception e) {
            throw new Exception("No se encontró el pdf" + e.getMessage());
        }
    }

    @Override
    public List<OrdenRespuestaDTO> traerOrdenesPorExpediente(Long expedienteId) throws Exception {
        Expediente expediente = expedienteRepository.findById(expedienteId)
            .orElseThrow(() -> new Exception("No se encontró el expediente buscado"));  
        List<Documento> documentos = expediente.getDenuncia().getDocumentos();      
        List<OrdenRespuestaDTO> ordenRespuesta = new ArrayList<>();

        for (Documento doc : documentos) {
            OrdenRespuestaDTO ordenDto = OrdenRespuestaDTO.builder()
                .id(doc.getId())
                .tipoDocumento(doc.getTipoDocumento())
                .nroDocumento(doc.getNombre())
                .referencia(doc.getReferencia())
                .fechaCreacion(doc.getFechaCreacion())
                .orden(doc.getOrden())
                .nombreVisible(doc.getNombrevisible() != null ? doc.getNombrevisible() : null)
                .id_pase(doc.getPase() != null ? doc.getPase().getId() : null)
                .build();
            ordenRespuesta.add(ordenDto);
        }
        if (!ordenRespuesta.isEmpty()) {
            return ordenRespuesta;
        } else {
            throw new Exception("No se encontraron documentos para el expediente con ID: " + expedienteId);
        }
    }

    public void crearOrden(List<MultipartFile> files, OrdenCreateDTO oDto) throws Exception {
        Expediente expediente = expedienteRepository.findById(oDto.getExpedienteId())
            .orElseThrow(() -> new Exception("No se encontró el expediente con ID: " + oDto.getExpedienteId()));
        Denuncia denuncia = expediente.getDenuncia();
        try {
            guardarDocumentos(files, denuncia, null, oDto.getTipoDocumento());
        } catch (Exception e) {
            throw new Exception("Error al guardar documentos: " + e.getMessage());
        }
    }

    @Override
    public DocumentoRespuestaDTO cambiarNombreVisible(Long id, String nuevoNombre) throws Exception {
        try {
            Documento doc = documentoRepository.findById(id)
                .orElseThrow(() -> new Exception("Error al encontrar el documento"));
            doc.setNombrevisible(nuevoNombre);
            documentoRepository.save(doc);
            return DocumentoRespuestaDTO.builder()
                .id(doc.getId())
                .nombre(doc.getNombre())
                .nombreVisible(doc.getNombrevisible())
                .formato(doc.getFormato())
                .build();
        } catch (Exception e) {
            throw new Exception("Error en la implementación de cambiar nombre al documento: " + e.getMessage());
        }
    }

    // Implementación de la descarga ZIP
    @Override
    public void descargarZipExpediente(Long expedienteId, HttpServletResponse response) throws Exception {
        // 1. Buscamos los documentos usando el método nuevo del Repo
        List<Documento> documentos = documentoRepository.buscarPorExpedienteId(expedienteId);

        if (documentos.isEmpty()) {
            throw new Exception("No hay documentos para este expediente.");
        }

        // 2. Configuramos el Response
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=\"Expediente_" + expedienteId + "_Documentos.zip\"");
        response.setStatus(HttpServletResponse.SC_OK);

        // 3. Generamos el ZIP
        try (ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream())) {
            Set<String> nombresUsados = new HashSet<>();

            for (Documento doc : documentos) {
                // Validar que tenga ruta física
                if (doc.getRuta() == null || doc.getRuta().isEmpty()) continue;

                Path path = Paths.get(doc.getRuta());
                if (!Files.exists(path)) continue; // Si el archivo no existe en disco, saltar

                // Definir nombre del archivo dentro del ZIP
                String nombreArchivo = (doc.getNombrevisible() != null && !doc.getNombrevisible().isEmpty()) 
                                        ? doc.getNombrevisible() 
                                        : doc.getNombre();
                
                // Asegurar extensión .pdf (si no la tiene ya)
                if (!nombreArchivo.toLowerCase().endsWith(".pdf")) {
                    nombreArchivo += ".pdf";
                }

                // Evitar nombres duplicados en el ZIP (agregar _ID si se repite)
                if (nombresUsados.contains(nombreArchivo)) {
                    nombreArchivo = nombreArchivo.replace(".pdf", "_" + doc.getId() + ".pdf");
                }
                nombresUsados.add(nombreArchivo);

                // Crear entrada en el ZIP y copiar bytes
                ZipEntry zipEntry = new ZipEntry(nombreArchivo);
                zipEntry.setSize(Files.size(path));
                zipOut.putNextEntry(zipEntry);

                try (FileInputStream fis = new FileInputStream(path.toFile())) {
                    StreamUtils.copy(fis, zipOut);
                }
                zipOut.closeEntry();
            }
            zipOut.finish();
        } catch (IOException e) {
            throw new Exception("Error al generar el archivo ZIP: " + e.getMessage());
        }
    }
}