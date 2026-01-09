//maneja la creación de expedientes
package com.dircomercio.site_backend.dtos;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class ExpedienteCreateDTO {
    
    private Long id;
    
    @NotBlank(message = "El número de expediente no puede estar vacio")
    @Pattern(regexp = "^[A-Z0-9\\-]+$", message = "Formato de expediente invalido (ej: EXP-2026-001234)")
    private String nroExp;
    
    @NotBlank(message = "La cantidad de folios no puede estar vacia")
    @Pattern(regexp = "^[0-9]+$", message = "La cantidad de folios debe ser un numero")
    private String cantFolios;
    
    @NotBlank(message = "La fecha de inicio no puede estar vacia")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Formato de fecha invalido (YYYY-MM-DD)")
    private String fechaInicio;
    
    private String fechaFinalizacion;
    
    @NotBlank(message = "Debe indicar si es hipervulnerable")
    @Pattern(regexp = "^(SI|NO|si|no)$", message = "Debe ser SI o NO")
    private String hipervulnerable;
    
    @NotBlank(message = "La delegacion no puede estar vacía")
    @Size(min = 2, max = 100, message = "La delegacion debe tener entre 2 y 100 caracteres")
    private String delegacion;
    
    @NotNull(message = "El ID de usuario no puede estar vacio")
    @Positive(message = "El ID de usuario debe ser un número positivo")
    private Long usuarioId;
}

    

