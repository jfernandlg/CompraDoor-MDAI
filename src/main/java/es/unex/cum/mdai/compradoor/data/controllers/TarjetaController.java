package es.unex.cum.mdai.compradoor.data.controllers;

import es.unex.cum.mdai.compradoor.data.model.Tarjeta;
import es.unex.cum.mdai.compradoor.data.services.ClienteService;
import es.unex.cum.mdai.compradoor.data.services.TarjetaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/tarjetas")
public class TarjetaController {

    private final TarjetaService tarjetaService;
    private final ClienteService clienteService;

    @Autowired
    public TarjetaController(TarjetaService tarjetaService, ClienteService clienteService) {
        this.tarjetaService = tarjetaService;
        this.clienteService = clienteService;
    }

    @GetMapping({"", "/", "/menu"})
    public String tarjetaMenu() {
        return "tarjetas_index";
    }

    @GetMapping("/all")
    public String listTarjetas(Model model) {
        // no hay findAll en el servicio, se puede obtener por clientes o por buscar null; aquí no hay método, asumimos que buscar por cliente no es obligatorio
        model.addAttribute("tarjetas", List.of());
        return "tarjetas";
    }

    @GetMapping("/new")
    public String showTarjetaForm(Model model) {
        model.addAttribute("tarjeta", new Tarjeta());
        model.addAttribute("clientes", clienteService.findAllClientes());
        return "tarjetaform";
    }

    @PostMapping("/")
    public String createTarjeta(@Valid @ModelAttribute Tarjeta tarjeta, BindingResult result) {
        if (result.hasErrors()) {
            return "tarjetaform";
        }
        tarjetaService.saveTarjeta(tarjeta);
        return "redirect:/tarjetas/all";
    }

    @PostMapping("/{id}/delete")
    public String deleteTarjeta(@PathVariable UUID id) {
        tarjetaService.findById(id).ifPresent(tarjetaService::deleteTarjeta);
        return "redirect:/tarjetas/all";
    }

}
