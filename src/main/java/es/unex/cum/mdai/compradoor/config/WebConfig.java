package es.unex.cum.mdai.compradoor.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:src/main/resources/static/images/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminInterceptor())
                // --- 1. ZONAS PROHIBIDAS (Solo Admin) ---
                .addPathPatterns(
                        "/admin/**",
                        "/clientes/**",
                        "/servicios/**",
                        "/ventas/**",        // Bloquea gestión de ventas
                        "/compras/**",       // <--- NUEVO: Bloquea TODA la gestión de compras
                        "/tarjetas/**",      // Bloquea gestión de tarjetas global
                        "/inmuebles/update/**",
                        "/inmuebles/delete/**",
                        "/inmuebles/*/delete"
                )
                // --- 2. EXCEPCIONES (Zonas permitidas) ---
                .excludePathPatterns(
                        "/ventas/mis-ventas",          // Historial del usuario
                        "/tarjetas/mis-tarjetas/**",   // Tarjetas del usuario
                        "/tarjetas/mis-tarjetas/new",
                        "/tarjetas/mis-tarjetas/save",
                        "/tarjetas/mis-tarjetas/*/toggle", // <--- NUEVO: Permitir activar tarjeta
                        "/inmuebles/",                 // Catálogo
                        "/inmuebles/search",
                        "/css/**", "/js/**", "/images/**"
                );
    }
}