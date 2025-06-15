package com.dircomercio.site_backend.auth.controller;

public record LoginRequest(
    String email,
    String password
) {

}
