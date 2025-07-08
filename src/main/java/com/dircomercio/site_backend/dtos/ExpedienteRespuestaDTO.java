package com.dircomercio.site_backend.dtos;

import java.time.LocalDate;
import java.util.List;

import com.dircomercio.site_backend.entities.Usuario;

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
    private List<UsuarioDTO> usuRespuesta;
     
}
