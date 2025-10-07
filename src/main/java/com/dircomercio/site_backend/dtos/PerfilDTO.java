package com.dircomercio.site_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerfilDTO {
    private String name;
    private String email;
    private String area; // Rol del usuario

    private String nombre;
    private String apellido;
    private String documento;
    private String telefono;
    private String domicilio;
    private String localidad;
    private String cp;
}
