package com.dircomercio.site_backend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
public class FrontendController {
    
    @RequestMapping(value = {
        "/", 
        "/{path:^(?!api|auth|usuarios|denuncia|expediente|doc|pases|rol|assets|.*\\..*).*}"
    })
    public String forward() {
        return "forward:/index.html";
    }

}
