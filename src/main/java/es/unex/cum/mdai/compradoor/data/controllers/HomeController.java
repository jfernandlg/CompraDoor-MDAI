package es.unex.cum.mdai.compradoor.data.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // Esto atiende a "localhost:8080/" (la raíz)
    @GetMapping({"","/"})
    public String index() {
        return "home_layout/index"; // Busca src/main/resources/templates/index.html
    }
}