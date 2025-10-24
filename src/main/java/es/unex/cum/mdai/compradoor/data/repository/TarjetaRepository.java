package es.unex.cum.mdai.compradoor.data.repository;

import es.unex.cum.mdai.compradoor.data.model.Tarjeta;
import es.unex.cum.mdai.compradoor.data.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TarjetaRepository extends JpaRepository<Tarjeta, UUID> {

    Optional<Tarjeta> findByCodigoTarjeta(String codigo);

    List<Tarjeta> findByCliente(Cliente cliente);

    boolean existsByCodigoTarjeta(String codigo);

    long countByCliente(Cliente cliente);
}
