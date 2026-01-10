package com.dircomercio.site_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DocumentoRespuestaDTO {
    private Long id;
    private String nombre;
    private String formato;
    private String nombreVisible;
}
