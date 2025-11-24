package es.unex.cum.mdai.compradoor.data.services;

import es.unex.cum.mdai.compradoor.data.model.Cliente;
import es.unex.cum.mdai.compradoor.data.model.Tarjeta;
import es.unex.cum.mdai.compradoor.data.repository.ClienteRepository;
import es.unex.cum.mdai.compradoor.data.repository.TarjetaRepository;
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
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente no valido");
        }
        boolean existCliente = clienteRepository.existsById(cliente.getId());
        if (!existCliente) {
            throw new IllegalArgumentException("Cliente no encontrado");
        }
        return tarjetaRepository.findByCliente(cliente);
    }

    @Override
    public Tarjeta saveTarjeta(Tarjeta tarjeta) {
        if (tarjeta == null) {
            throw new IllegalArgumentException("Tarjeta no valida");
        }
        Optional<Tarjeta> optionalTarjetaExist = tarjetaRepository.findByCodigoTarjeta(tarjeta.getCodigoTarjeta());
        if (optionalTarjetaExist.isPresent()) {
            throw new IllegalArgumentException("Codigo de tarjeta existente");
        }
        Optional<Tarjeta> optionalTarjetaExistById = tarjetaRepository.findById(tarjeta.getId());
        if (optionalTarjetaExistById.isPresent()) {
            throw new IllegalArgumentException("Id de tarjeta existente");
        }
        String codigoTajetaRegex = "\\d{16}";
        if (!tarjeta.getCodigoTarjeta().matches(codigoTajetaRegex)) {
            throw new IllegalArgumentException("Codigo de tarjeta no valido");
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
