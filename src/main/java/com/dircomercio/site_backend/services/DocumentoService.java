package com.dircomercio.site_backend.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.dircomercio.site_backend.dtos.DocumentoRespuestaDTO;
import com.dircomercio.site_backend.dtos.OrdenRespuestaDTO;
import com.dircomercio.site_backend.entities.Denuncia;
import com.dircomercio.site_backend.entities.Documento;
import com.dircomercio.site_backend.entities.Pase;

import com.dircomercio.site_backend.entities.TipoDocumento;

public interface DocumentoService {

    List<Documento> guardarDocumentos(List<MultipartFile> files, Denuncia denuncia, Pase pase) throws Exception;
    List<Documento> guardarDocumentos(List<MultipartFile> files, Denuncia denuncia, Pase pase, TipoDocumento tipoDocumento) throws Exception;
    List<DocumentoRespuestaDTO> traerDocumentosPorDenuncia(Long id) throws Exception;
    Documento obtenerPorId(Long id) throws Exception;

    List<OrdenRespuestaDTO> traerOrdenesPorExpediente(Long expedienteId) throws Exception;
}
