package es.unex.cum.mdai.compradoor.data.repository;

import es.unex.cum.mdai.compradoor.data.model.Inmueble;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InmuebleRepository extends JpaRepository<Inmueble, UUID> {

    List<Inmueble> findByLocalidadIgnoreCase(String localidad);

    List<Inmueble> findByPrecioBetween(Float min, Float max);

    List<Inmueble> findByVentaIsNull();
}