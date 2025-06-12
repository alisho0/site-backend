package com.dircomercio.site_backend.dtos;

import java.time.LocalDate;
import lombok.Data;

@Data
public class PaseRespuestaDTO {
    private Long id;
    private String accion;
    private LocalDate fechaAccion;
    private String areaAccion;
    private String tipoTramite;
    private String descripcion;
    private String nroExpediente;
    private String nombreUsuario;
}
