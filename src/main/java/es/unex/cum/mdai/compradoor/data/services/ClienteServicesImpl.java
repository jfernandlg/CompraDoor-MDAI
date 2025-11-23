package es.unex.cum.mdai.compradoor.data.services;

import es.unex.cum.mdai.compradoor.data.model.Cliente;
import es.unex.cum.mdai.compradoor.data.model.Tarjeta;
import es.unex.cum.mdai.compradoor.data.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClienteServicesImpl implements ClienteService{

    private final ClienteRepository clienteRepository;

    public ClienteServicesImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public List<Cliente> findAllClientes() {
        return clienteRepository.findAll();
    }

    @Override
    public Optional<Cliente> findClienteById(UUID id) {
        if(id == null){
            throw new IllegalArgumentException("ID de cliente invalido " + id);
        }
        return clienteRepository.findById(id);
    }

    @Override
    public Cliente saveCliente(Cliente cliente) {
        if (cliente.getNombre() == null) {
            throw new IllegalArgumentException("Nombre de cliente es obligatorio");
        }
        if (cliente.getDni() == null) {
            throw new IllegalArgumentException("DNI de cliente es obligatorio");
        }
        //? Idea: Poder comprobar el DNI con regex (Expresión regular)
        if (cliente.getDni().length() != 9) {
            throw new IllegalArgumentException("DNI de cliente debe contener 9 caracteres");
        }
        if (cliente.getEmail() == null) {
            throw new IllegalArgumentException("Email de cliente es obligatorio");
        }
        return clienteRepository.save(cliente);
    }

    @Override
    public void deleteCliente(UUID id) {
        if(id ==null){
            throw new IllegalArgumentException("ID de cliente invalido " + id);
        }
        if(!clienteRepository.existsById(id)){
            throw new IllegalArgumentException("ID de cliente no encontrado " + id);
        }
        clienteRepository.deleteById(id);
    }
}
