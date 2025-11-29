package es.unex.cum.mdai.compradoor.config;

import es.unex.cum.mdai.compradoor.data.model.Cliente;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession();
        Cliente cliente = (Cliente) session.getAttribute("clienteLogueado");

        // 1. Si no está logueado -> Al Login
        if (cliente == null) {
            response.sendRedirect("/login");
            return false; // Bloquea el paso
        }

        // 2. Si está logueado pero NO es admin -> A la Home
        if (!cliente.isAdmin()) {
            response.sendRedirect("/");
            return false; // Bloquea el paso
        }

        // 3. Si es Admin -> Pasa
        return true;
    }
}