//maneja la creacion de ordenes
package com.dircomercio.site_backend.dtos;

import com.dircomercio.site_backend.entities.TipoDocumento;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdenCreateDTO {
    
    @NotNull(message = "El ID del expediente no puede estar vacio")
    @Positive(message = "El ID del expediente debe ser un numero positivo")
    private Long expedienteId;
    
    @NotNull(message = "El tipo de documento no puede estar vacio")
    private TipoDocumento tipoDocumento;
}
