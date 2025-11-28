package es.unex.cum.mdai.compradoor.data.repository;

import es.unex.cum.mdai.compradoor.data.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ServicioTest {

    @Autowired
    private ServicioRepository serviciosRepository;

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private InmuebleRepository inmuebleRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @BeforeEach
    void setUp() {
        // Orden de borrado: Hijos primero -> Padres después
        serviciosRepository.deleteAll();
        compraRepository.deleteAll();
        inmuebleRepository.deleteAll();
        clienteRepository.deleteAll();
    }

    // Método auxiliar para crear Servicios
    private Servicio crearServicio(TipoServicio tipo, String descripcion, Compra compra, float coste, Date fecha) {
        Servicio servicio = new Servicio();
        servicio.setTipoServicio(tipo);
        servicio.setDescripcion(descripcion);
        servicio.setCompra(compra);
        servicio.setCoste(coste);
        servicio.setFechaAplicacion(fecha);
        return servicio;
    }

    // Método auxiliar para crear una Compra válida con Inmueble y Cliente REALES
    private Compra crearCompraValida() {
        // 1. Inmueble
        // Usamos el constructor disponible: Inmueble(String localidad, Float precio, String direccion)
        Inmueble inmueble = new Inmueble("Don Benito", 200_000f, "C/ Mayor, 123");
        inmuebleRepository.save(inmueble);

        // 2. Cliente
        // CORRECCIÓN: Cliente no tiene 'apellido', solo 'nombre', 'dni' y 'email'
        Cliente cliente = new Cliente();
        cliente.setDni("12345678Z");
        cliente.setNombre("Pepe Cliente Prueba"); // Nombre completo aquí
        cliente.setEmail("test@email.com");       // Obligatorio por @NotBlank
        clienteRepository.save(cliente);

        // 3. Compra
        Compra compra = new Compra();
        compra.setInmueble(inmueble);
        compra.setCliente(cliente);
        compra.setPrecioCompra(210_000f);
        compra.setFechaCompra(new Date());

        return compraRepository.save(compra);
    }

    @Test
    void testFindByTipoServicio() {
        Compra compra = crearCompraValida();
        Servicio servicio = crearServicio(TipoServicio.LIMPIEZA, "Limpieza final", compra, 120f, new Date());
        serviciosRepository.save(servicio);

        List<Servicio> result = serviciosRepository.findByTipoServicio(TipoServicio.LIMPIEZA);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTipoServicio()).isEqualTo(TipoServicio.LIMPIEZA);
    }

    @Test
    void testFindByCompra() {
        Compra compra = crearCompraValida();
        Servicio servicio = crearServicio(TipoServicio.FONTANERIA, "Fontanería básica", compra, 40f, new Date());
        serviciosRepository.save(servicio);

        List<Servicio> servicios = serviciosRepository.findByCompra(compra);

        assertThat(servicios).hasSize(1);
        assertThat(servicios.get(0).getCompra().getIdCompra()).isEqualTo(compra.getIdCompra());
    }

    @Test
    void testFindByCosteBetween() {
        Compra compra = crearCompraValida();
        // Guardamos 3 servicios con precios distintos
        serviciosRepository.save(crearServicio(TipoServicio.OTROS, "Barato", compra, 50f, new Date()));
        serviciosRepository.save(crearServicio(TipoServicio.OTROS, "Medio", compra, 150f, new Date()));
        serviciosRepository.save(crearServicio(TipoServicio.OTROS, "Caro", compra, 300f, new Date()));

        // Buscamos entre 100 y 200 (debería encontrar solo el "Medio" de 150)
        List<Servicio> encontrados = serviciosRepository.findByCosteBetween(100f, 200f);

        assertThat(encontrados).hasSize(1);
        assertThat(encontrados.get(0).getCoste()).isEqualTo(150f);
    }

    @Test
    void testFindByFechaAplicacionBetween() {
        Compra compra = crearCompraValida();

        Calendar cal = Calendar.getInstance();

        // Definimos fechas clave
        cal.set(2024, Calendar.JANUARY, 1);
        Date fechaEnero = cal.getTime();

        cal.set(2024, Calendar.APRIL, 1);
        Date fechaAbril = cal.getTime(); // Dentro del rango

        cal.set(2024, Calendar.JUNE, 1);
        Date fechaJunio = cal.getTime(); // Dentro del rango

        cal.set(2024, Calendar.DECEMBER, 31);
        Date fechaDiciembre = cal.getTime();

        // Guardamos servicios en distintas fechas
        serviciosRepository.save(crearServicio(TipoServicio.LIMPIEZA, "Enero", compra, 45f, fechaEnero));
        serviciosRepository.save(crearServicio(TipoServicio.LIMPIEZA, "Abril", compra, 45f, fechaAbril));
        serviciosRepository.save(crearServicio(TipoServicio.LIMPIEZA, "Junio", compra, 45f, fechaJunio));
        serviciosRepository.save(crearServicio(TipoServicio.LIMPIEZA, "Diciembre", compra, 45f, fechaDiciembre));

        // Rango de búsqueda: Febrero a Octubre
        cal.set(2024, Calendar.FEBRUARY, 1);
        Date inicioBusqueda = cal.getTime();

        cal.set(2024, Calendar.OCTOBER, 1);
        Date finBusqueda = cal.getTime();

        List<Servicio> encontrados = serviciosRepository.findByFechaAplicacionBetween(inicioBusqueda, finBusqueda);

        // Debería encontrar 2 (Abril y Junio)
        assertThat(encontrados).hasSize(2);
    }
}