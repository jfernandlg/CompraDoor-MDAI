package es.unex.cum.mdai.compradoor.data.services;

import es.unex.cum.mdai.compradoor.data.model.Compra;
import es.unex.cum.mdai.compradoor.data.model.Servicio;
import es.unex.cum.mdai.compradoor.data.model.TipoServicio;
import es.unex.cum.mdai.compradoor.data.repository.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class ServicioServiceImpl implements ServicioService {

    private final ServicioRepository servicioRepository;

    @Autowired
    public ServicioServiceImpl(ServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    @Override
    public List<Servicio> findAllServicios() {
        return servicioRepository.findAll();
    }

    @Override
    public Optional<Servicio> findServicioById(UUID id) {
        return servicioRepository.findById(id);
    }

    @Override
    public List<Servicio> findServicioByTipoServicio(TipoServicio tipoServicio) {
        return servicioRepository.findByTipoServicio(tipoServicio);
    }

    // Método actualizado
    @Override
    public List<Servicio> findServicioByCompra(Compra compra) {
        return servicioRepository.findByCompra(compra);
    }

    @Override
    public List<Servicio> findServicioByPrecioCompraBetween(float min, float max) {
        return servicioRepository.findByCosteBetween(min, max);
    }

    @Override
    public List<Servicio> findServicioByFecha(Date fechaInicio, Date fechaFin) {
        if (fechaInicio != null && fechaFin != null && fechaInicio.after(fechaFin)) {
            throw new IllegalArgumentException("Fecha inicio posterior a fecha fin");
        }
        return servicioRepository.findByFechaAplicacionBetween(fechaInicio, fechaFin);
    }

    @Override
    public Servicio saveServicio(Servicio servicio) {
        if (servicio.getTipoServicio() == null) throw new IllegalArgumentException("El tipo es obligatorio");
        if (servicio.getDescripcion() == null || servicio.getDescripcion().isBlank()) throw new IllegalArgumentException("La descripción es obligatoria");
        // Nota: Ya no exigimos Venta obligatoria aquí porque ahora es Compra,
        // y se setea desde CompraController.
        return servicioRepository.save(servicio);
    }

    @Override
    public void deleteServicio(Servicio servicio) {
        if (servicio != null) {
            servicioRepository.delete(servicio);
        }
    }
    @Override
    public List<Servicio> findCatalogo() {
        return servicioRepository.findByCompraIsNull();
    }
}