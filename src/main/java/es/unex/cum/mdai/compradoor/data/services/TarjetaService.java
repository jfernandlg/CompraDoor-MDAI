package es.unex.cum.mdai.compradoor.data.services;

import es.unex.cum.mdai.compradoor.data.model.Cliente;
import es.unex.cum.mdai.compradoor.data.model.Tarjeta;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TarjetaService {

    Optional<Tarjeta> findById(UUID id);

    Optional<Tarjeta> findByCodigo(String codigo);

    List<Tarjeta> findAllTarjetasByCliente(Cliente cliente);

    Tarjeta saveTarjeta(Tarjeta tarjeta);

    void deleteTarjeta(Tarjeta tarjeta);
}
