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
class TarjetaTest {

    @Autowired
    private TarjetaRepository tarjetaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    void testFindByCodigoTarjeta() {
        Cliente c = new Cliente("12345678A", "Juan");
        c.setEmail("juan@example.com");
        c.setPassword("password123");
        clienteRepository.save(c);

        Tarjeta t = new Tarjeta("8126 4499 8744 2222", true, c);
        tarjetaRepository.save(t);

        Optional<Tarjeta> byCodigoTarjeta = tarjetaRepository.findByCodigoTarjeta("8126 4499 8744 2222");
        assertThat(byCodigoTarjeta).isPresent();

        Optional<Tarjeta> byCodigoTarjetaNotExist = tarjetaRepository.findByCodigoTarjeta("1234 5678 7654 3211");
        assertThat(byCodigoTarjetaNotExist).isNotPresent();

    }

    @Test
    void testFindByCliente() {
        Cliente c = new Cliente("12345678A", "Juan");
        c.setEmail("juan@example.com");
        c.setPassword("password123");
        clienteRepository.save(c);

        Tarjeta t = new Tarjeta("8126 4499 8744 2222", true, c);
        Tarjeta t2 = new Tarjeta("1234 5678 7654 3211", true, c);
        tarjetaRepository.save(t);
        tarjetaRepository.save(t2);

        List<Tarjeta> byCliente = tarjetaRepository.findByCliente(c);
        assertThat(byCliente).hasSize(2);
        assertThat(byCliente).extracting("id").containsExactlyInAnyOrder(t.getId(), t2.getId());
    }

    @Test
    void testExistByCodigoTarjeta() {
        Cliente c = new Cliente("12345678A", "Juan");
        c.setEmail("juan@example.com");
        c.setPassword("password123");
        clienteRepository.save(c);

        Tarjeta t = new Tarjeta("8126 4499 8744 2222", true, c);
        tarjetaRepository.save(t);

        boolean exist = tarjetaRepository.existsByCodigoTarjeta("8126 4499 8744 2222");
        assertThat(exist).isTrue();

        boolean notExist = tarjetaRepository.existsByCodigoTarjeta("1234 5678 7654 3211");
        assertThat(notExist).isFalse();
    }

    @Test
    void testCountByCliente() {
        Cliente c = new Cliente("12345678A", "Juan");
        c.setEmail("juan@example.com");
        c.setPassword("password123");

        Cliente c2 = new Cliente("87654321X", "Ana");
        c2.setEmail("ana-garcia@example.com");
        c2.setPassword("password123");

        clienteRepository.saveAll(List.of(c, c2));

        Tarjeta t1 = new Tarjeta("8126 4499 8744 2222", true, c);
        Tarjeta t2 = new Tarjeta("1234 5678 7654 3211", true, c);
        Tarjeta t3 = new Tarjeta("9999 8888 7777 6666", false, c);
        Tarjeta t4 = new Tarjeta("4444 3333 2222 1111", true, c2);
        tarjetaRepository.saveAll(List.of(t1, t2, t3, t4));

        long countC = tarjetaRepository.countByCliente(c);
        assertThat(countC).isEqualTo(3);

        long countC1 = tarjetaRepository.countByCliente(c2);
        assertThat(countC1).isEqualTo(1);

    }

    @Test
    void testCodigoTarjetaUnico() {
        Cliente c = new Cliente("12345678A", "Juan");
        c.setEmail("juan@example.com");
        c.setPassword("password123");
        clienteRepository.save(c);

        Tarjeta t1 = new Tarjeta("8126 4499 8744 2222", true, c);
        tarjetaRepository.save(t1);

        Tarjeta t2 = new Tarjeta("8126 4499 8744 2222", false, c);

        assertThat(tarjetaRepository.existsByCodigoTarjeta("8126 4499 8744 2222")).isTrue();
    }

    @Test
    void testTarjetaValidasEInvalidas() {
        Cliente c = new Cliente("12345678A", "Juan");
        c.setEmail("juan@example.com");
        c.setPassword("password123");
        clienteRepository.save(c);

        Tarjeta t1 = new Tarjeta("8126 4499 8744 2222", true, c);
        Tarjeta t2 = new Tarjeta("1234 5678 7654 3211", false, c);
        tarjetaRepository.saveAll(List.of(t1, t2));

        List<Tarjeta> tarjetas = tarjetaRepository.findByCliente(c);
        assertThat(tarjetas).hasSize(2);

        assertThat(tarjetas).extracting("valida").containsExactlyInAnyOrder(true, false);

    }

    @Test
    void testUpdateTarjeta(){
        Cliente c = new Cliente("12345678A", "Juan");
        c.setEmail("juan@example.com");
        c.setPassword("password123");
        clienteRepository.save(c);

        Tarjeta t1 = new Tarjeta("8126 4499 8744 2222", true, c);
        Tarjeta t1_save = tarjetaRepository.save(t1);

        t1_save.setValida(false);
        tarjetaRepository.save(t1_save);

        Tarjeta update  = tarjetaRepository.findById(t1_save.getId()).orElse(null);
        assertThat(update).isNotNull();
        assertThat(update.isValida()).isFalse();
    }




}
