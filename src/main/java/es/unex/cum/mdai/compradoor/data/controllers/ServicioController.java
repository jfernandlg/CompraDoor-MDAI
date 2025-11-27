package es.unex.cum.mdai.compradoor.data.controllers;

import es.unex.cum.mdai.compradoor.data.model.Servicio;
import es.unex.cum.mdai.compradoor.data.services.ServicioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/servicios")
public class ServicioController {

    private final ServicioService servicioService;

    @Autowired
    public ServicioController(ServicioService servicioService) {
        this.servicioService = servicioService;
    }

    @GetMapping({"", "/", "/menu"})
    public String servicioMenu() {
        return "servicios_index";
    }

    @GetMapping("/all")
    public String listServicios(Model model) {
        model.addAttribute("servicios", servicioService.findAllServicios());
        return "servicios";
    }

    @GetMapping("/new")
    public String showServicioForm(Model model) {
        model.addAttribute("servicio", new Servicio());
        return "servicioform";
    }

    @PostMapping("/")
    public String createServicio(@Valid @ModelAttribute Servicio servicio, BindingResult result) {
        if (result.hasErrors()) {
            return "servicioform";
        }

        servicioService.saveServicio(servicio);
        return "redirect:/servicios/all";
    }

    @PostMapping("/{id}/delete")
    public String deleteServicio(@PathVariable("id") String idStr) {
        // buscar por id recorriendo la lista y eliminar si existe
        servicioService.findAllServicios().stream()
                .filter(s -> s.getIdServicio() != null && s.getIdServicio().toString().equals(idStr))
                .findFirst()
                .ifPresent(servicioService::deleteServicio);
        return "redirect:/servicios/all";
    }

}
