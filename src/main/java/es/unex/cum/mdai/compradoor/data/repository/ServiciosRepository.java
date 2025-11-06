package es.unex.cum.mdai.compradoor.data.repository;

import es.unex.cum.mdai.compradoor.data.model.Servicios;
import es.unex.cum.mdai.compradoor.data.model.TipoServicio;
import es.unex.cum.mdai.compradoor.data.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface ServiciosRepository extends JpaRepository<Servicios, UUID> {

    List<Servicios> findByTipoServicio(TipoServicio tipo);

    List<Servicios> findByDescripcionContainingIgnoreCase(String texto);

    List<Servicios> findByVenta(Venta venta);

    long countByVenta(Venta venta);

    boolean existsByVenta(Venta venta);

    List<Servicios> findByCosteBetween(float coste1, float coste2);

    List<Servicios> findByFechaAplicacionBetween(Date fechaInicio, Date fechaFin);
}
