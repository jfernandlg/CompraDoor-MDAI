package es.unex.cum.mdai.compradoor.data.repository;

import es.unex.cum.mdai.compradoor.data.model.Cliente;
import es.unex.cum.mdai.compradoor.data.model.Compra;
import es.unex.cum.mdai.compradoor.data.model.Inmueble;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CompraTest {

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private InmuebleRepository inmuebleRepository;

    @BeforeEach
    void setUp() {
        compraRepository.deleteAll();
        clienteRepository.deleteAll();
        inmuebleRepository.deleteAll();
    }

    @Test
    void testFindByCliente() {
        Cliente c = new Cliente("12345678A", "Juan Perez");
        c.setEmail("juan@example.com");
        clienteRepository.save(c);

        Inmueble i = new Inmueble("Badajoz", 250_000f, "Avenida de América, 25");
        Inmueble i2 = new Inmueble("Cáceres", 470_000f, "Avenida de la Hispanidad, 35");
        inmuebleRepository.save(i);
        inmuebleRepository.save(i2);

        Compra compra = new Compra(c, 300_000f, i);
        Compra compra2 = new Compra(c, 500_000f, i2);
        compraRepository.save(compra);
        compraRepository.save(compra2);

        List<Compra> byCliente = compraRepository.findByCliente(c);
        assertThat(byCliente).hasSize(2);
        assertThat(byCliente).extracting("idCompra").containsExactlyInAnyOrder(compra.getIdCompra(), compra2.getIdCompra());
    }

    @Test
    void testFindByInmueble() {
        Cliente c1 = new Cliente("20000000A", "Cliente1");
        Cliente c2 = new Cliente("30000000B", "Cliente2");
        clienteRepository.save(c1);
        clienteRepository.save(c2);

        Inmueble i = new Inmueble("Sevilla", 150_000f, "Calle Falsa 123");
        inmuebleRepository.save(i);

        Compra comp1 = new Compra(c1, 140_000f, i);
        Compra comp2 = new Compra(c2, 145_000f, i);
        compraRepository.save(comp1);
        compraRepository.save(comp2);

        List<Compra> porInmueble = compraRepository.findByInmueble(i);
        assertThat(porInmueble).hasSize(2);
        assertThat(porInmueble).extracting("idCompra").containsExactlyInAnyOrder(comp1.getIdCompra(), comp2.getIdCompra());
    }

    @Test
    void testFindByFechaCompraBetween() {
        Cliente c = new Cliente("40000000C", "FechaTester");
        clienteRepository.save(c);

        Inmueble i = new Inmueble("Madrid", 300_000f, "Gran Vía 1");
        inmuebleRepository.save(i);

        // Crear dos compras con fechas controladas usando setFechaCompra
        Date fechaAhora = new Date();
        Date fechaMasTarde = new Date(fechaAhora.getTime() + 1000);

        Compra compNow = new Compra(c, 300_000f, i);
        compNow.setFechaCompra(fechaAhora);
        compraRepository.save(compNow);

        Compra compLater = new Compra(c, 310_000f, i);
        compLater.setFechaCompra(fechaMasTarde);
        compraRepository.save(compLater);

        Date inicio = new Date(fechaAhora.getTime() - 1000);
        Date fin = new Date(fechaMasTarde.getTime() + 1000);

        List<Compra> between = compraRepository.findByFechaCompraBetween(inicio, fin);
        assertThat(between).isNotEmpty();
        assertThat(between).extracting("idCompra").contains(compNow.getIdCompra(), compLater.getIdCompra());
    }

    @Test
    void testDeletingInmuebleCascadesToCompra() {
        Cliente c = new Cliente("50000000D", "CascadeTest");
        clienteRepository.save(c);

        Inmueble i = new Inmueble("Toledo", 200_000f, "Plaza 4");
        inmuebleRepository.save(i);

        Compra comp = new Compra(c, 200_000f, i);
        compraRepository.save(comp);

        // Eliminar el inmueble debería eliminar también la compra porque Inmueble.compras tiene cascade ALL
        inmuebleRepository.delete(i);

        Optional<Compra> maybe = compraRepository.findById(comp.getIdCompra());
        assertThat(maybe).isNotPresent();
    }

    @Test
    void testDeletingClienteDoesNotCascadeToCompra() {
        Cliente c = new Cliente("60000000E", "ClienteNoCascade");
        clienteRepository.save(c);

        Inmueble i = new Inmueble("Zaragoza", 220_000f, "Calle Real 8");
        inmuebleRepository.save(i);

        Compra comp = new Compra(c, 220_000f, i);
        compraRepository.save(comp);

        // Intentar eliminar el cliente: según las restricciones de la BD, esto puede lanzar DataIntegrityViolationException
        // Afirmamos que o bien la eliminación está bloqueada (excepción) o, si se permite, la compra sigue existiendo (no hay cascade desde Cliente)
        try {
            clienteRepository.delete(c);
            // la eliminación tuvo éxito: comprobamos que la compra sigue en la BD
            Optional<Compra> maybe = compraRepository.findById(comp.getIdCompra());
            assertThat(maybe).isPresent();
        } catch (DataIntegrityViolationException ex) {
            // la eliminación fue bloqueada por la constraint de FK - esto también demuestra que no hay cascade desde Cliente a Compra
            Optional<Compra> maybe = compraRepository.findById(comp.getIdCompra());
            assertThat(maybe).isPresent();
        }
    }
}
