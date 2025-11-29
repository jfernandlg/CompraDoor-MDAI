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

//    @GetMapping({"", "/", "/menu"})
//    public String tarjetaMenu() {
//        return "tarjetas_index";
//    }

    @GetMapping("/all")
    public String listTarjetas(Model model) {
        model.addAttribute("tarjetas", tarjetaService.findAllTarjetas());
        return "tarjetas";
    }

    @GetMapping("/new")
    public String showTarjetaForm(Model model) {
        model.addAttribute("tarjeta", new Tarjeta());
        model.addAttribute("clientes", clienteService.findAllClientes());
        return "tarjetaform";
    }

    @PostMapping("/")
    public String createTarjeta(@Valid @ModelAttribute Tarjeta tarjeta, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("clientes", clienteService.findAllClientes());
            return "tarjetaform";
        }

        try {
            tarjetaService.saveTarjeta(tarjeta);
        } catch (Exception e) {
            model.addAttribute("clientes", clienteService.findAllClientes());
            model.addAttribute("error", e.getMessage()); // Muestra error global si quieres
            return "tarjetaform";
        }

        return "redirect:/tarjetas/all";
    }

    @PostMapping("/{id}/delete")
    public String deleteTarjeta(@PathVariable UUID id) {
        tarjetaService.findById(id).ifPresent(tarjetaService::deleteTarjeta);
        return "redirect:/tarjetas/all";
    }

    @GetMapping("/mis-tarjetas")
    public String misTarjetas(HttpSession session, Model model) {
        Cliente cliente = (Cliente) session.getAttribute("clienteLogueado");
        if (cliente == null) return "redirect:/login";

        // Pasamos la lista de tarjetas
        model.addAttribute("tarjetas", tarjetaService.findAllTarjetasByCliente(cliente));

        // CORRECCIÓN: Devolvemos la vista de LISTA (no la del formulario)
        return "mis_tarjetas"; // <--- Asegúrate de tener este archivo creado
    }

    // 2. VER FORMULARIO (Usa tarjetas_cliente.html)
    @GetMapping("/mis-tarjetas/new")
    public String createTarjetaForm(Model model, HttpSession session) {
        if (session.getAttribute("clienteLogueado") == null) return "redirect:/login";

        // Pasamos el objeto vacío para el formulario
        model.addAttribute("tarjeta", new Tarjeta());

        // Devolvemos la vista del FORMULARIO
        return "tarjetaform_cliente";
    }

    @PostMapping("/mis-tarjetas")
    public String saveTarjetaForm(@Valid @ModelAttribute Tarjeta tarjeta,
                                  BindingResult result,
                                  HttpSession session,
                                  Model model) {

        Cliente cliente = (Cliente) session.getAttribute("clienteLogueado");
        if (cliente == null) {
            return "redirect:/login";
        }

        if(tarjeta.getCodigoTarjeta() != null) {
            String codigoTarjeta = tarjeta.getCodigoTarjeta().replaceAll("\\s+", "");
            tarjeta.setCodigoTarjeta(codigoTarjeta);
        }

        if (result.hasErrors()) {
            return "tarjetaform_cliente";
        }

        try {
            tarjeta.setCliente(cliente);
            tarjetaService.saveTarjeta(tarjeta);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Error " + e.getMessage());
            return "tarjetaform_cliente";
        }


        return "redirect:/tarjetas/mis-tarjetas";
    }

    @PostMapping("/mis-tarjetas/{id}/delete")
    public String deleteTarjetaCliente(@PathVariable UUID id, HttpSession session) {

        tarjetaService.findById(id).ifPresent(tarjetaService::deleteTarjeta);
        return "redirect:/tarjetas/mis-tarjetas";

    }

}
