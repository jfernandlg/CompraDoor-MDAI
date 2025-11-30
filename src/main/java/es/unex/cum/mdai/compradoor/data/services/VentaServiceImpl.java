package es.unex.cum.mdai.compradoor.data.services;

import es.unex.cum.mdai.compradoor.data.model.Cliente;
import es.unex.cum.mdai.compradoor.data.model.Compra;
import es.unex.cum.mdai.compradoor.data.model.Inmueble;
import es.unex.cum.mdai.compradoor.data.model.Servicio; // Importar Modelo
import es.unex.cum.mdai.compradoor.data.model.Venta;
import es.unex.cum.mdai.compradoor.data.repository.ClienteRepository;
import es.unex.cum.mdai.compradoor.data.repository.CompraRepository;
import es.unex.cum.mdai.compradoor.data.repository.InmuebleRepository;
import es.unex.cum.mdai.compradoor.data.repository.ServicioRepository; // Importar Repo
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
    // 1. AÑADIMOS EL REPOSITORIO DE SERVICIOS
    private final ServicioRepository servicioRepository;

    @Autowired
    public VentaServiceImpl(VentaRepository ventaRepository,
                            ClienteRepository clienteRepository,
                            InmuebleRepository inmuebleRepository,
                            CompraRepository compraRepository,
                            ServicioRepository servicioRepository) { // 2. INYECTAMOS EN CONSTRUCTOR
        this.ventaRepository = ventaRepository;
        this.clienteRepository = clienteRepository;
        this.inmuebleRepository = inmuebleRepository;
        this.compraRepository = compraRepository;
        this.servicioRepository = servicioRepository;
    }

    // ... (El resto de métodos findAll, findById, save se mantienen igual) ...

    @Override
    public List<Venta> findVentaByCliente(Cliente cliente) {
        return ventaRepository.findByCliente(cliente);
    }

    @Override
    public List<Venta> findVentaByInmueble(Inmueble inmueble) {
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

    // ================================================================
    // AQUÍ ESTÁ LA CORRECCIÓN DEL ERROR 500
    // ================================================================
    @Override
    public void deleteVenta(Venta venta) {
        if (venta == null) {
            throw new IllegalArgumentException("Venta no válida");
        }

        // 1. Gestionar el Inmueble asociado (Lógica existente)
        if (venta.getInmueble() != null) {
            Inmueble inmueble = venta.getInmueble();

            // A. Borrar Compras asociadas
            List<Compra> comprasAsociadas = compraRepository.findByInmueble(inmueble);
            if (!comprasAsociadas.isEmpty()) {
                compraRepository.deleteAll(comprasAsociadas);
                compraRepository.flush();
            }

            // B. Desvincular Inmueble
            inmueble.setVenta(null);
            inmuebleRepository.save(inmueble);
        }

        // 2. NUEVO: BORRAR SERVICIOS ASOCIADOS A LA VENTA
        // Como estamos en una transacción (@Transactional), podemos acceder a la lista Lazy
        List<Servicio> serviciosAsociados = venta.getServicios();
        if (serviciosAsociados != null && !serviciosAsociados.isEmpty()) {
            // Opción A: Borrarlos (lo más lógico si borras la venta)
            servicioRepository.deleteAll(serviciosAsociados);

            // Forzamos el vaciado para evitar conflictos en memoria
            servicioRepository.flush();
            venta.setServicios(null);
        }

        // 3. Finalmente borramos la venta
        ventaRepository.delete(venta);
    }
}