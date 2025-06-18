package com.dircomercio.site_backend.auth.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import com.dircomercio.site_backend.entities.Usuario;
import com.dircomercio.site_backend.repositories.UsuarioRepository;
import java.util.Optional;

@Component
public class AuthUtil {
    private final UsuarioRepository usuarioRepository;

    public AuthUtil(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        String email = authentication.getName();
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        return usuarioOpt.orElse(null);
    }
}
