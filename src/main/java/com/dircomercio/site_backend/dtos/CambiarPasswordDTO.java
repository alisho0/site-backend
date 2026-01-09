//maneja contraseñas
package com.dircomercio.site_backend.dtos;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class CambiarPasswordDTO {
    
    @NotBlank(message = "La contraseña actual no puede estar vacia")
    private String actual;
    
    @NotBlank(message = "La nueva contraseña no puede estar vacia")
    @Size(min = 8, max = 128, message = "La contraseña debe tener entre 8 y 128 caracteres")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
        message = "La contraseña debe contener mayusculas, minusculas, numeros y caracteres especiales (@$!%*?&)"
    )
    private String nueva;
    
    @NotBlank(message = "Debe confirmar la contraseña")
    private String repetir;
}
