package es.unex.cum.mdai.compradoor.data.controllers;

import es.unex.cum.mdai.compradoor.data.model.Servicio;
import es.unex.cum.mdai.compradoor.data.model.TipoServicio;
import es.unex.cum.mdai.compradoor.data.services.CompraService; // Necesario para el desplegable
import es.unex.cum.mdai.compradoor.data.services.ServicioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/servicios")
public class ServicioController {

    private final ServicioService servicioService;
    private final CompraService compraService; // Inyectamos CompraService

    @Autowired
    public ServicioController(ServicioService servicioService, CompraService compraService) {
        this.servicioService = servicioService;
        this.compraService = compraService;
    }

    // 1. Menú Principal
    @GetMapping({"", "/", "/menu"})
    public String servicioMenu() {
        return "servicios_layouts/servicios_index";
    }

    // 2. Buscador y Listado (Igual que antes, modo Admin)
    @GetMapping("/buscar")
    public String buscarServicios(
            @RequestParam(required = false) TipoServicio tipo,
            @RequestParam(required = false) String compraId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaFin,
            Model model) {

        List<Servicio> resultados = servicioService.findAllServicios();
        List<String> errores = new ArrayList<>();

        if (tipo != null) {
            resultados = resultados.stream().filter(s -> s.getTipoServicio() == tipo).collect(Collectors.toList());
        }
        if (compraId != null && !compraId.isBlank()) {
            try {
                UUID uuid = UUID.fromString(compraId);
                resultados = resultados.stream().filter(s -> s.getCompra() != null && s.getCompra().getIdCompra().equals(uuid)).collect(Collectors.toList());
            } catch (IllegalArgumentException e) {
                errores.add("ID Compra inválido.");
            }
        }
        if (fechaInicio != null && fechaFin != null) {
            resultados = resultados.stream().filter(s -> s.getFechaAplicacion() != null && !s.getFechaAplicacion().before(fechaInicio) && !s.getFechaAplicacion().after(fechaFin)).collect(Collectors.toList());
        }

        model.addAttribute("servicios", resultados);
        model.addAttribute("filtrosAplicados", true);
        model.addAttribute("tipoSeleccionado", tipo);
        model.addAttribute("compraId", compraId);
        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);
        model.addAttribute("tipos", TipoServicio.values());

        if (!errores.isEmpty()) model.addAttribute("error", String.join(", ", errores));

        return "servicios_layouts/servicios";
    }

    @GetMapping("/all")
    public String listAll(Model model) {
        return buscarServicios(null, null, null, null, model);
    }

    // 3. NUEVO SERVICIO (Recuperado)
    @GetMapping("/new")
    public String showServicioForm(Model model) {
        model.addAttribute("servicio", new Servicio());
        model.addAttribute("tipos", TipoServicio.values());
        // Pasamos TODAS las compras para que el Admin elija a cuál asignarlo
        model.addAttribute("compras", compraService.findAllCompras());
        return "servicios_layouts/servicioform";
    }

    // 4. GUARDAR SERVICIO (Recuperado)
    @PostMapping("/save")
    public String createServicio(@Valid @ModelAttribute Servicio servicio, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("tipos", TipoServicio.values());
            model.addAttribute("compras", compraService.findAllCompras());
            return "servicios_layouts/servicioform";
        }
        try {
            servicioService.saveServicio(servicio);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("tipos", TipoServicio.values());
            model.addAttribute("compras", compraService.findAllCompras());
            return "servicios_layouts/servicioform";
        }
        return "redirect:/servicios/all";
    }

    // 5. Borrar
    @PostMapping("/delete/{id}")
    public String deleteServicio(@PathVariable("id") UUID id) {
        Optional<Servicio> servicio = servicioService.findServicioById(id);
        if (servicio.isPresent()) {
            servicioService.deleteServicio(servicio.get());
        }
        return "redirect:/servicios/all";
    }
}