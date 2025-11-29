package es.unex.cum.mdai.compradoor.data.controllers;

import es.unex.cum.mdai.compradoor.data.model.Cliente;
import es.unex.cum.mdai.compradoor.data.model.Inmueble;
import es.unex.cum.mdai.compradoor.data.services.CompraService;
import es.unex.cum.mdai.compradoor.data.services.InmuebleService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@RequestMapping("/transaccion")
public class CompraTransaccionController {

    private final CompraService compraService;

    private final InmuebleService inmuebleService;

    @Autowired
    public CompraTransaccionController(CompraService compraService, InmuebleService inmuebleService) {
        this.compraService = compraService;
        this.inmuebleService = inmuebleService;
    }

    @GetMapping("/confirmar")
    public String confirmarCompra(@RequestParam("idInmueble") UUID idInmueble, Model model, HttpSession session) {

        if (session.getAttribute("clienteLogueado") == null) {
            return "redirect:/login";
        }

        Inmueble inmueble = inmuebleService.findInmuebleById(idInmueble)
                .orElseThrow(() -> new RuntimeException("Inmueble no encontrado"));

        model.addAttribute("inmueble", inmueble);

        return "compra_confirmacion";
    }

    @PostMapping("/realizar")
    public String realizarCompra(@RequestParam("idInmueble") UUID idInmueble, HttpSession session) {

        Cliente cliente = (Cliente) session.getAttribute("clienteLogueado");
        if (session.getAttribute("clienteLogueado") == null) {
            return "redirect:/login";
        }

        Inmueble inmueble = inmuebleService.findInmuebleById(idInmueble)
                .orElseThrow(() -> new RuntimeException("Inmueble no encontrado"));

        compraService.realizarCompra(cliente, inmueble);

        return "redirect:/perfil";
    }
}
