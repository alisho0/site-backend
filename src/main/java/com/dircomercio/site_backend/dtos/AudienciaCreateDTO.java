//maneja la creación de audiencias
package com.dircomercio.site_backend.dtos;

import lombok.Data;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AudienciaCreateDTO {
    
    @NotNull(message = "La fecha no puede estar vacia")
    @FutureOrPresent(message = "La fecha debe ser presente o futura")
    private LocalDateTime fecha;
    
    @NotBlank(message = "La hora no puede estar vacia")
    @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Formato de hora invalido (HH:mm)")
    private String hora;
    
    @NotBlank(message = "El lugar no puede estar vacio")
    @Size(min = 3, max = 200, message = "El lugar debe tener entre 3 y 200 caracteres")
    private String lugar;
    
    @NotNull(message = "El ID del expediente no puede estar vacio")
    @Positive(message = "El ID del expediente debe ser un numero positivo")
    private Long expedienteId;
    
    @NotNull(message = "Las personas no pueden estar vacias")
    @NotEmpty(message = "Debe incluir al menos una persona")
    private List<Long> personasIds;
}
