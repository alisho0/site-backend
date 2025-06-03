package com.dircomercio.site_backend.dtos;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpedienteRespuestaDTO {
    private Long id;
    private String nro_exp;
    private String cant_folios;
    private LocalDate fecha_inicio;
    private LocalDate fecha_finalizacion;
    private String hipervulnerable;
    private String delegacion;
    /* Falta ->
     * private UsuarioRespuestaDTO usuRespuesta;
     * private List<AudienciasRespuestaDTO> auResp;
     */
}
