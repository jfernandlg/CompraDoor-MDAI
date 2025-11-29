package es.unex.cum.mdai.compradoor.data.controllers;

import es.unex.cum.mdai.compradoor.data.model.Cliente;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping({"", "/"})
    public String adminController(HttpSession session) {
        Cliente cliente = (Cliente) session.getAttribute("clienteLogueado");

        if (cliente == null || !cliente.isAdmin()) {
            return "redirect:/";
        }

        return "home_layout/index";
    }
}
