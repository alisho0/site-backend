package com.dircomercio.site_backend.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dircomercio.site_backend.entities.Rol;
import com.dircomercio.site_backend.entities.Usuario;
import com.dircomercio.site_backend.repositories.RolRepository;
import com.dircomercio.site_backend.repositories.UsuarioRepository;
import com.dircomercio.site_backend.services.AuditoriaService;
import com.dircomercio.site_backend.utils.IpUtil;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.servlet.http.HttpServletRequest;



@RestController
@RequestMapping("/rol")
public class RolController {

    @Autowired
    RolRepository rolRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    private AuditoriaService auditoriaService;

    private String obtenerUsuarioActual() {
        try {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                return authentication.getName();
            }
        } catch (Exception e) {
        }
        return "ANONIMO";
    }

    // registrar auditoria en creacion
    @PostMapping("/crearRol")
    public ResponseEntity<?> crearRol(@RequestBody Rol rol, HttpServletRequest request) {
        try {
            rolRepository.save(rol);
            auditoriaService.registrarAccion(obtenerUsuarioActual(), "CREAR_ROL", 
                "Rol creado: " + rol.getNombre(), IpUtil.obtenerIP(request), 
                "SUCCESS", "Rol", rol.getId());
            return ResponseEntity.ok("Rol creado correctamente: " + rol.getNombre());
        } catch (Exception e) {
            auditoriaService.registrarAccion(obtenerUsuarioActual(), "CREAR_ROL", 
                "Error al crear rol: " + e.getMessage(), IpUtil.obtenerIP(request), 
                "FAILURE", "Rol", null);
            return ResponseEntity.badRequest().body("Error al crear el rol: " + e.getMessage());
        }
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
