package es.unex.cum.mdai.compradoor.data.services;

import es.unex.cum.mdai.compradoor.data.model.Cliente;
import es.unex.cum.mdai.compradoor.data.model.Compra;
import es.unex.cum.mdai.compradoor.data.model.Inmueble;
import es.unex.cum.mdai.compradoor.data.repository.ClienteRepository;
import es.unex.cum.mdai.compradoor.data.repository.CompraRepository;
import es.unex.cum.mdai.compradoor.data.repository.InmuebleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CompraServiceImpl implements CompraService {

    private final CompraRepository compraRepository;

    private final ClienteRepository clienteRepository;

    private final InmuebleRepository inmuebleRepository;

    public CompraServiceImpl(CompraRepository compraRepository, ClienteRepository clienteRepository, InmuebleRepository inmuebleRepository) {
        this.compraRepository = compraRepository;
        this.clienteRepository = clienteRepository;
        this.inmuebleRepository = inmuebleRepository;
    }

    @Override
    public List<Compra> findAllComprasByCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente no valido");
        }
        Optional<Cliente> optionalCliente = clienteRepository.findById(cliente.getId());
        if (optionalCliente.isEmpty()) {
            throw new IllegalArgumentException("Cliente no encontrado");
        }
        return compraRepository.findByCliente(cliente);
    }

    @Override
    public List<Compra> findAllComprasByInmueble(Inmueble inmueble) {
        if (inmueble == null) {
            throw new IllegalArgumentException("Inmueble no valido");
        }
        Optional<Inmueble> optionalInmueble = inmuebleRepository.findById(inmueble.getIdInmueble());
        if (optionalInmueble.isEmpty()) {
            throw new IllegalArgumentException("Inmueble no encontrado");
        }
        return compraRepository.findByInmueble(inmueble);
    }

    @Override
    public List<Compra> findAllComprasByFecha(Date fechaInicio, Date fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Fecha no valido");
        }
        if (fechaInicio.before(fechaFin)) {
            throw new IllegalArgumentException("Fecha no valido");
        }
        return compraRepository.findByFechaCompraBetween(fechaInicio, fechaFin);
    }

    @Override
    public List<Compra> findAllCompras() {
        return compraRepository.findAll();
    }

    @Override
    public Compra saveCompra(Compra compra) {
        if (compra == null) {
            throw new IllegalArgumentException("Compra no valido");
        }
        Optional<Cliente> optionalCliente = clienteRepository.findById(compra.getCliente().getId());
        if (optionalCliente.isEmpty()) {
            throw new IllegalArgumentException("Cliente no encontrado");
        }
        Optional<Inmueble>optionalInmueble = inmuebleRepository.findById(compra.getInmueble().getIdInmueble());
        if (optionalInmueble.isEmpty()) {
            throw new IllegalArgumentException("Inmueble no encontrado");
        }
        if (compra.getFechaCompra() == null) {
            throw new IllegalArgumentException("Fecha no valido");
        }
        if (compra.getPrecioCompra() == 0) {
            throw new IllegalArgumentException("Precio no valido");
        }
        return compraRepository.save(compra);
    }

    @Override
    public void deleteCompra(Compra compra) {
        if (compra == null) {
            throw new IllegalArgumentException("Compra no valido");
        }
        Optional<Compra> optionalCompra = compraRepository.findById(compra.getIdCompra());
        if (optionalCompra.isEmpty()) {
            throw new IllegalArgumentException("Compra no encontrado");
        }
        compraRepository.delete(compra);
    }
}
