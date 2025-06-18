package com.dircomercio.site_backend.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DenunciaEstadoRespuestaDTO {

    private String estado;
    private String observacion;
    private LocalDateTime fecha;
}
