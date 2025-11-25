package es.unex.cum.mdai.compradoor.data.controllers;

import es.unex.cum.mdai.compradoor.data.model.Cliente;
import es.unex.cum.mdai.compradoor.data.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    public final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping("/")
    public String listClientes(Model model) {
        model.addAttribute("clientes", clienteService.findAllClientes());
        return "clientes";
    }

    @GetMapping("/new")
    public String showClienteForm(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "clienteForm";
    }

    @PostMapping("/")
    public String createCliente(@ModelAttribute Cliente cliente) {
        clienteService.saveCliente(cliente);
        return "redirect:/clientes";
    }

    @PostMapping("/{id}/delete")
    public String deleteCliente(@PathVariable UUID id) {
        clienteService.deleteCliente(id);
        return "redirect:/clientes/";
    }

}
