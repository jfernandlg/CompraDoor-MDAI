package es.unex.cum.mdai.compradoor.data.repository;

import es.unex.cum.mdai.compradoor.data.model.Cliente;
import es.unex.cum.mdai.compradoor.data.model.Inmueble;
import es.unex.cum.mdai.compradoor.data.model.Venta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class InmuebleTest {

    @Autowired
    private InmuebleRepository inmuebleRepository;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @BeforeEach
    void setUp() {
        ventaRepository.deleteAll();
        inmuebleRepository.deleteAll();
        clienteRepository.deleteAll();
    }

    @Test
    void testFindByLocalidad() {
        Inmueble i = new Inmueble("Badajoz", 250_000f, "Avenida de América, 25");
        Inmueble i2 = new Inmueble("Cáceres", 470_000f, "Avenida de la Hispanidad, 35");
        Inmueble i3 = new Inmueble("Badajoz", 170_000f, "Avenida de España, 10");
        Inmueble i4 = new Inmueble("Badajoz", 545_000f, "Paseo Fluvial, 16");
        inmuebleRepository.save(i);
        inmuebleRepository.save(i2);
        inmuebleRepository.save(i3);
        inmuebleRepository.save(i4);

        List<Inmueble> findByLocalidad = inmuebleRepository.findByLocalidadIgnoreCase("Badajoz");
        assertThat(findByLocalidad).hasSize(3);

        // Comprobar case-insensitive
        List<Inmueble> lower = inmuebleRepository.findByLocalidadIgnoreCase("badajoz");
        List<Inmueble> upper = inmuebleRepository.findByLocalidadIgnoreCase("BADAJOZ");
        assertThat(lower).hasSize(3);
        assertThat(upper).hasSize(3);
    }

    @Test
    void testPriceBetween() {
        Inmueble i = new Inmueble("Badajoz", 250_000f, "Avenida de América, 25");
        Inmueble i2 = new Inmueble("Cáceres", 470_000f, "Avenida de la Hispanidad, 35");
        Inmueble i3 = new Inmueble("Badajoz", 170_000f, "Avenida de España, 10");
        Inmueble i4 = new Inmueble("Badajoz", 545_000f, "Paseo Fluvial, 16");
        inmuebleRepository.save(i);
        inmuebleRepository.save(i2);
        inmuebleRepository.save(i3);
        inmuebleRepository.save(i4);

        List<Inmueble> ByPriceBetween = inmuebleRepository.findByPrecioBetween(100_000f, 500_000f);
        assertThat(ByPriceBetween).hasSize(3);
    }

    @Test
    void testPriceBetweenInclusivity() {
        Inmueble a = new Inmueble("Test", 100_000f, "A");
        Inmueble b = new Inmueble("Test", 200_000f, "B");
        Inmueble c = new Inmueble("Test", 300_000f, "C");
        inmuebleRepository.save(a);
        inmuebleRepository.save(b);
        inmuebleRepository.save(c);

        // límites incluidos
        List<Inmueble> res = inmuebleRepository.findByPrecioBetween(100_000f, 300_000f);
        assertThat(res).hasSize(3);
        assertThat(res).extracting("idInmueble").containsExactlyInAnyOrder(a.getIdInmueble(), b.getIdInmueble(), c.getIdInmueble());
    }

    @Test
    void testPriceBetweenEmpty() {
        List<Inmueble> res = inmuebleRepository.findByPrecioBetween(10_000f, 20_000f);
        assertThat(res).isEmpty();
    }

    @Test
    void testVentaIsNull() {
        // ===== CONFIGURACIÓN INICIAL =====

        // Crear y guardar un cliente
        Cliente c = new Cliente("12345678A", "Juan Perez");
        c.setEmail("juan@example.com");
        clienteRepository.save(c);

        // Crear tres inmuebles en diferentes localidades
        Inmueble i = new Inmueble("Badajoz", 250_000f, "Avenida de América, 25");
        Inmueble i2 = new Inmueble("Cáceres", 470_000f, "Avenida de la Hispanidad, 35");
        Inmueble i3 = new Inmueble("Mérida", 340_000f, "Avenida Reina Sofía, 12");

        // Guardar los inmuebles en la base de datos
        inmuebleRepository.save(i);
        inmuebleRepository.save(i2);
        inmuebleRepository.save(i3);

        // ===== SIMULACIÓN DE VENTA =====

        // Crear una venta asociando el primer inmueble (i) con el cliente
        // El inmueble i (Badajoz) ahora tiene una venta asociada
        Venta v = new Venta(i, 270_000f, c);
        ventaRepository.save(v);

        // Añadir la venta a la lista de ventas del cliente (relación bidireccional)
        c.getVentas().add(v);

        // ===== PRIMERA VERIFICACIÓN: Inmuebles SIN venta asociada =====

        // findByVentaIsNull() debe retornar solo los inmuebles que NO tienen venta
        List<Inmueble> byVenta = inmuebleRepository.findByVentaIsNull();

        // Verificar que hay 2 inmuebles sin venta (i2 e i3)
        assertThat(byVenta).hasSize(2);

        // Verificar que los inmuebles sin venta son exactamente i2 e i3 (en cualquier orden)
        assertThat(byVenta).extracting("idInmueble").containsExactlyInAnyOrder(i2.getIdInmueble(), i3.getIdInmueble());

        // ===== SEGUNDA VERIFICACIÓN: Confirmar que i SÍ tiene venta =====

        // Realizar la misma consulta nuevamente para confirmar consistencia
        List<Inmueble> after = inmuebleRepository.findByVentaIsNull();

        // VERIFICACIÓN CORREGIDA:
        // El inmueble i (Badajoz) NO debe aparecer porque tiene una venta asociada
        assertThat(after).doesNotContain(i);

        // Verificación adicional opcional: confirmar que i2 e i3 siguen en la lista
        assertThat(after).contains(i2, i3);

        // Otra verificación opcional: confirmar que el tamaño sigue siendo 2
        assertThat(after).hasSize(2);
    }

    @Test
    void testCrudAndFindById() {
        Inmueble i = new Inmueble("Prueba", 123_456f, "Calle Test 1");
        inmuebleRepository.save(i);

        Optional<Inmueble> maybe = inmuebleRepository.findById(i.getIdInmueble());
        assertThat(maybe).isPresent();
        Inmueble stored = maybe.get();
        assertThat(stored.getDireccion()).isEqualTo("Calle Test 1");

        stored.setDireccion("Calle Modificada 2");
        inmuebleRepository.save(stored);

        Optional<Inmueble> updated = inmuebleRepository.findById(i.getIdInmueble());
        assertThat(updated).isPresent();
        assertThat(updated.get().getDireccion()).isEqualTo("Calle Modificada 2");

        inmuebleRepository.delete(updated.get());
        Optional<Inmueble> afterDelete = inmuebleRepository.findById(i.getIdInmueble());
        assertThat(afterDelete).isNotPresent();
    }

}
