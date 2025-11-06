package es.unex.cum.mdai.compradoor.data.repository;

import es.unex.cum.mdai.compradoor.data.model.Servicio;
import es.unex.cum.mdai.compradoor.data.model.Servicio;
import es.unex.cum.mdai.compradoor.data.model.TipoServicio;
import es.unex.cum.mdai.compradoor.data.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, UUID> {
    List<Servicio> findByTipoServicio(TipoServicio tipoServicio);
    List<Servicio> findByDescripcionContainingIgnoreCase(String descripcion);
    List<Servicio> findByVenta(Venta venta);
    long countByVenta(Venta venta);
    boolean existsByVenta(Venta venta);
    List<Servicio> findByCosteBetween(Float minCoste, Float maxCoste);
    List<Servicio> findByFechaAplicacionBetween(Date startDate, Date endDate);
}
