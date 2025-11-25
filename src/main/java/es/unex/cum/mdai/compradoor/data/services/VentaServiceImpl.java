package es.unex.cum.mdai.compradoor.data.services;

import es.unex.cum.mdai.compradoor.data.model.Cliente;
import es.unex.cum.mdai.compradoor.data.model.Inmueble;
import es.unex.cum.mdai.compradoor.data.model.Venta;
import es.unex.cum.mdai.compradoor.data.repository.ClienteRepository;
import es.unex.cum.mdai.compradoor.data.repository.InmuebleRepository;
import es.unex.cum.mdai.compradoor.data.repository.VentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VentaServiceImpl implements VentaService {

    public final VentaRepository ventaRepository;

    public final ClienteRepository clienteRepository;

    public final InmuebleRepository inmuebleRepository;

    public VentaServiceImpl(VentaRepository ventaRepository, ClienteRepository clienteRepository, InmuebleRepository inmuebleRepository) {
        this.ventaRepository = ventaRepository;
        this.clienteRepository = clienteRepository;
        this.inmuebleRepository = inmuebleRepository;
    }

    @Override
    public List<Venta> findVentaByCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente no es válido");
        }
        return ventaRepository.findByCliente(cliente);
    }

    @Override
    public List<Venta> findVentaByInmueble(Inmueble inmueble) {
        if (inmueble == null) {
            throw new IllegalArgumentException("Inmueble no es valido");
        }
        return ventaRepository.findByInmueble(inmueble);
    }

    @Override
    public List<Venta> findVentaByFechaVentaBetween(Date inicio, Date fin) {
        if (inicio == null || fin == null) {
            throw new IllegalArgumentException("Fechas no validas");
        }
        if (inicio.before(fin)) {
            throw new IllegalArgumentException("Fechas no validas");
        }
        return ventaRepository.findByFechaVentaBetween(inicio, fin);
    }

    @Override
    public List<Venta> findAllVentas() {
        return ventaRepository.findAll();
    }

    @Override
    public Venta saveVenta(Venta venta) {
        if (venta == null) {
            throw new IllegalArgumentException("Venta no es valida");
        }
        if (venta.getCliente() == null) {
            throw new IllegalArgumentException("Cliente no es valido");
        }
        if (venta.getInmueble() == null) {
            throw new IllegalArgumentException("Inmueble no es valido");
        }
        if (venta.getFechaVenta() == null) {
            throw new IllegalArgumentException("Fecha no es valido");
        }
        if (venta.getPrecioVenta() == 0) {
            throw new IllegalArgumentException("Precio no es valido");
        }
        return ventaRepository.save(venta);
    }

    @Override
    public void deleteVenta(Venta venta) {
        if (venta == null) {
            throw new IllegalArgumentException("Venta no es valido");
        }
        Optional<Venta> optionalVenta = ventaRepository.findById(venta.getIdVenta());
        if (optionalVenta.isEmpty()) {
            throw new IllegalArgumentException("Venta no encontrada");
        }
        ventaRepository.delete(venta);
    }
}
