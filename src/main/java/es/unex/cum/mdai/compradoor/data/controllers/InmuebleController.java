package es.unex.cum.mdai.compradoor.data.controllers;

import es.unex.cum.mdai.compradoor.data.model.Inmueble;
import es.unex.cum.mdai.compradoor.data.services.InmuebleService;
import jakarta.servlet.http.HttpSession;
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

    // LISTADO PRINCIPAL (Catálogo)
    @GetMapping({"/", ""})
    public String listInmuebles(Model model, HttpSession session) {

        // OPCIONAL: Si eres ADMIN, quizás quieras ver todas (incluidas vendidas).
        // Si eres CLIENTE o ANÓNIMO, solo ves las disponibles.
        /* Cliente cliente = (Cliente) session.getAttribute("clienteLogueado");
        if (cliente != null && cliente.isAdmin()) {
             model.addAttribute("inmuebles", inmuebleService.findAllInmueble());
        } else {
             model.addAttribute("inmuebles", inmuebleService.findInmueblesDisponibles());
        }
        */

        // POR AHORA: Mostramos solo disponibles a todo el mundo en esta vista
        model.addAttribute("inmuebles", inmuebleService.findInmueblesDisponibles());

        return "inmuebles_layouts/inmuebles";
    }

    @GetMapping("/new")
    public String showInmuebleForm(Model model) {
        model.addAttribute("inmueble", new Inmueble());
        return "inmuebles_layouts/inmuebleform";
    }

    @PostMapping("/")
    public String createInmueble(@Valid @ModelAttribute Inmueble inmueble, BindingResult result) {
        if (result.hasErrors()) {
            return "inmuebles_layouts/inmuebleform";
        }
        inmuebleService.saveInmueble(inmueble);
        return "redirect:/inmuebles/";
    }

    @GetMapping("/edit/{id}")
    public String editInmueble(@PathVariable UUID id, Model model) {
        Inmueble inmueble = inmuebleService.findInmuebleById(id)
                .orElseThrow(() -> new IllegalArgumentException("Inmueble no encontrado"));
        model.addAttribute("inmueble", inmueble);
        return "inmuebles_layouts/inmuebleform";
    }

    @PostMapping("/update/{id}")
    public String updateInmueble(@PathVariable UUID id, @ModelAttribute Inmueble inmueble, BindingResult result) {
        if (result.hasErrors()) {
            return "inmuebles_layouts/inmuebleform";
        }
        inmueble.setIdInmueble(id);
        try {
            inmuebleService.updateInmueble(inmueble);
        } catch (IllegalArgumentException e) {
            result.reject("error.inmueble", e.getMessage());
            return "inmuebles_layouts/inmuebleform";
        }

        return "redirect:/inmuebles/";
    }

    // BUSCADOR (Filtrado también para ocultar vendidos)
    @GetMapping("/search")
    public String searchByPrecio(@RequestParam(required = false) Float min, @RequestParam(required = false) Float max, Model model) {
        if (min == null || max == null) {
            model.addAttribute("error", "Debe proporcionar min y max");
            // Si hay error, mostramos disponibles por defecto
            model.addAttribute("inmuebles", inmuebleService.findInmueblesDisponibles());
            return "inmuebles_layouts/inmuebles";
        }

        try {
            // Este método del servicio ahora llama a findByVentaIsNullAndPrecioBetween
            List<Inmueble> res = inmuebleService.findInmuebleByPrecioBetween(min, max);
            model.addAttribute("inmuebles", res);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("inmuebles", List.of());
        }

        return "inmuebles_layouts/inmuebles";
    }

    @PostMapping("/{id}/delete")
    public String deleteInmueble(@PathVariable UUID id) {
        inmuebleService.deleteInmueble(id);
        return "redirect:/inmuebles/";
    }
}