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
class VentaRepositoryTest {

    @Autowired
    VentaRepository ventaRepository;

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    InmuebleRepository inmuebleRepository;

    @Test
    void testFindByCliente() {
        Cliente c = new Cliente("12345678A", "Juan Perez");
        c.setEmail("juan@example.com");
        clienteRepository.save(c);

        Inmueble i = new Inmueble("Badajoz", 250_000f, "Avenida de América, 25");
        Inmueble i2 = new Inmueble("Cáceres", 470_000f, "Avenida de la Hispanidad, 35");
        inmuebleRepository.save(i);
        inmuebleRepository.save(i2);

        Venta v = new Venta(i, 285_000f, c);
        Venta v2 = new Venta(i2, 500_000f, c);
        ventaRepository.save(v);
        ventaRepository.save(v2);

        List<Venta> findByCliente = ventaRepository.findByCliente(c);
        assertThat(findByCliente).hasSize(2);
        assertThat(findByCliente).extracting("idVenta").containsExactlyInAnyOrder(v.getIdVenta(), v2.getIdVenta());

    }

    @Test
    void testFindByInmueble() {
        Inmueble i = new Inmueble("Don Benito", 470_000f, "Calle Colón, 3");
        Inmueble i2 = new Inmueble("Mérida", 340_000f, "Avenida Reina Sofía, 12");
        Inmueble i3 = new Inmueble("Villanueva de la Serena", 125_000f, "Calle San Pedro de Alcántara, 12");
        inmuebleRepository.save(i);
        inmuebleRepository.save(i2);
        inmuebleRepository.save(i3);

        Cliente c = new Cliente("87654321X", "Ana García");
        c.setEmail("ana-garcia@example.com");
        clienteRepository.save(c);

        Venta v = new Venta(i, 500_000f, c);
        Venta v2 = new Venta(i2, 375_000f, c);
        ventaRepository.save(v);
        ventaRepository.save(v2);

        List<Venta> findByInmueble = ventaRepository.findByInmueble(i);
        assertThat(findByInmueble).extracting("idVenta").containsExactlyInAnyOrder(v.getIdVenta());
    }

    @Test
    void testByFechaVentaBetween() {
        Inmueble i = new Inmueble("Don Benito", 470_000f, "Calle Colón, 3");
        Inmueble i2 = new Inmueble("Mérida", 340_000f, "Avenida Reina Sofía, 12");
        inmuebleRepository.save(i);
        inmuebleRepository.save(i2);

        Cliente c = new Cliente("87654321X", "Ana García");
        c.setEmail("ana-garcia@example.com");
        clienteRepository.save(c);

        Venta v = new Venta(i, 500_000f, c);
        Venta v2 = new Venta(i2, 375_000f, c);
        Date fechaVenta1 = new GregorianCalendar(2018, Calendar.DECEMBER, 12).getTime();
        v.setFechaVenta(fechaVenta1);

        Date fechaVenta2 = new GregorianCalendar(2020, Calendar.APRIL, 24).getTime();
        v2.setFechaVenta(fechaVenta2);

        ventaRepository.save(v);
        ventaRepository.save(v2);

        Date fechaInicial = new GregorianCalendar(2018, Calendar.JANUARY, 1).getTime();
        Date fechaFinal = new GregorianCalendar(2018, Calendar.DECEMBER, 31).getTime();


        List<Venta> findByDate = ventaRepository.findByFechaVentaBetween(fechaInicial, fechaFinal);
        assertThat(findByDate).extracting("idVenta").containsExactlyInAnyOrder(v.getIdVenta());
    }

    @Test
    void testCascadeRemoveCliente(){
        Cliente c = new Cliente("87654321X", "Ana García");
        c.setEmail("ana-garcia@example.com");
        clienteRepository.save(c);

        Inmueble i = new Inmueble("Don Benito", 470_000f, "Calle Colón, 3");
        inmuebleRepository.save(i);

        Venta v = new Venta(i, 500_000f, c);

        c.getVentas().add(v);

        ventaRepository.save(v);

        UUID clienteId = c.getId();
        clienteRepository.deleteById(clienteId);

        assertThat(clienteRepository.findById(clienteId)).isEmpty();
        assertThat(ventaRepository.count()).isZero();
    }
}
