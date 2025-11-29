package es.unex.cum.mdai.compradoor.data.controllers;

import es.unex.cum.mdai.compradoor.data.model.Cliente;
import es.unex.cum.mdai.compradoor.data.model.Tarjeta;
import es.unex.cum.mdai.compradoor.data.services.ClienteService;
import es.unex.cum.mdai.compradoor.data.services.TarjetaService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

    // ==========================================
    // ZONA ADMIN (Gestión total)
    // ==========================================

    @GetMapping("/all")
    public String listTarjetas(Model model) {
        model.addAttribute("tarjetas", tarjetaService.findAllTarjetas());
        return "tarjetas_layouts/tarjetas";
    }

    // Formulario para ADMIN (Debe permitir elegir cliente)
    @GetMapping("/new")
    public String showTarjetaFormAdmin(Model model) {
        model.addAttribute("tarjeta", new Tarjeta());
        // Pasamos la lista de clientes para el desplegable
        model.addAttribute("clientes", clienteService.findAllClientes());
        // Devolvemos la vista ADMIN (tarjetaform.html)
        return "tarjetas_layouts/tarjetaform";
    }

    // Guardado para ADMIN (Recibe el cliente del formulario)
    @PostMapping("/save")
    public String saveTarjetaAdmin(@Valid @ModelAttribute Tarjeta tarjeta, BindingResult result, Model model) {

        // Limpieza de espacios en blanco
        if(tarjeta.getCodigoTarjeta() != null) {
            tarjeta.setCodigoTarjeta(tarjeta.getCodigoTarjeta().replaceAll("\\s+", ""));
        }

        if (result.hasErrors()) {
            model.addAttribute("clientes", clienteService.findAllClientes());
            return "tarjetas_layouts/tarjetaform";
        }

        try {
            // Aquí NO cogemos el usuario de la sesión, respetamos el que viene seleccionado en el form
            if (tarjeta.getCliente() == null) {
                throw new IllegalArgumentException("Debes seleccionar un cliente titular.");
            }
            tarjetaService.saveTarjeta(tarjeta);
        } catch (Exception e) {
            model.addAttribute("error", "Error: " + e.getMessage());
            model.addAttribute("clientes", clienteService.findAllClientes());
            return "tarjetas_layouts/tarjetaform";
        }

        return "redirect:/tarjetas/all";
    }

    // Borrado Admin
    @PostMapping("/{id}/delete")
    public String deleteTarjetaAdmin(@PathVariable UUID id) {
        tarjetaService.findById(id).ifPresent(tarjetaService::deleteTarjeta);
        return "redirect:/tarjetas/all";
    }


    // ==========================================
    // ZONA CLIENTE (Mis Tarjetas)
    // ==========================================

    @GetMapping("/mis-tarjetas")
    public String listMisTarjetas(Model model, HttpSession session) {
        Cliente cliente = (Cliente) session.getAttribute("clienteLogueado");
        if (cliente == null) return "redirect:/login";

        model.addAttribute("tarjetas", tarjetaService.findAllTarjetasByCliente(cliente));
        return "tarjetas_layouts/mis_tarjetas";
    }

    // Formulario para CLIENTE (Automático, sin elegir usuario)
    @GetMapping("/mis-tarjetas/new")
    public String showTarjetaFormCliente(Model model, HttpSession session) {
        Cliente cliente = (Cliente) session.getAttribute("clienteLogueado");
        if (cliente == null) return "redirect:/login";

        model.addAttribute("tarjeta", new Tarjeta());
        // NO pasamos lista de clientes, no hace falta
        return "tarjetas_layouts/tarjetaform_cliente";
    }

    // Guardado para CLIENTE (Se auto-asigna)
    @PostMapping("/mis-tarjetas/save")
    public String saveTarjetaCliente(@Valid @ModelAttribute Tarjeta tarjeta,
                                     BindingResult result,
                                     HttpSession session,
                                     Model model) {

        Cliente cliente = (Cliente) session.getAttribute("clienteLogueado");
        if (cliente == null) return "redirect:/login";

        if(tarjeta.getCodigoTarjeta() != null) {
            tarjeta.setCodigoTarjeta(tarjeta.getCodigoTarjeta().replaceAll("\\s+", ""));
        }

        if (result.hasErrors()) {
            return "tarjetas_layouts/tarjetaform_cliente";
        }

        try {
            // FORZAMOS que el dueño sea el usuario logueado
            tarjeta.setCliente(cliente);
            tarjetaService.saveTarjeta(tarjeta);
        } catch (Exception e) {
            model.addAttribute("error", "Error: " + e.getMessage());
            return "tarjetas_layouts/tarjetaform_cliente";
        }

        return "redirect:/tarjetas/mis-tarjetas";
    }

    @PostMapping("/mis-tarjetas/{id}/delete")
    public String deleteTarjetaCliente(@PathVariable UUID id, HttpSession session) {
        // Podríamos añadir seguridad extra comprobando que la tarjeta pertenece al usuario
        tarjetaService.findById(id).ifPresent(tarjetaService::deleteTarjeta);
        return "redirect:/tarjetas/mis-tarjetas";
    }

    // NUEVO: Método para activar/desactivar tarjeta
    @PostMapping("/mis-tarjetas/{id}/toggle")
    public String toggleTarjetaValida(@PathVariable UUID id, HttpSession session) {
        Cliente cliente = (Cliente) session.getAttribute("clienteLogueado");
        if (cliente == null) return "redirect:/login";

        // Buscamos la tarjeta y verificamos que sea del usuario logueado por seguridad
        tarjetaService.findById(id).ifPresent(t -> {
            if (t.getCliente().getId().equals(cliente.getId())) {
                t.setValida(!t.isValida()); // Cambiamos de true a false o viceversa
                tarjetaService.saveTarjeta(t);
            }
        });

        return "redirect:/tarjetas/mis-tarjetas";
    }
}