package es.unex.cum.mdai.compradoor.data.services;

import es.unex.cum.mdai.compradoor.data.model.Cliente;
import es.unex.cum.mdai.compradoor.data.model.Compra;
import es.unex.cum.mdai.compradoor.data.model.Inmueble;
import es.unex.cum.mdai.compradoor.data.model.Venta;
import es.unex.cum.mdai.compradoor.data.repository.ClienteRepository;
import es.unex.cum.mdai.compradoor.data.repository.CompraRepository;
import es.unex.cum.mdai.compradoor.data.repository.InmuebleRepository;
import es.unex.cum.mdai.compradoor.data.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;
    private final ClienteRepository clienteRepository;
    private final InmuebleRepository inmuebleRepository;
    private final CompraRepository compraRepository;

    @Autowired
    public VentaServiceImpl(VentaRepository ventaRepository,
                            ClienteRepository clienteRepository,
                            InmuebleRepository inmuebleRepository,
                            CompraRepository compraRepository) {
        this.ventaRepository = ventaRepository;
        this.clienteRepository = clienteRepository;
        this.inmuebleRepository = inmuebleRepository;
        this.compraRepository = compraRepository;
    }

    @Override
    public List<Venta> findVentaByCliente(Cliente cliente) {
        if (cliente == null) throw new IllegalArgumentException("Cliente no válido");
        return ventaRepository.findByCliente(cliente);
    }

    @Override
    public List<Venta> findVentaByInmueble(Inmueble inmueble) {
        if (inmueble == null) throw new IllegalArgumentException("Inmueble no válido");
        return ventaRepository.findByInmueble(inmueble);
    }

    @Override
    public List<Venta> findVentaByFechaVentaBetween(Date inicio, Date fin) {
        return ventaRepository.findByFechaVentaBetween(inicio, fin);
    }

    @Override
    public List<Venta> findAllVentas() {
        return ventaRepository.findAll();
    }

    @Override
    public Optional<Venta> findVentaById(UUID id) {
        return ventaRepository.findById(id);
    }

    @Override
    public Venta saveVenta(Venta venta) {
        if (venta == null || venta.getCliente() == null || venta.getInmueble() == null) {
            throw new IllegalArgumentException("Datos de venta incompletos");
        }
        return ventaRepository.save(venta);
    }

    @Override
    public void deleteVenta(Venta venta) {
        if (venta == null) {
            throw new IllegalArgumentException("Venta no válida");
        }

        // 1. Gestionar el Inmueble asociado
        if (venta.getInmueble() != null) {
            Inmueble inmueble = venta.getInmueble();

            // A. Borrar Compras asociadas (y sus servicios por cascada)
            List<Compra> comprasAsociadas = compraRepository.findByInmueble(inmueble);
            if (!comprasAsociadas.isEmpty()) {
                compraRepository.deleteAll(comprasAsociadas);
                compraRepository.flush(); // Forzar borrado inmediato
            }

            // B. ¡CORRECCIÓN DEL ERROR 500!
            // Debemos romper la relación bidireccional antes de borrar la venta.
            // Si no hacemos esto, Hibernate intenta guardar el Inmueble que apunta a una Venta borrada.
            inmueble.setVenta(null);
            inmuebleRepository.save(inmueble); // Guardamos el inmueble "libre"
        }

        // 2. Ahora es seguro borrar la Venta
        ventaRepository.delete(venta);
    }
}