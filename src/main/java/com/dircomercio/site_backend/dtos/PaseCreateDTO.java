package com.dircomercio.site_backend.dtos;

import java.time.LocalDate;
import lombok.Data;

@Data
public class PaseCreateDTO {
    private String accion;
    private LocalDate fechaAccion;
    private String areaAccion;
    private String tipoTramite;
    private String descripcion;
    private Long expedienteId;
    private Long usuarioId;
}
