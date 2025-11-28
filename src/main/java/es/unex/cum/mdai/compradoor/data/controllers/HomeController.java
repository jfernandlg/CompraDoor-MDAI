package es.unex.cum.mdai.compradoor.data.controllers;

import es.unex.cum.mdai.compradoor.data.model.Cliente;
import es.unex.cum.mdai.compradoor.data.services.ClienteService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class HomeController {

    private final ClienteService clienteService;

    public HomeController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping({"", "/"})
    public String index(HttpSession httpSession, Model model) {
        model.addAttribute("cliente", httpSession.getAttribute("clienteLogueado"));
        return "index_client";
    }

    @GetMapping("/login")
    public String login(HttpSession session) {
        if (session.getAttribute("clienteLogueado") != null) {
            return "redirect:/";
        }
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String email, @RequestParam String password,
                               HttpSession httpSession, Model model) {

        Optional<Cliente> clienteOptional = clienteService.findClienteByEmail(email);

        if (clienteOptional.isPresent()) {
            Cliente cliente = clienteOptional.get();
            if (cliente.getPassword() != null && cliente.getPassword().equals(password)) {
                httpSession.setAttribute("clienteLogueado", cliente);
                return "redirect:/";
            }
        }

        model.addAttribute("error", "Credenciales incorrectas");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        httpSession.invalidate();
        return "redirect:/";
    }

    @GetMapping("/register")
    public String register(HttpSession session, Model model) {
        if (session.getAttribute("clienteLogueado") != null) {
            return "redirect:/";
        }
        model.addAttribute("cliente", new Cliente());
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@Valid @ModelAttribute Cliente cliente, HttpSession session, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "register";
        }

        try {
            clienteService.saveCliente(cliente);
            session.setAttribute("clienteLogueado", cliente);

        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            return "register";
        }

        return "redirect:/";
    }
}