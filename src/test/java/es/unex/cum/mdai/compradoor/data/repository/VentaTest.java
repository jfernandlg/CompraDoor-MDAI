package es.unex.cum.mdai.compradoor.data.repository;

import es.unex.cum.mdai.compradoor.data.model.Cliente;
import es.unex.cum.mdai.compradoor.data.model.Inmueble;
import es.unex.cum.mdai.compradoor.data.model.Venta;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class VentaTest {

    @Autowired
    VentaRepository ventaRepository;

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    InmuebleRepository inmuebleRepository;

    @Test
    void testFindByCliente() {
        // ===== CONFIGURACIÓN: Crear cliente y inmuebles =====
        Cliente c = new Cliente("12345678A", "Juan Perez");
        c.setEmail("juan@example.com");
        clienteRepository.save(c);

        Inmueble i = new Inmueble("Badajoz", 250_000f, "Avenida de América, 25");
        Inmueble i2 = new Inmueble("Cáceres", 470_000f, "Avenida de la Hispanidad, 35");
        inmuebleRepository.save(i);
        inmuebleRepository.save(i2);

        // ===== EJECUCIÓN: Crear ventas para el mismo cliente =====
        Venta v = new Venta(i, 285_000f, c);
        Venta v2 = new Venta(i2, 500_000f, c);
        ventaRepository.save(v);
        ventaRepository.save(v2);

        // ===== VERIFICACIÓN: Buscar ventas por cliente =====
        List<Venta> findByCliente = ventaRepository.findByCliente(c);

        // Verificar que se encuentran exactamente 2 ventas para este cliente
        assertThat(findByCliente).hasSize(2);

        // Verificar que las ventas encontradas son las correctas (en cualquier orden)
        assertThat(findByCliente).extracting("idVenta")
                .containsExactlyInAnyOrder(v.getIdVenta(), v2.getIdVenta());
    }

    @Test
    void testFindByInmueble() {
        // ===== CONFIGURACIÓN: Crear múltiples inmuebles y cliente =====
        Inmueble i = new Inmueble("Don Benito", 470_000f, "Calle Colón, 3");
        Inmueble i2 = new Inmueble("Mérida", 340_000f, "Avenida Reina Sofía, 12");
        Inmueble i3 = new Inmueble("Villanueva de la Serena", 125_000f, "Calle San Pedro de Alcántara, 12");
        inmuebleRepository.save(i);
        inmuebleRepository.save(i2);
        inmuebleRepository.save(i3);

        Cliente c = new Cliente("87654321X", "Ana García");
        c.setEmail("ana-garcia@example.com");
        clienteRepository.save(c);

        // ===== EJECUCIÓN: Crear ventas para algunos inmuebles =====
        Venta v = new Venta(i, 500_000f, c);   // Venta para inmueble i
        Venta v2 = new Venta(i2, 375_000f, c); // Venta para inmueble i2
        ventaRepository.save(v);
        ventaRepository.save(v2);

        // ===== VERIFICACIÓN: Buscar ventas por inmueble específico =====
        List<Venta> findByInmueble = ventaRepository.findByInmueble(i);

        // Verificar que solo se encuentra 1 venta para el inmueble i
        assertThat(findByInmueble).hasSize(1);

        // Verificar que la venta encontrada es exactamente v
        assertThat(findByInmueble).extracting("idVenta")
                .containsExactlyInAnyOrder(v.getIdVenta());
    }

    @Test
    void testByFechaVentaBetween() {
        // ===== CONFIGURACIÓN: Crear inmuebles y cliente =====
        Inmueble i = new Inmueble("Don Benito", 470_000f, "Calle Colón, 3");
        Inmueble i2 = new Inmueble("Mérida", 340_000f, "Avenida Reina Sofía, 12");
        inmuebleRepository.save(i);
        inmuebleRepository.save(i2);

        Cliente c = new Cliente("87654321X", "Ana García");
        c.setEmail("ana-garcia@example.com");
        clienteRepository.save(c);

        // ===== EJECUCIÓN: Crear ventas con fechas específicas =====
        Venta v = new Venta(i, 500_000f, c);
        Venta v2 = new Venta(i2, 375_000f, c);

        // Establecer fechas de venta específicas
        Date fechaVenta1 = new GregorianCalendar(2018, Calendar.DECEMBER, 12).getTime();
        v.setFechaVenta(fechaVenta1);

        Date fechaVenta2 = new GregorianCalendar(2020, Calendar.APRIL, 24).getTime();
        v2.setFechaVenta(fechaVenta2);

        ventaRepository.save(v);
        ventaRepository.save(v2);

        // ===== VERIFICACIÓN: Buscar ventas en rango de fechas =====

        // Definir rango de búsqueda (solo año 2018)
        Date fechaInicial = new GregorianCalendar(2018, Calendar.JANUARY, 1).getTime();
        Date fechaFinal = new GregorianCalendar(2018, Calendar.DECEMBER, 31).getTime();

        List<Venta> findByDate = ventaRepository.findByFechaVentaBetween(fechaInicial, fechaFinal);

        // Verificar que solo se encuentra 1 venta en el rango (v del 12/12/2018)
        assertThat(findByDate).hasSize(1);

        // Verificar que la venta encontrada es exactamente v
        assertThat(findByDate).extracting("idVenta")
                .containsExactlyInAnyOrder(v.getIdVenta());
    }

}
