package com.dircomercio.site_backend.controllers;

import com.dircomercio.site_backend.entities.Area;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/areas")
public class AreaController {
    @GetMapping
    public Area[] getAreas() {
        return Area.values();
    }
}
