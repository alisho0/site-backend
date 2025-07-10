package com.dircomercio.site_backend.dtos;

import lombok.Data;

@Data
public class CambiarPasswordDTO {
    private String actual;  // Contraseña actual
    private String nueva;   // Nueva contraseña
    private String repetir; // Repetir nueva contraseña (mejora sugerida)
}
