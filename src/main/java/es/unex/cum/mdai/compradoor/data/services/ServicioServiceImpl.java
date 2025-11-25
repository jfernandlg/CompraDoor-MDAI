package es.unex.cum.mdai.compradoor.data.services;

import es.unex.cum.mdai.compradoor.data.model.Cliente;
import es.unex.cum.mdai.compradoor.data.model.Servicio;
import es.unex.cum.mdai.compradoor.data.model.TipoServicio;
import es.unex.cum.mdai.compradoor.data.model.Venta;
import es.unex.cum.mdai.compradoor.data.repository.ClienteRepository;
import es.unex.cum.mdai.compradoor.data.repository.ServicioRepository;
import es.unex.cum.mdai.compradoor.data.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ServicioServiceImpl implements ServicioService {

    public final ServicioRepository servicioRepository;

    public final VentaRepository ventaRepository;

    public final ClienteRepository clienteRepository;

    @Autowired
    public ServicioServiceImpl(ServicioRepository servicioRepository, VentaRepository ventaRepository, ClienteRepository clienteRepository) {
        this.servicioRepository = servicioRepository;
        this.ventaRepository = ventaRepository;
        this.clienteRepository = clienteRepository;
    }

    @Override
    public List<Servicio> findAllServicios() {
        return servicioRepository.findAll();
    }

    @Override
    public List<Servicio> findServicioByTipoServicio(TipoServicio tipoServicio) {
        if (tipoServicio == null) {
            throw new IllegalArgumentException("TipoServicio no valido");
        }
        return servicioRepository.findByTipoServicio(tipoServicio);
    }

    @Override
    public List<Servicio> findServicioByVenta(Venta venta) {
        if (venta == null) {
            throw new IllegalArgumentException("Venta no es valida");
        }
        Optional<Venta> optionalVenta = ventaRepository.findById(venta.getIdVenta());
        if (optionalVenta.isEmpty()) {
            throw new IllegalArgumentException("Venta no encontrada");
        }
        return servicioRepository.findByVenta(venta);
    }

    @Override
    public List<Servicio> findServicioByPrecioCompraBetween(float min, float max) {
        if (min > max) {
            throw new IllegalArgumentException("Precio compra no valido");
        }
        if (min == 0 || max == 0) {
            throw new IllegalArgumentException("Precio compra no valido");
        }
        return servicioRepository.findByCosteBetween(min, max);
    }

    @Override
    public List<Servicio> findServicioByFecha(Date fechaInicio, Date fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Fecha no valida");
        }
        if (fechaInicio.before(fechaFin)) {
            throw new IllegalArgumentException("Fecha no valida");
        }
        return servicioRepository.findByFechaAplicacionBetween(fechaInicio, fechaFin);
    }

    @Override
    public Servicio saveServicio(Servicio servicio) {
        if (servicio == null) {
            throw new IllegalArgumentException("Servicio no valido");
        }
        if (servicio.getTipoServicio() == null) {
            throw new IllegalArgumentException("TipoServicio no valido");
        }
        if (servicio.getCoste() == 0) {
            throw new IllegalArgumentException("Coste no valido");
        }
        return servicioRepository.save(servicio);
    }

    @Override
    public void deleteServicio(Servicio servicio) {
        if (servicio == null) {
            throw new IllegalArgumentException("Servicio no valido");
        }
        Optional<Servicio> optionalServicio = servicioRepository.findById(servicio.getIdServicio());
        if (optionalServicio.isEmpty()) {
            throw new IllegalArgumentException("Servicio no encontrado");
        }
        servicioRepository.delete(servicio);

    }
}
