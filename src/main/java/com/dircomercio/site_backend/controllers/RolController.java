package com.dircomercio.site_backend.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dircomercio.site_backend.entities.Rol;
import com.dircomercio.site_backend.entities.Usuario;
import com.dircomercio.site_backend.repositories.RolRepository;
import com.dircomercio.site_backend.repositories.UsuarioRepository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/rol")
@CrossOrigin(origins = "http://localhost:5173")
public class RolController {

    @Autowired
    RolRepository rolRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @PostMapping("/crearRol")
    public ResponseEntity<?> crearRol(@RequestBody Rol rol) {
        rolRepository.save(rol);
        return ResponseEntity.ok("Rol creado correctamente: " + rol.getNombre());
    }

    @GetMapping("/traerRol")
    public ResponseEntity<?> traerRol() {
        List<Rol> roles = (List<Rol>) rolRepository.findAll();
        return ResponseEntity.ok(roles);
    }
    
    @GetMapping("/usuarios")
    public ResponseEntity<?> traerUsuarios() {
        List<Usuario> u = (List<Usuario>) usuarioRepository.findAll();
        List<Usuario> usu = new ArrayList<>();
        for (Usuario usuario : u) {
            Usuario us = Usuario.builder()
                .email(usuario.getEmail())
                .contraseña(usuario.getContraseña())
                .rol(usuario.getRol())
                .build();
            usu.add(us);
        }
        return ResponseEntity.ok(usu);
    }
    
}
