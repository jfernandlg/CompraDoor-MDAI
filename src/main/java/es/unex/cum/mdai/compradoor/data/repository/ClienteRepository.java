package es.unex.cum.mdai.compradoor.data.repository;

import es.unex.cum.mdai.compradoor.data.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, UUID> {

    // Buscar por DNI único
    Optional<Cliente> findByDni(String dni);

    // Buscar por email
    Optional<Cliente> findByEmail(String email);

    // Buscar por nombre (contiene, case-insensitive)
    List<Cliente> findByNombreContainingIgnoreCase(String nombreParte);

    // Buscar clientes por el código de una de sus tarjetas (join implícito mediante convenciones de nombres)
    List<Cliente> findByTarjetasCodigoTarjeta(String codigoTarjeta);
}
