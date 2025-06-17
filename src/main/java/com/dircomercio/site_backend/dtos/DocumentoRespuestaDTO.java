package com.dircomercio.site_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DocumentoRespuestaDTO {
    private Long id;
    private String nombre;
    private String formato;
}
