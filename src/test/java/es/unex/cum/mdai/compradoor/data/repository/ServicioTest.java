package es.unex.cum.mdai.compradoor.data.repository;
import es.unex.cum.mdai.compradoor.data.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ServicioTest {

    @Autowired
    private ServicioRepository serviciosRepository;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private InmuebleRepository inmuebleRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @BeforeEach
    void setUp() {
        serviciosRepository.deleteAll();
        ventaRepository.deleteAll();
        inmuebleRepository.deleteAll();
        clienteRepository.deleteAll();
    }

    private Servicio crearServicio(TipoServicio tipo, String descripcion, Venta venta, float coste, Date fecha) {
        Servicio servicio = new Servicio();
        servicio.setTipoServicio(tipo);
        servicio.setDescripcion(descripcion);
        servicio.setVenta(venta);
        servicio.setCoste(coste);
        servicio.setFechaAplicacion(fecha);  // Ahora funciona correctamente
        return servicio;
    }

    private Venta crearVentaValida() {
        Inmueble inmueble = new Inmueble("Don Benito", 200_000f, "C/ Mayor");
        inmuebleRepository.save(inmueble);

        Cliente cliente = new Cliente("12345678Z", "Prueba Cliente");
        clienteRepository.save(cliente);

        Venta venta = new Venta(inmueble, 210_000f, cliente);
        return ventaRepository.save(venta);
    }

    @Test
    void testFindByTipoServicio() {
        Venta venta = crearVentaValida();
        Servicio servicio = crearServicio(TipoServicio.LIMPIEZA, "Limpieza final", venta, 120f, new Date());
        serviciosRepository.save(servicio);

        List<Servicio> result = serviciosRepository.findByTipoServicio(TipoServicio.LIMPIEZA);

        assertThat(result).hasSize(1);
        assertThat(result).first().extracting(Servicio::getTipoServicio).isEqualTo(TipoServicio.LIMPIEZA);
    }

    @Test
    void testFindByDescripcionContainingIgnoreCase() {
        Venta venta = crearVentaValida();
        Servicio servicio = crearServicio(TipoServicio.PINTURA, "Esta es una Prueba Unica", venta, 90f, new Date());
        serviciosRepository.save(servicio);

        List<Servicio> result = serviciosRepository.findByDescripcionContainingIgnoreCase("prueba");

        assertThat(result).hasSize(1);
        assertThat(result).first().extracting(Servicio::getDescripcion)
                .asString()
                .containsIgnoringCase("prueba");
    }

    @Test
    void testFindByVenta() {
        Venta venta = crearVentaValida();
        Servicio servicio = crearServicio(TipoServicio.FONTANERIA, "Fontanería básica", venta, 40f, new Date());
        serviciosRepository.save(servicio);

        List<Servicio> servicios = serviciosRepository.findByVenta(venta);

        assertThat(servicios).hasSize(1);
        assertThat(servicios).first().extracting(Servicio::getVenta).isEqualTo(venta);
    }

    @Test
    void testFindByFechaAplicacionBetween() {
        Venta venta = crearVentaValida();

        Calendar cal = Calendar.getInstance();
        cal.set(2024, Calendar.JANUARY, 1);
        Date fecha1 = cal.getTime();

        cal.set(2024, Calendar.APRIL, 1);
        Date fechaIntermedia = cal.getTime();

        cal.set(2024, Calendar.JUNE, 1);
        Date fecha2 = cal.getTime();

        serviciosRepository.save(crearServicio(TipoServicio.LIMPIEZA, "FechaAntigua", venta, 45f, fecha1));
        serviciosRepository.save(crearServicio(TipoServicio.LIMPIEZA, "FechaIntermedia", venta, 45f, fechaIntermedia));
        serviciosRepository.save(crearServicio(TipoServicio.LIMPIEZA, "FechaReciente", venta, 45f, fecha2));

        List<Servicio> encontrados = serviciosRepository.findByFechaAplicacionBetween(fecha1, fecha2);

        assertThat(encontrados).hasSize(3);
    }

    // Los demás tests funcionarán de manera similar...
}
