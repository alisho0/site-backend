package com.dircomercio.site_backend.auth.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RegisterRequest(
    String email,
    String password,
    String name,
    String rol // nuevo campo para el nombre del rol
) {

}
