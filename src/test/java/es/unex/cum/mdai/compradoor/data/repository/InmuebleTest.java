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
        // Limpiamos en orden para respetar las claves foráneas
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
        // Este test requiere que InmuebleRepository tenga el método findByPrecioBetween(min, max)
        Inmueble i = new Inmueble("Badajoz", 250_000f, "Avenida de América, 25");
        Inmueble i2 = new Inmueble("Cáceres", 470_000f, "Avenida de la Hispanidad, 35");
        Inmueble i3 = new Inmueble("Badajoz", 170_000f, "Avenida de España, 10");
        Inmueble i4 = new Inmueble("Badajoz", 545_000f, "Paseo Fluvial, 16");

        inmuebleRepository.save(i);
        inmuebleRepository.save(i2);
        inmuebleRepository.save(i3);
        inmuebleRepository.save(i4);

        List<Inmueble> ByPriceBetween = inmuebleRepository.findByVentaIsNullAndPrecioBetween(100_000f, 500_000f);
        assertThat(ByPriceBetween).hasSize(3);
    }

    @Test
    void testPriceBetweenInclusivity() {
        Inmueble a = new Inmueble("Test", 100_000f, "A");
        a.setDireccion("Calle Falsa 1");

        Inmueble b = new Inmueble("Test", 200_000f, "B");
        b.setDireccion("Calle Falsa 2");

        Inmueble c = new Inmueble("Test", 300_000f, "C");
        c.setDireccion("Calle Falsa 3");

        inmuebleRepository.save(a);
        inmuebleRepository.save(b);
        inmuebleRepository.save(c);

        // límites incluidos
        List<Inmueble> res = inmuebleRepository.findByVentaIsNullAndPrecioBetween(100_000f, 300_000f);
        assertThat(res).hasSize(3);
        assertThat(res).extracting("idInmueble").containsExactlyInAnyOrder(a.getIdInmueble(), b.getIdInmueble(), c.getIdInmueble());
    }

    @Test
    void testPriceBetweenEmpty() {
        List<Inmueble> res = inmuebleRepository.findByVentaIsNullAndPrecioBetween(10_000f, 20_000f);
        assertThat(res).isEmpty();
    }

    @Test
    void testVentaIsNull() {
        // 1. Crear Cliente (Usamos setters por si el constructor no existe en tu versión de Cliente)
        Cliente c = new Cliente();
        c.setDni("12345678A");
        c.setNombre("Juan");
        c.setEmail("juan@example.com");
        c.setPassword("password123");
        clienteRepository.save(c);

        // 2. Crear Inmuebles
        Inmueble i = new Inmueble("Badajoz", 250_000f, "Avenida de América, 25");
        Inmueble i2 = new Inmueble("Cáceres", 470_000f, "Avenida de la Hispanidad, 35");
        Inmueble i3 = new Inmueble("Mérida", 340_000f, "Avenida Reina Sofía, 12");

        inmuebleRepository.save(i);
        inmuebleRepository.save(i2);
        inmuebleRepository.save(i3);

        // 3. Crear Venta (Vende el inmueble 'i')
        Venta v = new Venta(i, 270_000f, c);
        ventaRepository.save(v);

        // Nota: Al guardar la venta, la relación se establece en la base de datos.

        // 4. Test: Buscar inmuebles DISPONIBLES (Sin venta)
        List<Inmueble> disponibles = inmuebleRepository.findByVentaIsNull();

        // Deben ser 2 (i2 e i3). El 'i' está vendido.
        assertThat(disponibles).hasSize(2);
        assertThat(disponibles).extracting("idInmueble")
                .containsExactlyInAnyOrder(i2.getIdInmueble(), i3.getIdInmueble());

        // Aseguramos que el vendido NO aparece
        assertThat(disponibles).doesNotContain(i);
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