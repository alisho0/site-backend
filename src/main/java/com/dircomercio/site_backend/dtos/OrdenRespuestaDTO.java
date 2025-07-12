package com.dircomercio.site_backend.dtos;

import java.time.LocalDateTime;

import com.dircomercio.site_backend.entities.TipoDocumento;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrdenRespuestaDTO {
    private TipoDocumento tipoDocumento;
    private String nroDocumento;
    private String referencia;
    private LocalDateTime fechaCreacion;
    private Long orden;
}
