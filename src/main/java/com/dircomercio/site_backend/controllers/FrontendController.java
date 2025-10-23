package com.dircomercio.site_backend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FrontendController {

    @RequestMapping(value = { "/", "/{path:^(?!api|auth|denuncia|usuarios|expediente|doc|pases|rol).*}" })
    public String forward() {
        return "forward:/index.html";
    }
}

