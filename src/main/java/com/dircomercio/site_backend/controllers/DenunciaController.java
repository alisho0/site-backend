package com.dircomercio.site_backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dircomercio.site_backend.services.DenunciaService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/denuncia")
public class DenunciaController {

    @Autowired
    DenunciaService denunciaService;

    @PostMapping("/guardarDenuncia")
    public String postMethodName(@RequestBody String formulario) {
        return "String";
    }
    
}
