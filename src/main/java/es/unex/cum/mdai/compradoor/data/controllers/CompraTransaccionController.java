package es.unex.cum.mdai.compradoor.data.controllers;

import es.unex.cum.mdai.compradoor.data.model.*;
import es.unex.cum.mdai.compradoor.data.services.CompraService;
import es.unex.cum.mdai.compradoor.data.services.InmuebleService;
import es.unex.cum.mdai.compradoor.data.services.ServicioService; // Importante
import es.unex.cum.mdai.compradoor.data.services.TarjetaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/transaccion")
public class CompraTransaccionController {

    private final CompraService compraService;
    private final InmuebleService inmuebleService;
    private final TarjetaService tarjetaService;
    private final ServicioService servicioService; // Inyectamos ServicioService

    @Autowired
    public CompraTransaccionController(CompraService compraService, InmuebleService inmuebleService, TarjetaService tarjetaService, ServicioService servicioService) {
        this.compraService = compraService;
        this.inmuebleService = inmuebleService;
        this.tarjetaService = tarjetaService;
        this.servicioService = servicioService;
    }

    @GetMapping("/confirmar")
    public String confirmarCompra(@RequestParam("idInmueble") UUID idInmueble, Model model, HttpSession session) {
        Cliente cliente = (Cliente) session.getAttribute("clienteLogueado");
        if (cliente == null) return "redirect:/login";

        Inmueble inmueble = inmuebleService.findInmuebleById(idInmueble)
                .orElseThrow(() -> new RuntimeException("Inmueble no encontrado"));

        model.addAttribute("inmueble", inmueble);

        // 1. CARGAR TARJETAS DEL CLIENTE
        List<Tarjeta> tarjetasValidas = tarjetaService.findAllTarjetasByCliente(cliente).stream()
                .filter(Tarjeta::isValida)
                .collect(Collectors.toList());
        model.addAttribute("tarjetas", tarjetasValidas);

        // 2. CARGAR CATÁLOGO REAL DESDE BBDD (Creado por Admin)
        List<Servicio> catalogo = servicioService.findCatalogo();
        model.addAttribute("catalogoServicios", catalogo);

        return "compras_layouts/compra_confirmacion";
    }

    @PostMapping("/realizar")
    public String realizarCompra(@RequestParam("idInmueble") UUID idInmueble,
                                 @RequestParam("idTarjeta") UUID idTarjeta,
                                 @RequestParam(value = "serviciosIds", required = false) List<UUID> serviciosIds,
                                 HttpSession session) {

        Cliente cliente = (Cliente) session.getAttribute("clienteLogueado");
        if (cliente == null) return "redirect:/login";

        Tarjeta tarjeta = tarjetaService.findById(idTarjeta).orElseThrow(() -> new IllegalArgumentException("Tarjeta error"));
        if (!tarjeta.getCliente().getId().equals(cliente.getId()) || !tarjeta.isValida()) return "redirect:/perfil?error=Tarjeta";
        Inmueble inmueble = inmuebleService.findInmuebleById(idInmueble).orElseThrow(() -> new RuntimeException("Inmueble error"));

        compraService.realizarCompraConServicios(cliente, inmueble, serviciosIds, idTarjeta);

        return "redirect:/perfil";
    }
}