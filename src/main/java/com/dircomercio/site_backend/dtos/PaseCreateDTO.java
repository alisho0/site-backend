//maneja la creacion de pases
package com.dircomercio.site_backend.dtos;

import com.dircomercio.site_backend.entities.Area;
import com.dircomercio.site_backend.entities.TipoDocumento;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class PaseCreateDTO {
    
    @NotBlank(message = "El asunto no puede estar vacio")
    @Size(min = 5, max = 200, message = "El asunto debe tener entre 5 y 200 caracteres")
    private String asunto;
    
    @NotNull(message = "La cantidad de folios no puede estar vacia")
    @Positive(message = "La cantidad de folios debe ser un número positivo")
    private Long cantFolios;
    
    @NotNull(message = "El area origen no puede estar vacia")
    private Area areaOrigen;
    
    @NotNull(message = "El area destino no puede estar vacia")
    private Area areaDestino;
    
    @NotBlank(message = "La descripcion no puede estar vacia")
    @Size(min = 5, max = 1000, message = "La descripcion debe tener entre 5 y 1000 caracteres")
    private String descripcion;

    @NotNull(message = "El ID del expediente no puede estar vacio")
    @Positive(message = "El ID del expediente debe ser un número positivo")
    private Long expedienteId;
    
    @NotNull(message = "El ID del usuario no puede estar vacio")
    @Positive(message = "El ID del usuario debe ser un numero positivo")
    private Long usuarioId;

    @NotNull(message = "El tipo de documento no puede estar vacio")
    private TipoDocumento tipoDocumento; 
}
