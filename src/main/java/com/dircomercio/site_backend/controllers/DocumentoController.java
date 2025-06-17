package com.dircomercio.site_backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dircomercio.site_backend.services.DocumentoService;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@RestController
@NoArgsConstructor
@AllArgsConstructor
@RequestMapping("/doc")
public class DocumentoController {

    @Autowired
    DocumentoService documentoService;
}
