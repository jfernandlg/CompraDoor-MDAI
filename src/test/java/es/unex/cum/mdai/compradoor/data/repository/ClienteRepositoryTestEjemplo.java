package es.unex.cum.mdai.compradoor.data.repository;

import es.unex.cum.mdai.compradoor.data.model.Cliente;
import es.unex.cum.mdai.compradoor.data.model.Tarjeta;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.Scanner;
import jakarta.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Scanner;
import java.util.stream.IntStream;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ClienteRepositoryTestEjemplo {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private TarjetaRepository tarjetaRepository;

    @Test
    void testEjemplo() {
        // Crear cliente
        Cliente cliente = new Cliente("99999999Z", "Cliente Ejemplo");
        cliente.setEmail("ejemplo@local.test");

        // Crear varias tarjetas y asociarlas al cliente (bidireccional)
        int NUM_TARJETAS = 10;
        for (int i = 0; i < NUM_TARJETAS; i++) {
            Tarjeta t = new Tarjeta();
            t.setCodigoTarjeta("TARJETA-" + i);
            t.setValida(true);
            t.setCliente(cliente);
            cliente.getTarjetas().add(t);
        }

        // Guardar el cliente (cascade debe persistir las tarjetas)
        Cliente saved = clienteRepository.save(cliente);

        // Comprobar que el cliente existe y las tarjetas están persistidas
        Optional<Cliente> optional = clienteRepository.findByDni("99999999Z");
        assertThat(optional).isPresent();

        Cliente persisted = optional.get();
        long tarjetasPersistidas = tarjetaRepository.countByCliente(persisted);
        assertThat(tarjetasPersistidas).isEqualTo(NUM_TARJETAS);

        // Eliminar el cliente
        clienteRepository.delete(persisted);

        // Verificar que no quedan tarjetas asociadas
        long countAfterDelete = tarjetaRepository.countByCliente(persisted);
        assertThat(countAfterDelete).isEqualTo(0);
    }

}

