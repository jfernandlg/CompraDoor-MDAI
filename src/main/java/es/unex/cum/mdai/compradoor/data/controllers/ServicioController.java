package es.unex.cum.mdai.compradoor.data.controllers;

import es.unex.cum.mdai.compradoor.data.model.Compra;
import es.unex.cum.mdai.compradoor.data.model.Servicio;
import es.unex.cum.mdai.compradoor.data.model.TipoServicio;
import es.unex.cum.mdai.compradoor.data.services.CompraService;
import es.unex.cum.mdai.compradoor.data.services.ServicioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor; // IMPORTANTE
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat; // IMPORTANTE
import java.util.*;

@Controller
@RequestMapping("/servicios")
public class ServicioController {

    private final ServicioService servicioService;
    private final CompraService compraService;

    @Autowired
    public ServicioController(ServicioService servicioService, CompraService compraService) {
        this.servicioService = servicioService;
        this.compraService = compraService;
    }

    // --- CORRECCIÓN DEL ERROR DE FECHA Y STRINGS ---
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // 1. Convierte Strings vacíos en NULL
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));

        // 2. Convierte Fechas del HTML (yyyy-MM-dd) a java.util.Date
        // El 'true' final permite que la fecha esté vacía (null)
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @GetMapping({"", "/", "/menu"})
    public String servicioMenu() {
        return "servicios_layouts/servicios_index";
    }

    @GetMapping("/all")
    public String listServicios(Model model) {
        model.addAttribute("servicios", servicioService.findAllServicios());
        return "servicios_layouts/servicios";
    }

    @GetMapping("/new")
    public String showServicioForm(Model model) {
        model.addAttribute("servicio", new Servicio());
        model.addAttribute("tipos", TipoServicio.values());
        model.addAttribute("compras", compraService.findAllCompras());
        return "servicios_layouts/servicioform";
    }

    @PostMapping("/save")
    public String createServicio(@Valid @ModelAttribute Servicio servicio,
                                 BindingResult result,
                                 @RequestParam(value = "compraId", required = false) String compraId,
                                 Model model) {

        if (result.hasErrors()) {
            model.addAttribute("tipos", TipoServicio.values());
            model.addAttribute("compras", compraService.findAllCompras());
            return "servicios_layouts/servicioform";
        }

        try {
            // Gestión manual de la compra para evitar errores de conversión
            if (compraId != null && !compraId.trim().isEmpty()) {
                Optional<Compra> compraOpt = compraService.findCompraById(UUID.fromString(compraId));
                if (compraOpt.isPresent()) {
                    servicio.setCompra(compraOpt.get());
                } else {
                    throw new IllegalArgumentException("La compra seleccionada no existe.");
                }
            } else {
                servicio.setCompra(null); // Es un servicio de catálogo
            }

            servicioService.saveServicio(servicio);

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("tipos", TipoServicio.values());
            model.addAttribute("compras", compraService.findAllCompras());
            return "servicios_layouts/servicioform";
        }
        return "redirect:/servicios/all";
    }

    @PostMapping("/delete/{id}")
    public String deleteServicio(@PathVariable("id") UUID id) {
        servicioService.findServicioById(id).ifPresent(servicioService::deleteServicio);
        return "redirect:/servicios/all";
    }
}