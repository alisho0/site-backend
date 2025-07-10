package com.dircomercio.site_backend.controllers;

import com.dircomercio.site_backend.dtos.CambiarPasswordDTO;
import com.dircomercio.site_backend.dtos.PerfilDTO;
import com.dircomercio.site_backend.entities.Usuario;
import com.dircomercio.site_backend.repositories.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/perfil")
@CrossOrigin(origins = "http://localhost:5173")
public class PerfilController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<PerfilDTO> obtenerPerfil(Authentication authentication) {
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        PerfilDTO perfilDTO = PerfilDTO.builder()
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .sector(usuario.getRol() != null ? usuario.getRol().getNombre() : null)
                .build();

        return ResponseEntity.ok(perfilDTO);
    }

    @PutMapping
    public ResponseEntity<String> actualizarNombre(Authentication authentication, @RequestBody PerfilDTO perfilDTO) {
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setNombre(perfilDTO.getNombre());
        usuarioRepository.save(usuario);

        return ResponseEntity.ok("Nombre actualizado correctamente");
    }

    @PutMapping("/password")
    public ResponseEntity<String> cambiarPassword(Authentication authentication, @RequestBody CambiarPasswordDTO dto) {
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(dto.getActual(), usuario.getContraseña())) {
            return ResponseEntity.badRequest().body("La contraseña actual no es correcta");
        }

        if (!dto.getNueva().equals(dto.getRepetir())) {
            return ResponseEntity.badRequest().body("Las contraseñas nuevas no coinciden");
        }

        if (!validarPassword(dto.getNueva())) {
            return ResponseEntity.badRequest().body("La nueva contraseña no cumple con los requisitos: mínimo 8 caracteres, una mayúscula y un número");
        }

        usuario.setContraseña(passwordEncoder.encode(dto.getNueva()));
        usuarioRepository.save(usuario);

        return ResponseEntity.ok("Contraseña actualizada correctamente");
    }

    private boolean validarPassword(String password) {
        // Requisitos: mínimo 8, una mayúscula, un número
        return password != null && password.matches("^(?=.*[A-Z])(?=.*\\d).{8,}$");
    }
}
