package com.dircomercio.site_backend.implementation;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    }
