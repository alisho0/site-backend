package com.dircomercio.site_backend.dtos;

import com.dircomercio.site_backend.entities.Area;
import com.dircomercio.site_backend.entities.TipoDocumento;

import lombok.Data;

@Data
public class PaseCreateDTO {
    
    private String asunto;
    private Long cantFolios;
    private Area areaOrigen;
    private Area areaDestino;
    private String descripcion;

    private Long expedienteId;
    private Long usuarioId;

    private TipoDocumento tipoDocumento; 
}
