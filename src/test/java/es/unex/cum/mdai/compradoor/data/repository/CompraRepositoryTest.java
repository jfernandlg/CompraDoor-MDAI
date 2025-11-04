package es.unex.cum.mdai.compradoor.data.repository;

import es.unex.cum.mdai.compradoor.data.model.Cliente;
import es.unex.cum.mdai.compradoor.data.model.Compra;
import es.unex.cum.mdai.compradoor.data.model.Inmueble;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CompraRepositoryTest {

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private InmuebleRepository inmuebleRepository;

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
}
