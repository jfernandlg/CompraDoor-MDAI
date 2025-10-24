package es.unex.cum.mdai.compradoor.data.repository;

import es.unex.cum.mdai.compradoor.data.model.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // use the datasource from src/test/resources
class ClienteRepositoryPersistenceTest {

    @Autowired
    private ClienteRepository clienteRepository;

    @BeforeEach
    void setUp() {
        clienteRepository.deleteAll();
    }

    @Test
    void saveAndFindByDniAndEmailAndName() {
        Cliente c = new Cliente("12345678A", "Juan Perez");
        c.setEmail("juan@example.com");

        clienteRepository.save(c);

        Optional<Cliente> byDni = clienteRepository.findByDni("12345678A");
        assertThat(byDni).isPresent();
        Cliente saved = byDni.get();
        assertThat(saved.getNombre()).isEqualTo("Juan Perez");

        Optional<Cliente> byEmail = clienteRepository.findByEmail("juan@example.com");
        assertThat(byEmail).isPresent();

        List<Cliente> byName = clienteRepository.findByNombreContainingIgnoreCase("juan");
        assertThat(byName).hasSize(1);
    }
}

