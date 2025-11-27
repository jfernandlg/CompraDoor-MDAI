package es.unex.cum.mdai.compradoor.data.controllers;

import es.unex.cum.mdai.compradoor.data.model.Compra;
import es.unex.cum.mdai.compradoor.data.services.CompraService;
import es.unex.cum.mdai.compradoor.data.services.ClienteService;
import es.unex.cum.mdai.compradoor.data.services.InmuebleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/compras")
public class CompraController {

    private final CompraService compraService;
    private final ClienteService clienteService;
    private final InmuebleService inmuebleService;

    @Autowired
    public CompraController(CompraService compraService, ClienteService clienteService, InmuebleService inmuebleService) {
        this.compraService = compraService;
        this.clienteService = clienteService;
        this.inmuebleService = inmuebleService;
    }

    @GetMapping({"", "/", "/all"})
    public String listCompras(Model model) {
        model.addAttribute("compras", compraService.findAllCompras());
        return "compras"; // requiere plantilla compras.html si se desea vista
    }

    @GetMapping("/new")
    public String showCompraForm(Model model) {
        model.addAttribute("compra", new Compra());
        model.addAttribute("clientes", clienteService.findAllClientes());
        model.addAttribute("inmuebles", inmuebleService.findAllInmueble());
        return "compraform"; // requiere plantilla compraform.html
    }

    @PostMapping("/")
    public String createCompra(@Valid @ModelAttribute Compra compra, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("clientes", clienteService.findAllClientes());
            model.addAttribute("inmuebles", inmuebleService.findAllInmueble());
            return "compraform";
        }
        compraService.saveCompra(compra);
        return "redirect:/compras/all";
    }

    @PostMapping("/{id}/delete")
    public String deleteCompra(@PathVariable UUID id) {
        Compra c = new Compra();
        c.setIdCompra(id);
        compraService.deleteCompra(c);
        return "redirect:/compras/all";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable UUID id, Model model) {
        Compra target = compraService.findAllCompras().stream()
                .filter(c -> id.equals(c.getIdCompra()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Compra no encontrada"));

        model.addAttribute("compra", target);
        model.addAttribute("clientes", clienteService.findAllClientes());
        model.addAttribute("inmuebles", inmuebleService.findAllInmueble());
        return "compraform";
    }

    @PostMapping("/update/{id}")
    public String updateCompra(@PathVariable UUID id, @ModelAttribute Compra compra, BindingResult result) {
        if (result.hasErrors()) {
            return "compraform";
        }
        compra.setIdCompra(id);
        compraService.saveCompra(compra);
        return "redirect:/compras/all";
    }
}
