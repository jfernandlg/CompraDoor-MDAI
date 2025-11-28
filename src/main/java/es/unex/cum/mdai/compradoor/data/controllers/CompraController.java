package es.unex.cum.mdai.compradoor.data.controllers;

import es.unex.cum.mdai.compradoor.data.model.Compra;
import es.unex.cum.mdai.compradoor.data.model.Servicio;
import es.unex.cum.mdai.compradoor.data.model.TipoServicio; // Importante
import es.unex.cum.mdai.compradoor.data.services.CompraService;
import es.unex.cum.mdai.compradoor.data.services.ClienteService;
import es.unex.cum.mdai.compradoor.data.services.InmuebleService;
import es.unex.cum.mdai.compradoor.data.services.ServicioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/compras")
public class CompraController {

    private final CompraService compraService;
    private final ClienteService clienteService;
    private final InmuebleService inmuebleService;
    private final ServicioService servicioService;

    @Autowired
    public CompraController(CompraService compraService, ClienteService clienteService,
                            InmuebleService inmuebleService, ServicioService servicioService) {
        this.compraService = compraService;
        this.clienteService = clienteService;
        this.inmuebleService = inmuebleService;
        this.servicioService = servicioService;
    }

    // 1. Menú Principal
    @GetMapping({"/menu", "", "/"})
    public String menu() {
        return "compras_layouts/compras_index";
    }

    // 2. Listar TODAS las compras
    @GetMapping("/all")
    public String listAll(Model model) {
        model.addAttribute("compras", compraService.findAllCompras());
        model.addAttribute("filtrosAplicados", false);
        return "compras_layouts/compras";
    }

    // 3. Formulario de NUEVA compra
    @GetMapping("/new")
    public String newCompra(Model model) {
        model.addAttribute("compra", new Compra());
        model.addAttribute("clientes", clienteService.findAllClientes());
        model.addAttribute("inmuebles", inmuebleService.findAllInmueble());

        // Pasamos los tipos de servicio para el desplegable opcional
        model.addAttribute("tiposServicio", TipoServicio.values());
        return "compras_layouts/compraform";
    }

    // 4. GUARDAR COMPRA (+ SERVICIO OPCIONAL)
    @PostMapping("/save")
    public String saveCompra(@Valid @ModelAttribute("compra") Compra compra,
                             BindingResult result,
                             // Parámetros EXTRA para el servicio
                             @RequestParam(value = "agregarServicio", required = false) boolean agregarServicio,
                             @RequestParam(value = "srvTipo", required = false) TipoServicio srvTipo,
                             @RequestParam(value = "srvDesc", required = false) String srvDesc,
                             @RequestParam(value = "srvCoste", defaultValue = "0") float srvCoste,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("clientes", clienteService.findAllClientes());
            model.addAttribute("inmuebles", inmuebleService.findAllInmueble());
            model.addAttribute("tiposServicio", TipoServicio.values());
            return "compras_layouts/compraform";
        }

        try {
            // A. Guardamos la COMPRA primero para generar su ID
            Compra compraGuardada = compraService.saveCompra(compra);

            // B. Si el usuario marcó "Agregar Servicio", creamos el servicio vinculado
            if (agregarServicio) {
                if (srvTipo == null) throw new IllegalArgumentException("Debes elegir un tipo de servicio");

                Servicio servicio = new Servicio();
                servicio.setTipoServicio(srvTipo);
                servicio.setDescripcion(srvDesc);
                servicio.setCoste(srvCoste);
                servicio.setFechaAplicacion(compra.getFechaCompra()); // Misma fecha que la compra

                // ASOCIACIÓN: El servicio apunta a la compra recién creada
                servicio.setCompra(compraGuardada);

                servicioService.saveServicio(servicio);
            }

            redirectAttributes.addFlashAttribute("msg", "Compra registrada con éxito 🚀");

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("clientes", clienteService.findAllClientes());
            model.addAttribute("inmuebles", inmuebleService.findAllInmueble());
            model.addAttribute("tiposServicio", TipoServicio.values());
            return "compras_layouts/compraform";
        }
        return "redirect:/compras/all";
    }

    // 5. Editar compra
    @GetMapping("/edit/{id}")
    public String editCompra(@PathVariable("id") UUID id, Model model) {
        Optional<Compra> compra = compraService.findCompraById(id);
        if (compra.isPresent()) {
            model.addAttribute("compra", compra.get());
            model.addAttribute("clientes", clienteService.findAllClientes());
            model.addAttribute("inmuebles", inmuebleService.findAllInmueble());
            // No pasamos tiposServicio aquí porque no permitimos añadir servicios al editar, solo al crear
            return "compras_layouts/compraform";
        } else {
            return "redirect:/compras/all";
        }
    }

    // 6. Actualizar compra (Update)
    @PostMapping("/update/{id}")
    public String updateCompra(@PathVariable("id") UUID id,
                               @Valid @ModelAttribute("compra") Compra compra,
                               BindingResult result,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("clientes", clienteService.findAllClientes());
            model.addAttribute("inmuebles", inmuebleService.findAllInmueble());
            return "compras_layouts/compraform";
        }
        compra.setIdCompra(id);
        try {
            compraService.saveCompra(compra);
            redirectAttributes.addFlashAttribute("msg", "Compra actualizada correctamente ✨");
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("clientes", clienteService.findAllClientes());
            model.addAttribute("inmuebles", inmuebleService.findAllInmueble());
            return "compras_layouts/compraform";
        }
        return "redirect:/compras/all";
    }

    // 7. Borrar compra
    @PostMapping("/delete/{id}")
    public String deleteCompra(@PathVariable("id") UUID id, RedirectAttributes redirectAttributes) {
        Optional<Compra> compra = compraService.findCompraById(id);
        if (compra.isPresent()) {
            compraService.deleteCompra(compra.get());
            redirectAttributes.addFlashAttribute("msg", "Compra eliminada del sistema 🗑️");
        } else {
            redirectAttributes.addFlashAttribute("error", "No se encontró la compra a eliminar");
        }
        return "redirect:/compras/all";
    }

    // 8. Buscador
    @GetMapping("/buscar")
    public String buscarCompras(
            @RequestParam(required = false) String clienteId,
            @RequestParam(required = false) String inmuebleId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaFin,
            @RequestParam(required = false) String orden,
            Model model) {

        List<Compra> resultados = compraService.findAllCompras();
        List<String> errores = new ArrayList<>();

        // Filtro Cliente
        if (clienteId != null && !clienteId.isBlank()) {
            try {
                UUID uuidCliente = UUID.fromString(clienteId);
                resultados = resultados.stream()
                        .filter(c -> c.getCliente().getId().equals(uuidCliente))
                        .collect(Collectors.toList());
            } catch (IllegalArgumentException e) {
                errores.add("ID de cliente inválido.");
            }
        }

        // Filtro Inmueble
        if (inmuebleId != null && !inmuebleId.isBlank()) {
            try {
                UUID uuidInmueble = UUID.fromString(inmuebleId);
                resultados = resultados.stream()
                        .filter(c -> c.getInmueble().getIdInmueble().equals(uuidInmueble))
                        .collect(Collectors.toList());
            } catch (IllegalArgumentException e) {
                errores.add("ID de inmueble inválido.");
            }
        }

        // Filtro Fechas
        if (fechaInicio != null && fechaFin != null) {
            resultados = resultados.stream()
                    .filter(c -> !c.getFechaCompra().before(fechaInicio) && !c.getFechaCompra().after(fechaFin))
                    .collect(Collectors.toList());
        }

        // Ordenación
        if (orden != null) {
            switch (orden) {
                case "precio_asc":
                    resultados.sort(Comparator.comparingDouble(Compra::getPrecioCompra));
                    break;
                case "precio_desc":
                    resultados.sort(Comparator.comparingDouble(Compra::getPrecioCompra).reversed());
                    break;
                case "fecha_asc":
                    resultados.sort(Comparator.comparing(Compra::getFechaCompra));
                    break;
                case "fecha_desc":
                    resultados.sort(Comparator.comparing(Compra::getFechaCompra).reversed());
                    break;
            }
        }

        if (!errores.isEmpty()) model.addAttribute("error", String.join(" ", errores));

        model.addAttribute("compras", resultados);
        model.addAttribute("filtrosAplicados", true);
        model.addAttribute("clienteId", clienteId);
        model.addAttribute("inmuebleId", inmuebleId);
        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);
        model.addAttribute("ordenSeleccionado", orden);

        return "compras_layouts/compras";
    }
}