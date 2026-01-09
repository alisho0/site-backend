//maneja roles asignados a personas
package com.dircomercio.site_backend.dtos;

import com.dircomercio.site_backend.entities.Persona;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class PersonaRolDTO {
    
    @NotNull(message = "La persona no puede estar vacia")
    private Persona persona;
    
    @NotBlank(message = "El rol no puede estar vacio")
    @Size(min = 2, max = 50, message = "El rol debe tener entre 2 y 50 caracteres")
    private String rol;
    
    private String nombreDelegado;
    
    private String apellidoDelegado;
    
    @Pattern(regexp = "^[0-9]{7,8}$", message = "DNI debe contener entre 7 y 8 dígitos")
    private String dniDelegado;
}
