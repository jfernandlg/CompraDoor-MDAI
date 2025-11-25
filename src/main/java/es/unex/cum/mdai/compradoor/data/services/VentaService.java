package es.unex.cum.mdai.compradoor.data.services;

import es.unex.cum.mdai.compradoor.data.model.Cliente;
import es.unex.cum.mdai.compradoor.data.model.Inmueble;
import es.unex.cum.mdai.compradoor.data.model.Venta;

import java.util.Date;
import java.util.List;

public interface VentaService {

    List<Venta> findVentaByCliente(Cliente cliente);

    List<Venta> findVentaByInmueble(Inmueble inmueble);

    List<Venta> findVentaByFechaVentaBetween(Date inicio, Date fin);

    List<Venta> findAllVentas();

    Venta saveVenta(Venta venta);

    void deleteVenta(Venta venta);
}
