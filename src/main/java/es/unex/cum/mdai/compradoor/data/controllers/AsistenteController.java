package es.unex.cum.mdai.compradoor.data.controllers;

import es.unex.cum.mdai.compradoor.data.model.Inmueble;
import es.unex.cum.mdai.compradoor.data.services.IAService;
import es.unex.cum.mdai.compradoor.data.services.InmuebleService;
import jakarta.servlet.http.HttpSession; // Importante para la sesión
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class AsistenteController {

    private final IAService iaService;
    private final InmuebleService inmuebleService;

    public AsistenteController(IAService iaService, InmuebleService inmuebleService) {
        this.iaService = iaService;
        this.inmuebleService = inmuebleService;
    }

    // PROTECCIÓN: Solo usuarios logueados pueden ver la pantalla
    @GetMapping("/asistente")
    public String verAsistente(HttpSession session) {
        if (session.getAttribute("clienteLogueado") == null) {
            return "redirect:/login"; // Si no hay usuario, mandamos al login
        }
        return "home_layout/asistente";
    }

    // PROTECCIÓN: Solo usuarios logueados pueden preguntar
    @PostMapping("/asistente/preguntar")
    public String consultarIA(@RequestParam("pregunta") String pregunta, Model model, HttpSession session) {

        // 1. Verificación de Seguridad
        if (session.getAttribute("clienteLogueado") == null) {
            return "redirect:/login";
        }

        try {
            // 2. Obtener inventario (Solo disponibles)
            List<Inmueble> inventario = inmuebleService.findInmueblesDisponibles();

            // 3. Llamar a la IA (Versión con control de errores y modelo Groq/OpenAI)
            IAService.RespuestaRecomendacion respuesta = iaService.recomendarInmuebles(pregunta, inventario);

            // 4. Recuperar los objetos completos con fotos
            List<Inmueble> recomendados = new ArrayList<>();
            if (respuesta.inmueblesIds != null) {
                for (UUID id : respuesta.inmueblesIds) {
                    inmuebleService.findInmuebleById(id).ifPresent(recomendados::add);
                }
            }

            model.addAttribute("preguntaUsuario", pregunta);
            model.addAttribute("respuestaIA", respuesta.texto);
            model.addAttribute("inmueblesRecomendados", recomendados);

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("preguntaUsuario", pregunta);
            model.addAttribute("respuestaIA", "Ocurrió un error interno: " + e.getMessage());
        }

        return "home_layout/asistente";
    }
}