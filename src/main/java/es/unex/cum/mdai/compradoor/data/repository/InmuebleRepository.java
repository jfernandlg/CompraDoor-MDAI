package es.unex.cum.mdai.compradoor.data.repository;

import es.unex.cum.mdai.compradoor.data.model.Inmueble;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InmuebleRepository extends JpaRepository<Inmueble, UUID> {

    // Buscar por localidad (Podrías necesitar actualizar este también si lo usas)
    List<Inmueble> findByLocalidadIgnoreCase(String localidad);

    // ESTE ES EL NUEVO PARA EL BUSCADOR: Rango de precio Y que no esté vendido
    List<Inmueble> findByVentaIsNullAndPrecioBetween(Float min, Float max);

    // ESTE ES PARA EL CATÁLOGO PRINCIPAL: Todo lo que no tenga venta asignada
    List<Inmueble> findByVentaIsNull();
}