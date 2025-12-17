package es.unex.cum.mdai.compradoor.data.repository;

import es.unex.cum.mdai.compradoor.data.model.Cliente;
import es.unex.cum.mdai.compradoor.data.model.Tarjeta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ClienteTest {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private TarjetaRepository tarjetaRepository;

    @BeforeEach
    void setUp() {
        tarjetaRepository.deleteAll();
        clienteRepository.deleteAll();
    }

    @Test
    void saveAndFindByDniAndEmailAndName() {
        Cliente c = new Cliente("12345678A", "Juan");
        c.setEmail("juan@example.com");
        c.setPassword("password123");

        clienteRepository.save(c);

        Optional<Cliente> byDni = clienteRepository.findByDni("12345678A");
        assertThat(byDni).isPresent();
        Cliente saved = byDni.get();
        assertThat(saved.getNombre()).isEqualTo("Juan");

        Optional<Cliente> byEmail = clienteRepository.findByEmail("juan@example.com");
        assertThat(byEmail).isPresent();

        List<Cliente> byName = clienteRepository.findByNombreContainingIgnoreCase("juan");
        assertThat(byName).hasSize(1);
    }

    // Nuevo: comprobar findByEmail cuando existe
    @Test
    void findByEmailWhenExists() {
        Cliente c = new Cliente("87654321B", "Maria");
        c.setEmail("maria@example.com");
        c.setPassword("password123");
        clienteRepository.save(c);

        Optional<Cliente> byEmail = clienteRepository.findByEmail("maria@example.com");
        assertThat(byEmail).isPresent();
        assertThat(byEmail.get().getDni()).isEqualTo("87654321B");
    }

    // Nuevo: comprobar findByNombreContainingIgnoreCase con varios resultados y case-insensitive
    @Test
    void findByNombreContainingIgnoreCaseMultiple() {
        Cliente a = new Cliente("11111111A", "Ana");
        a.setEmail("ana@example.com");
        a.setPassword("password123");

        Cliente b = new Cliente("22222222B", "Mariana");
        b.setEmail("mariana@example.com");
        b.setPassword("password123");

        Cliente c = new Cliente("33333333C", "Carlos");
        c.setEmail("carlos@example.com");
        c.setPassword("password123");

        clienteRepository.save(a);
        clienteRepository.save(b);
        clienteRepository.save(c);

        List<Cliente> resultLower = clienteRepository.findByNombreContainingIgnoreCase("ana");
        assertThat(resultLower).hasSize(2); // Ana, Mariana

        List<Cliente> resultUpper = clienteRepository.findByNombreContainingIgnoreCase("ANA");
        assertThat(resultUpper).hasSize(2);
    }

    // Nuevo: comprobar comportamiento cuando no existe un DNI
    @Test
    void findByDniNotFound() {
        Optional<Cliente> byDni = clienteRepository.findByDni("NOEXISTE");
        assertThat(byDni).isNotPresent();
    }

    // Nuevo: CRUD básico - findAll y delete
    @Test
    void findAllAndDelete() {
        Cliente a = new Cliente("44444444D", "Luis");
        a.setEmail("luis@example.com");
        a.setPassword("password123");
        Cliente b = new Cliente("55555555E", "Sofía");
        b.setEmail("sofia@example.com");
        b.setPassword("password123");
        clienteRepository.save(a);
        clienteRepository.save(b);

        List<Cliente> all = clienteRepository.findAll();
        assertThat(all).hasSize(2);

        clienteRepository.delete(a);
        List<Cliente> afterDelete = clienteRepository.findAll();
        assertThat(afterDelete).hasSize(1);
        // usar AssertJ para evitar get(0) warning
        assertThat(afterDelete).first().extracting(Cliente::getDni).isEqualTo("55555555E");
    }

    // Nuevo: buscar clientes por el código de una tarjeta
    @Test
    void findByTarjetasCodigoTarjeta() {
        Cliente cliente = new Cliente("66666666F", "Pablo");
        cliente.setEmail("pablo@example.com");
        cliente.setPassword("password123");

        Tarjeta tarjeta = new Tarjeta("1234 5678 1234 5678", true, cliente);
        // establecer relación bidireccional
        cliente.getTarjetas().add(tarjeta);

        clienteRepository.save(cliente);

        List<Cliente> found = clienteRepository.findByTarjetasCodigoTarjeta("1234 5678 1234 5678");
        assertThat(found).hasSize(1);
        // usar AssertJ first() para evitar get(0) warning
        assertThat(found).first().extracting(Cliente::getDni).isEqualTo("66666666F");
    }

    // Nuevo: demostrar efecto de cascade + orphanRemoval en Cliente -> Tarjeta
    @Test
    void cascadeDeleteClienteRemovesTarjetas() {
        Cliente cliente = new Cliente("77777777G", "Lucía");
        cliente.setEmail("lucia@example.com");
        cliente.setPassword("password123");

        Tarjeta tarjeta = new Tarjeta("1234 5678 1234 5678", true, cliente);
        cliente.getTarjetas().add(tarjeta);

        clienteRepository.save(cliente);

        // compruebo que la tarjeta existe en su repo
        boolean existsBefore = tarjetaRepository.existsByCodigoTarjeta("1234 5678 1234 5678");
        assertThat(existsBefore).isTrue();

        // borro el cliente -> debería eliminar las tarjetas por cascade
        clienteRepository.delete(cliente);

        boolean existsAfter = tarjetaRepository.existsByCodigoTarjeta("1234 5678 1234 5678");
        assertThat(existsAfter).isFalse();

        Optional<Cliente> maybe = clienteRepository.findByDni("77777777G");
        assertThat(maybe).isNotPresent();
    }

    @Test
    void findByAdminFalse() {
        Cliente normal = new Cliente("11111111A", "Usuario Normal");
        normal.setEmail("normal@example.com");
        normal.setPassword("pass123");
        normal.setAdmin(false);

        // Cliente que sí es admin
        Cliente admin = new Cliente("22222222B", "Usuario Admin");
        admin.setEmail("admin@example.com");
        admin.setPassword("pass123");
        admin.setAdmin(true);

        clienteRepository.save(normal);
        clienteRepository.save(admin);

        List<Cliente> noAdmins = clienteRepository.findByAdminFalse();

        // Debería encontrar solo al usuario normal
        assertThat(noAdmins).hasSize(1);
        assertThat(noAdmins).first().extracting(Cliente::getDni).isEqualTo("11111111A");
    }
}
