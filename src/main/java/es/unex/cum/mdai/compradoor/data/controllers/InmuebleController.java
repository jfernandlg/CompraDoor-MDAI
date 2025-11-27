package es.unex.cum.mdai.compradoor.data.controllers;

import es.unex.cum.mdai.compradoor.data.model.Inmueble;
import es.unex.cum.mdai.compradoor.data.services.InmuebleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/inmuebles")
public class InmuebleController {

    private final InmuebleService inmuebleService;

    @Autowired
    public InmuebleController(InmuebleService inmuebleService) {
        this.inmuebleService = inmuebleService;
    }

    @GetMapping({"/", ""})
    public String listInmuebles(Model model) {
        model.addAttribute("inmuebles", inmuebleService.findAllInmueble());
        return "inmuebles";
    }

    @GetMapping("/new")
    public String showInmuebleForm(Model model) {
        model.addAttribute("inmueble", new Inmueble());
        return "inmuebleform";
    }

    @PostMapping("/")
    public String createInmueble(@Valid @ModelAttribute Inmueble inmueble, BindingResult result) {
        if (result.hasErrors()) {
            return "inmuebleform";
        }

        try {
            inmuebleService.saveInmueble(inmueble);
        } catch (IllegalArgumentException e) {
            result.reject("error.inmueble", e.getMessage());
            return "inmuebleform";
        }

        return "redirect:/inmuebles/";
    }

    @PostMapping("/{id}/delete")
    public String deleteInmueble(@PathVariable UUID id) {
        inmuebleService.deleteInmueble(id);
        return "redirect:/inmuebles/";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable UUID id, Model model) {
        Inmueble inmueble = inmuebleService.findInmuebleById(id)
                .orElseThrow(() -> new IllegalArgumentException("Inmueble no encontrado"));
        model.addAttribute("inmueble", inmueble);
        return "inmuebleform";
    }

    @PostMapping("/update/{id}")
    public String updateInmueble(@PathVariable UUID id, @ModelAttribute Inmueble inmueble, BindingResult result) {
        if (result.hasErrors()) {
            return "inmuebleform";
        }
        inmueble.setIdInmueble(id);
        try {
            inmuebleService.updateInmueble(inmueble);
        } catch (IllegalArgumentException e) {
            result.reject("error.inmueble", e.getMessage());
            return "inmuebleform";
        }

        return "redirect:/inmuebles/";
    }

    @GetMapping("/search")
    public String searchByPrecio(@RequestParam(required = false) Float min, @RequestParam(required = false) Float max, Model model) {
        if (min == null || max == null) {
            model.addAttribute("error", "Debe proporcionar min y max");
            model.addAttribute("inmuebles", List.of());
            return "inmuebles";
        }

        try {
            List<Inmueble> res = inmuebleService.findInmuebleByPrecioBetween(min, max);
            model.addAttribute("inmuebles", res);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("inmuebles", List.of());
        }

        return "inmuebles";
    }
}
