package com.dircomercio.site_backend.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dircomercio.site_backend.dtos.CambiarPasswordDTO;
import com.dircomercio.site_backend.dtos.PerfilDTO;
import com.dircomercio.site_backend.dtos.UsuarioDTO;
import com.dircomercio.site_backend.implementation.UsuarioServiceImpl;
import com.dircomercio.site_backend.repositories.UsuarioRepository;
import com.dircomercio.site_backend.services.AuditoriaService;
import com.dircomercio.site_backend.utils.IpUtil;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioServiceImpl usuarioServiceImpl;

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

    @GetMapping("/traerUsuarios")
    public ResponseEntity<?> traerUsuarios() {
        try {
            List<UsuarioDTO> usuarios = usuarioServiceImpl.traerUsuarios();
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error obteniendo los usuarios: "+ e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> traerUsuarioPorId(@PathVariable Long id) {
        try {
            PerfilDTO perfil = usuarioServiceImpl.traerUsuarioPorId(id);
            return ResponseEntity.ok(perfil);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    // registrar auditoria en actualizacion
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarUsuario(@PathVariable Long id, @Valid @RequestBody PerfilDTO perfilDTO, HttpServletRequest request) {
        try {
            usuarioServiceImpl.actualizarUsuarioPorId(id, perfilDTO);
            auditoriaService.registrarAccion(obtenerUsuarioActual(), "ACTUALIZAR_USUARIO", 
                "Usuario ID " + id + " actualizado", IpUtil.obtenerIP(request), 
                "SUCCESS", "Usuario", id);
            return ResponseEntity.ok("Usuario actualizado correctamente");
        } catch (Exception e) {
            auditoriaService.registrarAccion(obtenerUsuarioActual(), "ACTUALIZAR_USUARIO", 
                "Error al actualizar usuario ID " + id + ": " + e.getMessage(), IpUtil.obtenerIP(request), 
                "FAILURE", "Usuario", id);
            return ResponseEntity.badRequest().body("Error al actualizar el usuario: " + e.getMessage());
        }
    }

    // registrar auditoria en eliminacion
    @DeleteMapping("/borrar/{id}")
    public ResponseEntity<String> borrarUsuario(@PathVariable Long id, HttpServletRequest request) {
        try {
            usuarioServiceImpl.borrarUsuario(id);
            auditoriaService.registrarAccion(obtenerUsuarioActual(), "ELIMINAR_USUARIO", 
                "Usuario ID " + id + " eliminado", IpUtil.obtenerIP(request), 
                "SUCCESS", "Usuario", id);
            return ResponseEntity.ok("Usuario borrado correctamente");
        } catch (Exception e) {
            auditoriaService.registrarAccion(obtenerUsuarioActual(), "ELIMINAR_USUARIO", 
                "Error al eliminar usuario ID " + id + ": " + e.getMessage(), IpUtil.obtenerIP(request), 
                "FAILURE", "Usuario", id
            );
            return ResponseEntity.badRequest().body("Error al borrar el usuario: " + e.getMessage());
        }
    }

    @GetMapping("/perfilUsuario")
    public ResponseEntity<?> obtenerPerfil() {
        try {
            PerfilDTO perfil = usuarioServiceImpl.obtenerPerfil();
            return ResponseEntity.ok(perfil);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // registrar auditoria en cambio de nombre
    @PutMapping("/actualizarNombre")
    public ResponseEntity<String> actualizarNombre(@Valid @RequestBody PerfilDTO perfilDTO, HttpServletRequest request) {
        try {
            usuarioServiceImpl.actualizarPerfil(perfilDTO);
            auditoriaService.registrarAccion(obtenerUsuarioActual(), "ACTUALIZAR_NOMBRE", 
                "Nombre de usuario actualizado: " + perfilDTO.getNombre(), IpUtil.obtenerIP(request), 
                "SUCCESS", "Usuario", null);
            return ResponseEntity.ok("Perfil actualizado correctamente");
        } catch (Exception e) {
            auditoriaService.registrarAccion(obtenerUsuarioActual(), "ACTUALIZAR_NOMBRE", 
                "Error al actualizar nombre: " + e.getMessage(), IpUtil.obtenerIP(request), 
                "FAILURE", "Usuario", null);
            return ResponseEntity.badRequest().body("Error al actualizar el perfil: " + e.getMessage());
        }
    }

    // registrar auditoria en cambio de password
    @PutMapping("/cambiarPassword")
    public ResponseEntity<String> cambiarPassword(@Valid @RequestBody CambiarPasswordDTO dto, HttpServletRequest request) {
        try {
            usuarioServiceImpl.cambiarPassword(dto);
            auditoriaService.registrarAccion(obtenerUsuarioActual(), "CAMBIAR_PASSWORD", 
                "Contrasena actualizada por usuario", IpUtil.obtenerIP(request), 
                "SUCCESS", "Usuario", null);
            return ResponseEntity.ok("Contraseña actualizada correctamente");
        } catch (Exception e) {
            auditoriaService.registrarAccion(obtenerUsuarioActual(), "CAMBIAR_PASSWORD", 
                "Error al cambiar contrasena: " + e.getMessage(), IpUtil.obtenerIP(request), 
                "FAILURE", "Usuario", null);
            return ResponseEntity.badRequest().body("Error al cambiar la contraseña: " + e.getMessage());
        }
    }
}