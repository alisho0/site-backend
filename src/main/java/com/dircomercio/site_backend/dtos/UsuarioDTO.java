//maneja datos sensibles de usuarios
package com.dircomercio.site_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {

    private Long id;
    
    @NotBlank(message = "El nombre del usuario no puede estar vacio")
    @Size(min = 2, max = 50, message = "El nombre del usuario debe tener entre 2 y 50 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "El nombre de usuario solo puede contener letras, numeros, puntos, guiones y guiones bajos")
    private String nombreUsuario;
    
    @NotBlank(message = "El email no puede estar vacio")
    @Email(message = "El email debe tener un formato valido")
    private String email;
    
    @NotBlank(message = "El rol no puede estar vacio")
    private String rol;
}
