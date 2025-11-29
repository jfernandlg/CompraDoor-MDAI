package es.unex.cum.mdai.compradoor.data.services;

import es.unex.cum.mdai.compradoor.data.model.Compra;
import es.unex.cum.mdai.compradoor.data.model.Servicio;
import es.unex.cum.mdai.compradoor.data.model.TipoServicio;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServicioService {

    List<Servicio> findAllServicios();

    Optional<Servicio> findServicioById(UUID id);

    List<Servicio> findServicioByTipoServicio(TipoServicio tipoServicio);

    // Cambiado de Venta a Compra
    List<Servicio> findServicioByCompra(Compra compra);

    List<Servicio> findServicioByPrecioCompraBetween(float min, float max);

    List<Servicio> findServicioByFecha(Date fechaInicio, Date fechaFin);

    Servicio saveServicio(Servicio servicio);

    List<Servicio> findCatalogo(); // Añade esto

    void deleteServicio(Servicio servicio);
}