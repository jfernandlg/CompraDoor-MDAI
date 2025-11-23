package es.unex.cum.mdai.compradoor.data.services;

import es.unex.cum.mdai.compradoor.data.model.Cliente;
import es.unex.cum.mdai.compradoor.data.model.Tarjeta;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClienteService {
    List<Cliente> findAllClientes();
    Optional<Cliente> findClienteById(UUID id);
    Cliente saveCliente(Cliente cliente);
    void deleteCliente(UUID id);
}
