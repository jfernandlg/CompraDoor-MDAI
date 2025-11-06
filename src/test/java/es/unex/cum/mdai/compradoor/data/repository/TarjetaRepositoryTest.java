package es.unex.cum.mdai.compradoor.data.repository;

import es.unex.cum.mdai.compradoor.data.model.Cliente;
import es.unex.cum.mdai.compradoor.data.model.Tarjeta;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TarjetaRepositoryTest {

    @Autowired
    private TarjetaRepository tarjetaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    void testFindByCodigoTarjeta() {
        Cliente c = new Cliente("12345678A", "Juan Perez");
        c.setEmail("juan@example.com");
        clienteRepository.save(c);

        Tarjeta t = new Tarjeta("8126449987442222", true, c);
        tarjetaRepository.save(t);

        Optional<Tarjeta> byCodigoTarjeta = tarjetaRepository.findByCodigoTarjeta("8126449987442222");
        assertThat(byCodigoTarjeta).isPresent();

        Optional<Tarjeta> byCodigoTarjetaNotExist = tarjetaRepository.findByCodigoTarjeta("123456787654321");
        assertThat(byCodigoTarjetaNotExist).isNotPresent();

    }

    @Test
    void testFindByCliente() {
        Cliente c = new Cliente("12345678A", "Juan Perez");
        c.setEmail("juan@example.com");
        clienteRepository.save(c);

        Tarjeta t = new Tarjeta("8126449987442222", true, c);
        Tarjeta t2 = new Tarjeta("123456787654321", true, c);
        tarjetaRepository.save(t);
        tarjetaRepository.save(t2);

        List<Tarjeta> byCliente = tarjetaRepository.findByCliente(c);
        assertThat(byCliente).hasSize(2);
        assertThat(byCliente).extracting("id").containsExactlyInAnyOrder(t.getId(), t2.getId());
    }

}
