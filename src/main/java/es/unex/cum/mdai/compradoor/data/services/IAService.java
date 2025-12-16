package es.unex.cum.mdai.compradoor.data.services;

import es.unex.cum.mdai.compradoor.data.model.Inmueble;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class IAService {

    private final ChatClient chatClient;

    public IAService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    // Clase auxiliar para devolver texto + lista de IDs
    public static class RespuestaRecomendacion {
        public String texto;
        public List<UUID> inmueblesIds;

        public RespuestaRecomendacion(String texto, List<UUID> inmueblesIds) {
            this.texto = texto;
            this.inmueblesIds = inmueblesIds;
        }
    }

    public RespuestaRecomendacion recomendarInmuebles(String consultaUsuario, List<Inmueble> inmuebles) {
        System.out.println("--- [IA] Iniciando análisis ---");
        // 1. VALIDACIÓN PREVIA
        if (inmuebles == null || inmuebles.isEmpty()) {
            return new RespuestaRecomendacion("No hay inmuebles disponibles en el catálogo.", new ArrayList<>());
        }

        // 2. LIMITAR DATOS (Crucial para evitar que se quede cargando infinito)
        // Tomamos solo los primeros 10-15 inmuebles para que la respuesta sea rápida.
        List<Inmueble> muestra = inmuebles.stream().limit(12).toList();

        // 3. CONVERTIR A TEXTO (Solo campos que SÍ tienes en Inmueble.java)
        String inventarioTexto = muestra.stream()
                .map(i -> {
                    String precioStr = (i.getPrecio() != null) ? String.format("%.0f", i.getPrecio()) : "Consultar";
                    return String.format("- ID: %s | Loc: %s | Precio: %s€ | Dir: %s",
                            i.getIdInmueble(), i.getLocalidad(), precioStr, i.getDireccion());
                })
                .collect(Collectors.joining("\n"));

        // 4. EL PROMPT (Instrucciones)
        String prompt = String.format("""
            Eres un agente inmobiliario.
            Inventario reducido:
            %s
            
            Cliente: "%s"
            
            Instrucciones:
            1. Responde en menos de 60 palabras.
            2. Recomienda 1 o 2 opciones del inventario si encajan.
            3. Formato HTML simple (usar <b> para resaltar).
            4. AL FINAL DE TODO, añade una línea con "---IDS---" y los UUID de las casas recomendadas separados por comas.
            """, inventarioTexto, consultaUsuario);

        // 5. LLAMADA A LA IA (Con protección de errores)
        try {
            String respuestaCompleta = chatClient.prompt().user(prompt).call().content();

            if (respuestaCompleta == null) return new RespuestaRecomendacion("La IA no respondió.", new ArrayList<>());

            // Separar Texto de IDs
            String textoUsuario = respuestaCompleta;
            List<UUID> ids = new ArrayList<>();

            if (respuestaCompleta.contains("---IDS---")) {
                String[] partes = respuestaCompleta.split("---IDS---");
                textoUsuario = partes[0].trim();

                if (partes.length > 1) {
                    // Buscamos UUIDs válidos con Expresión Regular
                    Pattern p = Pattern.compile("[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}");
                    Matcher m = p.matcher(partes[1]);
                    while (m.find()) {
                        ids.add(UUID.fromString(m.group()));
                    }
                }
            }
            return new RespuestaRecomendacion(textoUsuario, ids);

        } catch (Exception e) {
            e.printStackTrace(); // Ver error en consola
            return new RespuestaRecomendacion("Error conectando con OpenAI: " + e.getMessage(), new ArrayList<>());
        }
    }
}