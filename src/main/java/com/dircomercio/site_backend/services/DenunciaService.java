package com.dircomercio.site_backend.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.dircomercio.site_backend.dtos.DenunciaDTO;
import com.dircomercio.site_backend.dtos.DenunciaEstadoRespuestaDTO;
import com.dircomercio.site_backend.dtos.DenunciaRespuestaDTO;
import com.dircomercio.site_backend.dtos.DenunciaUpdateDTO;
import com.dircomercio.site_backend.entities.Denuncia;

public interface DenunciaService {

    void guardarDenuncia(DenunciaDTO denuncia, List<MultipartFile> files);
    List<DenunciaRespuestaDTO> traerDenuncias() throws Exception;
    DenunciaRespuestaDTO traerDenunciaPorId(Long id) throws Exception;
    Denuncia actualizarEstadoDenuncia(Long id, DenunciaUpdateDTO dto) throws Exception;
    void notificarEstadoSinCambio(Long denunciaId, String observacion) throws Exception;
    List<DenunciaEstadoRespuestaDTO> traerHistorialDenuncia(Long id) throws Exception;
    List<DenunciaRespuestaDTO> traerDenunciasPorUsuario() throws Exception;
}
