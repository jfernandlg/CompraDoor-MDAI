package es.unex.cum.mdai.compradoor.data.repository;

import es.unex.cum.mdai.compradoor.data.model.Compra;
import es.unex.cum.mdai.compradoor.data.model.Servicio;
import es.unex.cum.mdai.compradoor.data.model.TipoServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, UUID> {

    List<Servicio> findByTipoServicio(TipoServicio tipoServicio);

    // Cambiado de Venta a Compra
    List<Servicio> findByCompra(Compra compra);

    List<Servicio> findByCosteBetween(float min, float max);

    List<Servicio> findByFechaAplicacionBetween(Date fechaInicio, Date fechaFin);
}
