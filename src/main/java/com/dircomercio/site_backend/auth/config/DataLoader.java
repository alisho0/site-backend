package com.dircomercio.site_backend.auth.config;

import com.dircomercio.site_backend.entities.Area;
import com.dircomercio.site_backend.entities.Rol;
import com.dircomercio.site_backend.repositories.RolRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final RolRepository rolRepository;
    private Area area;

    @Override
    public void run(String... args) throws Exception {
        initializeDefaultRoles();
    }

    private void initializeDefaultRoles() {
        
        List<Area> rolesToCreate = Arrays.asList(
            Area.DIRECCION,
            Area.ABOGADOS,
            Area.ASESORIA_LEGAL,
            Area.MESA_DE_ENTRADA
        );

        for (Area area : rolesToCreate) {
            String roleName = area.name();
            
            // 1. Verificar si el rol ya existe
            rolRepository.findByNombre(roleName)
                .ifPresentOrElse(
                    // Si el rol EXISTE, no hacemos nada (idempotente)
                    rol -> System.out.println("Rol " + roleName + " ya existe."),
                    
                    // Si el rol NO EXISTE, lo creamos
                    () -> {
                        Rol newRol = new Rol();
                        newRol.setNombre(roleName); 
                        
                        rolRepository.save(newRol);
                        System.out.println("Rol " + roleName + " creado.");
                    }
                );
        }
    }
}

