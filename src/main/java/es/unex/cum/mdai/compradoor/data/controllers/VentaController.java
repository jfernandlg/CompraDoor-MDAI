package es.unex.cum.mdai.compradoor.data.controllers;

import es.unex.cum.mdai.compradoor.data.model.Cliente;
import es.unex.cum.mdai.compradoor.data.model.Inmueble;
import es.unex.cum.mdai.compradoor.data.model.Venta;
import es.unex.cum.mdai.compradoor.data.services.ClienteService;
import es.unex.cum.mdai.compradoor.data.services.InmuebleService;
import es.unex.cum.mdai.compradoor.data.services.VentaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/ventas")
public class VentaController {

    public final VentaService ventaService;
    public final ClienteService clienteService;
    public final InmuebleService inmuebleService;

    @Autowired
    public VentaController(VentaService ventaService, ClienteService clienteService, InmuebleService inmuebleService) {
        this.ventaService = ventaService;
        this.clienteService = clienteService;
        this.inmuebleService = inmuebleService;
    }


    @GetMapping
    public String listVentas(Model model) {
        model.addAttribute("ventas", ventaService.findAllVentas());
        return "ventas";
    }

    @GetMapping("/new")
    public String showVentaForm(Model model) {
        model.addAttribute("venta", new Venta());

        model.addAttribute("listaClientes", clienteService.findAllClientes());
        model.addAttribute("listaInmuebles", inmuebleService.findAllInmueble());
        return "ventaform";
    }

    @PostMapping
    public String createVenta(@Valid @ModelAttribute Venta venta, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("listaClientes", clienteService.findAllClientes());
            model.addAttribute("listaInmuebles", inmuebleService.findAllInmueble());
            return "ventaform";
        }

        try {
            ventaService.saveVenta(venta);
        } catch (IllegalArgumentException e) {
            result.reject("error.global", e.getMessage());

            model.addAttribute("listaClientes", clienteService.findAllClientes());
            model.addAttribute("listaInmuebles", inmuebleService.findAllInmueble());
            return "ventaform";
        }

        return "redirect:/ventas";
    }

    @PostMapping("/{id}/delete")
    public String deleteVenta(@PathVariable UUID id) {

        ventaService.findVentaById(id).ifPresent(ventaService::deleteVenta);
        return "redirect:/ventas";
    }

    @GetMapping("/search_clientes")
    public String searchVentaByCliente(@RequestParam("query") String query, Model model) {

        try {

            Optional<Cliente> optionalCliente = clienteService.findClienteByDni(query);
            if (optionalCliente.isPresent()) {

                List<Venta> ventas = ventaService.findVentaByCliente(optionalCliente.get());
                model.addAttribute("ventas", ventas);
            }else{
                model.addAttribute("ventas", Collections.emptyList());
                model.addAttribute("error", "No se encontro el cliente");
            }

        } catch (Exception e) {
            model.addAttribute("ventas", Collections.emptyList());
            model.addAttribute("error", "Error en la búsqueda");
        }

        return "ventas";
    }

    @GetMapping("/search")
    public String searchVentasByPrecio(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaInicio,
                                       @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaFin, Model model) {
        if (fechaInicio == null || fechaFin == null) {
            model.addAttribute("error", "Debe proporcionar min y max");
            model.addAttribute("ventas", List.of());
            return "ventas";
        }

        try {
            List<Venta> resultados = ventaService.findVentaByFechaVentaBetween(fechaInicio, fechaFin);
            model.addAttribute("ventas", resultados);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("ventas", List.of());
        }

        return "ventas";
    }

}
