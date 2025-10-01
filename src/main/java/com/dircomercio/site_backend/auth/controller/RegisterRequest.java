package com.dircomercio.site_backend.auth.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RegisterRequest(
    String email,
    String password,
    String name,
    String rol, // nuevo campo para el nombre del rol
    String nombre,
    String apellido,
    String telefono,
    String cp,
    String localidad,
    String documento,
    String domicilio
) {

}
