package es.unex.cum.mdai.compradoor.data.controllers;

import es.unex.cum.mdai.compradoor.data.model.Cliente;
import es.unex.cum.mdai.compradoor.data.services.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    public final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public String listClientes(Model model) {
        model.addAttribute("clientes", clienteService.findAllClientes());
        return "clientes";
    }

    @GetMapping("/new")
    public String showClienteForm(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "clienteform";
    }

    @PostMapping
    public String createCliente(@Valid @ModelAttribute Cliente cliente, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "clienteform";
        }

        try {
            clienteService.saveCliente(cliente);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().toLowerCase().contains("dni")) {
                result.rejectValue("dni", "error.cliente", e.getMessage());
            } else if (e.getMessage().toLowerCase().contains("email")) {
                result.rejectValue("email", "error.cliente", e.getMessage());
            } else {
                result.reject("error.global", e.getMessage());
            }
            return "clienteform";
        }

        return "redirect:/clientes";
    }

    @PostMapping("/{id}/delete")
    public String deleteCliente(@PathVariable UUID id) {
        clienteService.deleteCliente(id);
        return "redirect:/clientes";
    }

    @GetMapping("/edit/{id}")
    public String showEditClienteForm(@PathVariable UUID id, Model model) {

        Cliente cliente = clienteService.findClienteById(id).
                orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));

        model.addAttribute("cliente", cliente);
        return "clienteform";
    }

    @PostMapping("/update/{id}")
    public String updateCliente(@PathVariable("id") UUID id, @Valid @ModelAttribute Cliente cliente, BindingResult result) {

        if (result.hasErrors()) {
            return "clienteform";
        }

        try {
            cliente.setId(id);
            clienteService.updateCliente(cliente);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().toLowerCase().contains("dni")) {
                result.rejectValue("dni", "error.cliente", e.getMessage());
            } else if (e.getMessage().toLowerCase().contains("email")) {
                result.rejectValue("email", "error.cliente", e.getMessage());
            } else {
                result.reject("error.global", e.getMessage());
            }
            return "clienteform";
        }

        return "redirect:/clientes";
    }

    @GetMapping("/search")
    public String searchCliente(@RequestParam("query") String query, Model model) {
        Optional<Cliente> optionalCliente = clienteService.findClienteByDni(query);

        if (optionalCliente.isPresent()) {
            model.addAttribute("clientes", List.of(optionalCliente.get()));
        } else {
            model.addAttribute("clientes", List.of());
            model.addAttribute("error", "cliente no encontrado");
        }

        return "clientes";
    }

}
