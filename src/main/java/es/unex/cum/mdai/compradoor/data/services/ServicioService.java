package es.unex.cum.mdai.compradoor.data.services;

import es.unex.cum.mdai.compradoor.data.model.Cliente;
import es.unex.cum.mdai.compradoor.data.model.Servicio;
import es.unex.cum.mdai.compradoor.data.model.TipoServicio;
import es.unex.cum.mdai.compradoor.data.model.Venta;

import java.util.Date;
import java.util.List;

public interface ServicioService {

    List<Servicio> findAllServicios();

    List<Servicio> findServicioByTipoServicio(TipoServicio tipoServicio);

    List<Servicio> findServicioByVenta(Venta venta);

    List<Servicio> findServicioByPrecioCompraBetween(float min, float max);

    List<Servicio> findServicioByFecha(Date fechaInicio, Date fechaFin);

    Servicio saveServicio(Servicio servicio);

    void deleteServicio(Servicio servicio);

}
