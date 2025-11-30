package es.unex.cum.mdai.compradoor.data.services;

import es.unex.cum.mdai.compradoor.data.model.*;
import es.unex.cum.mdai.compradoor.data.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class CompraServiceImpl implements CompraService {

    private final CompraRepository compraRepository;

    private final ClienteRepository clienteRepository;

    private final InmuebleRepository inmuebleRepository;

    private final VentaRepository ventaRepository;

    private final ServicioRepository servicioRepository;

    @Autowired
    public CompraServiceImpl(CompraRepository compraRepository, ClienteRepository clienteRepository, InmuebleRepository inmuebleRepository, VentaRepository ventaRepository, ServicioRepository servicioRepository) {
        this.compraRepository = compraRepository;
        this.clienteRepository = clienteRepository;
        this.inmuebleRepository = inmuebleRepository;
        this.ventaRepository = ventaRepository;
        this.servicioRepository = servicioRepository;
    }

    @Override
    public Optional<Compra> findCompraById(UUID id) {
        return compraRepository.findById(id);
    }

    @Override
    public List<Compra> findAllComprasByClienteId(UUID clienteId) {
        return compraRepository.findByClienteId(clienteId);
    }

    @Override
    public List<Compra> findAllComprasByInmuebleId(UUID inmuebleId) {
        return compraRepository.findByInmuebleIdInmueble(inmuebleId);
    }


    @Override
    public List<Compra> findAllComprasByFecha(Date fechaInicio, Date fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Fechas no válidas");
        }
        if (fechaInicio.after(fechaFin)) { // CORRECCIÓN: estaba al revés
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha fin");
        }
        return compraRepository.findByFechaCompraBetween(fechaInicio, fechaFin);
    }

    @Override
    public List<Compra> findAllCompras() {
        return compraRepository.findAll();
    }

    @Override
    public void realizarCompra(Cliente cliente, Inmueble inmueble) {
        Compra compra = new Compra();
        compra.setCliente(cliente);
        compra.setInmueble(inmueble);
        compra.setFechaCompra(new Date());
        if (inmueble.getPrecio() != null) {
            compra.setPrecioCompra(inmueble.getPrecio());
        } else {
            compra.setPrecioCompra(0.0f);
        }

        compraRepository.save(compra);

        Venta venta = new Venta();
        venta.setCliente(cliente);
        venta.setInmueble(inmueble);
        venta.setFechaVenta(new Date());
        venta.setPrecioVenta(compra.getPrecioCompra());
        ventaRepository.save(venta);


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
        Optional<Inmueble> optionalInmueble = inmuebleRepository.findById(compra.getInmueble().getIdInmueble());
        if (optionalInmueble.isEmpty()) {
            throw new IllegalArgumentException("Inmueble no encontrado");
        }
        if (compra.getFechaCompra() == null) {
            throw new IllegalArgumentException("Fecha no valido");
        }
        if (compra.getPrecioCompra() == 0) {
            throw new IllegalArgumentException("Precio no valido");
        }
        // Validar que el inmueble no haya sido vendido previamente
        List<Compra> comprasExistentes = compraRepository.findByInmueble(compra.getInmueble());
        if (!comprasExistentes.isEmpty()) {
            throw new IllegalArgumentException("El inmueble ya ha sido vendido anteriormente");
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

    @Override
    public void realizarCompraConServicios(Cliente cliente, Inmueble inmueble, List<UUID> serviciosIds, UUID tarjetaId) {

        float precioBase = (inmueble.getPrecio() != null) ? inmueble.getPrecio() : 0.0f;

        float gastosGestion = precioBase * 0.02f;
        float precioTotal = precioBase +  gastosGestion;

        // 1. Crear y Guardar la COMPRA (Historial Cliente)
        Compra compra = new Compra();
        compra.setCliente(cliente);
        compra.setInmueble(inmueble);
        compra.setFechaCompra(new Date());
        // Guardamos temporalmente con precio base
        compra.setPrecioCompra(precioBase);
        compraRepository.save(compra);

        // 2. Crear y Guardar la VENTA (Historial Inmobiliaria)
        // Guardamos la venta ANTES de asociar los servicios para tener el ID
        Venta venta = new Venta();
        venta.setCliente(cliente);
        venta.setInmueble(inmueble);
        venta.setFechaVenta(new Date());
        venta.setPrecioVenta(precioBase);
        ventaRepository.save(venta); // ¡IMPORTANTE! Save aquí para tener ID

        // 3. Procesar Servicios Extra
        if (serviciosIds != null && !serviciosIds.isEmpty()) {
            List<Servicio> serviciosExtra = servicioRepository.findAllById(serviciosIds);

            for (Servicio s : serviciosExtra) {
                precioTotal += s.getCoste();

                // CAMBIO CRÍTICO: Asociamos el servicio a la VENTA, no a la compra
                // Esto arregla el error de Thymeleaf "Property 'servicios' not found on Venta"
                s.setVenta(venta);
                s.setCompra(null); // Limpiamos referencia a compra por si acaso existía en tu modelo viejo

                servicioRepository.save(s); // Guardamos el servicio con la nueva relación
            }

            // Si tu clase Venta tiene una lista, Java la actualiza en memoria,
            // pero es bueno refrescar.
            venta.setServicios(serviciosExtra);
        }

        // 4. Actualizar Precios Finales (Base + Extras)
        compra.setPrecioCompra(precioTotal);
        venta.setPrecioVenta(precioTotal);

        // 5. Guardar cambios de precio
        compraRepository.save(compra);
        ventaRepository.save(venta);
    }

}
