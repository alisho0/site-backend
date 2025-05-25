package com.dircomercio.site_backend.dtos;

import lombok.Data;

@Data
public class PersonaConRolDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String cp;
    private String localidad;
    private String documento;
    private String domicilio;
    private String fax;
    private String rol;
    private String nombreDelegado;
    private String apellidoDelegado;
    private String dniDelegado;
}
