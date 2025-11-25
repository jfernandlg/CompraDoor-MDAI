package es.unex.cum.mdai.compradoor.data.services;

import es.unex.cum.mdai.compradoor.data.model.Cliente;
import es.unex.cum.mdai.compradoor.data.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
public class ClienteServicesImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteServicesImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public List<Cliente> findAllClientes() {
        return clienteRepository.findAll();
    }

    @Override
    public Optional<Cliente> findClienteById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID de cliente invalido " + id);
        }
        return clienteRepository.findById(id);
    }

    @Override
    public Cliente saveCliente(Cliente cliente) {
        if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre de cliente es obligatorio");
        }
        //? Idea: Poder comprobar el DNI con regex (Expresión regular)
        String dniRegex = "\\d{8}[A-Z]";
        if (cliente.getDni() == null || !cliente.getDni().matches(dniRegex)) {
            throw new IllegalArgumentException("DNI de cliente no valido");
        }
        Optional<Cliente> optionalClienteByDni = clienteRepository.findByDni(cliente.getDni());
        if (optionalClienteByDni.isPresent()) {
            throw new IllegalArgumentException("DNI de cliente ya existente:  " + cliente.getDni());
        }
        if(cliente.getDni().trim().isEmpty()){
            throw new IllegalArgumentException("DNI de cliente es obligatorio");
        }
        if (cliente.getEmail() == null || cliente.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email de cliente es obligatorio");
        }
        if (!cliente.getEmail().contains("@")){
            throw new IllegalArgumentException("Email de cliente no valido");
        }
        Optional<Cliente> optionalCliente = clienteRepository.findByEmail(cliente.getEmail());
        if (optionalCliente.isPresent()) {
            throw new IllegalArgumentException("Email de cliente ya existente:  " + cliente.getEmail());
        }
        return clienteRepository.save(cliente);
    }

    @Override
    public void deleteCliente(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID de cliente invalido " + id);
        }
        if (!clienteRepository.existsById(id)) {
            throw new IllegalArgumentException("ID de cliente no encontrado " + id);
        }
        clienteRepository.deleteById(id);
    }

    @Override
    public Optional<Cliente> findClienteByDni(String dni) {
        if (dni == null) {
            throw new IllegalArgumentException("DNI de cliente no válido");
        }
        return clienteRepository.findByDni(dni);
    }

    @Override
    public Optional<Cliente> findClienteByEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("Email de cliente no válido");
        }
        return clienteRepository.findByEmail(email);
    }

//    @Override
//    public Cliente updateCliente(Cliente cliente) {
//        Optional<Cliente> optionalClienteExist = clienteRepository.findById(cliente.getId());
//        if (optionalClienteExist.isPresent()) {
//            throw new IllegalArgumentException("ID de cliente ya existente:  " + cliente.getId());
//        }
//        return null;
//    }


}
