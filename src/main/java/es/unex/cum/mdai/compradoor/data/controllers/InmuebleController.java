package es.unex.cum.mdai.compradoor.data.controllers;

import es.unex.cum.mdai.compradoor.data.model.Inmueble;
import es.unex.cum.mdai.compradoor.data.services.InmuebleService;
import es.unex.cum.mdai.compradoor.data.services.StorageService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/inmuebles")
public class InmuebleController {

    private final InmuebleService inmuebleService;

    private final StorageService storageService;

    @Autowired
    public InmuebleController(InmuebleService inmuebleService, StorageService storageService) {
        this.inmuebleService = inmuebleService;
        this.storageService = storageService;
    }

    // LISTADO PRINCIPAL (Catálogo)
    @GetMapping({"/", ""})
    public String listInmuebles(Model model, HttpSession session) {

        model.addAttribute("inmuebles", inmuebleService.findInmueblesDisponibles());

        return "inmuebles_layouts/inmuebles";
    }

    @GetMapping("/new")
    public String showInmuebleForm(Model model) {
        model.addAttribute("inmueble", new Inmueble());
        return "inmuebles_layouts/inmuebleform";
    }

    @PostMapping("/")
    public String createInmueble(@Valid @ModelAttribute Inmueble inmueble,
                                 BindingResult result,
                                 @RequestParam(value = "archivos", required = false) MultipartFile[] archivos) {

        if (result.hasErrors()) {
            return "inmuebles_layouts/inmuebleform";
        }

        try {
            List<String> rutasFotos = new ArrayList<>();

            if (archivos != null && archivos.length > 0 && !archivos[0].isEmpty()) {

                String nombreCarpeta = storageService.generarNombreNuevaCarpeta(); // Ej: inmueble5

                for (MultipartFile archivo : archivos) {
                    if (!archivo.isEmpty()) {
                        String rutaURL = storageService.store(archivo, nombreCarpeta);
                        rutasFotos.add(rutaURL);
                    }
                }
            }

            if (!rutasFotos.isEmpty()) {
                inmueble.setPathFotos(rutasFotos);
            }

            inmuebleService.saveInmueble(inmueble);

        } catch (Exception e) {
            result.reject("error.global", "Error al subir fotos: " + e.getMessage());
            return "inmuebles_layouts/inmuebleform";
        }

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