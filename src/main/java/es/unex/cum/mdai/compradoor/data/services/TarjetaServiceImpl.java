package es.unex.cum.mdai.compradoor.data.services;

import es.unex.cum.mdai.compradoor.data.model.Cliente;
import es.unex.cum.mdai.compradoor.data.model.Tarjeta;
import es.unex.cum.mdai.compradoor.data.repository.ClienteRepository;
import es.unex.cum.mdai.compradoor.data.repository.TarjetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
public class TarjetaServiceImpl implements TarjetaService {

    private final TarjetaRepository tarjetaRepository;

    private final ClienteRepository clienteRepository;

    @Autowired
    public TarjetaServiceImpl(TarjetaRepository tarjetaRepository, ClienteRepository clienteRepository) {
        this.tarjetaRepository = tarjetaRepository;
        this.clienteRepository = clienteRepository;
    }

    @Override
    public Optional<Tarjeta> findById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("id de Tarjeta no válido");
        }
        return tarjetaRepository.findById(id);
    }

    @Override
    public Optional<Tarjeta> findByCodigo(String codigo) {
        if (codigo == null) {
            throw new IllegalArgumentException("Codigo de tarjeta no valido");
        }
        return tarjetaRepository.findByCodigoTarjeta(codigo);
    }

    @Override
    public List<Tarjeta> findAllTarjetasByCliente(Cliente cliente) {
        if (cliente == null || cliente.getId() == null) {
            throw new IllegalArgumentException("Cliente no valido");
        }

        return tarjetaRepository.findByCliente(cliente);
    }

    @Override
    public List<Tarjeta> findAllTarjetas() {
        return tarjetaRepository.findAll();
    }

    @Override
    public Tarjeta saveTarjeta(Tarjeta tarjeta) {
        if (tarjeta == null) {
            throw new IllegalArgumentException("Tarjeta no valida");
        }
        if (tarjeta.getCodigoTarjeta() == null || tarjeta.getCodigoTarjeta().trim().isEmpty()) {
            throw new IllegalArgumentException("Codigo de tarjeta es obligatorio");
        }

        String regexTarjeta = "\\d{16}";
        if(!tarjeta.getCodigoTarjeta().matches(regexTarjeta)) {
            throw new IllegalArgumentException("Codigo de tarjeta debe contener 16 dígitos numéricos");
        }

        Optional<Tarjeta> optionalTarjetaExistById = tarjetaRepository.findByCodigoTarjeta(tarjeta.getCodigoTarjeta());
        if (optionalTarjetaExistById.isPresent()) {
            if(tarjeta.getId() == null || !optionalTarjetaExistById.get().getId().equals(tarjeta.getId())) {
                throw new IllegalArgumentException("La tarjeta introducida ya existe en el sistema");
            }
        }

        return tarjetaRepository.save(tarjeta);
    }

    @Override
    public void deleteTarjeta(Tarjeta tarjeta) {
        if (tarjeta == null) {
            throw new IllegalArgumentException("Tarjeta no valida");
        }
        Optional<Tarjeta> optionalTarjetaExistById = tarjetaRepository.findById(tarjeta.getId());
        if (!optionalTarjetaExistById.isPresent()) {
            throw new IllegalArgumentException("Id de tarjeta existente");
        }
        tarjetaRepository.delete(tarjeta);

    }
}
