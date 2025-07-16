package com.dircomercio.site_backend.dtos;

import com.dircomercio.site_backend.entities.TipoDocumento;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdenCreateDTO {
    private Long expedienteId;
    private TipoDocumento tipoDocumento;
}
