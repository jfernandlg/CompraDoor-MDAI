package es.unex.cum.mdai.compradoor.data.services;

import es.unex.cum.mdai.compradoor.data.model.Cliente;
import es.unex.cum.mdai.compradoor.data.model.Venta;
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

        if (cliente.getNombre() == null) {
            throw new IllegalArgumentException("Nombre de cliente es obligatorio");
        }
        if (cliente.getDni() == null) {
            throw new IllegalArgumentException("DNI de cliente no valido");
        }
        Optional<Cliente> optionalClienteByDni = clienteRepository.findByDni(cliente.getDni());
        if (optionalClienteByDni.isPresent()) {
            throw new IllegalArgumentException("DNI de cliente ya existente:  " + cliente.getDni());
        }
        if (cliente.getEmail() == null) {
            throw new IllegalArgumentException("Email de cliente es obligatorio");
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

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de cliente no encontrado"));

        if (cliente.isAdmin()) {
            throw new IllegalArgumentException("No se puede eliminar a un usuario Administrador");
        }

        for (Venta venta: cliente.getVentas()) {
            if(venta.getInmueble() != null){
                venta.getInmueble().setVenta(null);
            }
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

    @Override
    public Cliente updateCliente(Cliente cliente) {
        if (cliente.getId() == null || !clienteRepository.existsById(cliente.getId())) {
            throw new IllegalArgumentException("ID de cliente no encontrado " + cliente.getId());
        }

        Cliente clienteUpdate = clienteRepository.findByDni(cliente.getDni()).orElseThrow(()
                -> new IllegalArgumentException("DNI de cliente no encontrado"));

        if (!clienteUpdate.getDni().equals(cliente.getDni()) && clienteRepository.findByDni(cliente.getDni()).isPresent()) {
            throw new IllegalArgumentException("DNI de cliente ya existente:  " + cliente.getDni());
        }


        if (!clienteUpdate.getEmail().equals(cliente.getEmail()) && clienteRepository.findByEmail(cliente.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Email de cliente ya existente:  " + cliente.getEmail());
            }


        if (!clienteUpdate.getNombre().equals(cliente.getNombre())) {
            clienteUpdate.setNombre(cliente.getNombre());
        }

        clienteUpdate.setDni(cliente.getDni());
        clienteUpdate.setEmail(cliente.getEmail());
        clienteUpdate.setTelefono(cliente.getTelefono());
        clienteRepository.save(clienteUpdate);

        return clienteRepository.save(clienteUpdate);
    }

    @Override
    public List<Cliente> findClientesNoAdmin() {
        return clienteRepository.findByAdminFalse();
    }


}
