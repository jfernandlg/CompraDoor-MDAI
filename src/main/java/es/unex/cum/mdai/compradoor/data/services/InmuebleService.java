package es.unex.cum.mdai.compradoor.data.services;


import es.unex.cum.mdai.compradoor.data.model.Inmueble;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InmuebleService {
    // Cambiamos la firma o añadimos uno nuevo. Yo prefiero ser explícito:
    List<Inmueble> findInmueblesDisponibles(); // <--- NUEVO

    // Mantenemos este para el admin si quiere ver todo
    List<Inmueble> findAllInmueble();

    Optional<Inmueble> findInmuebleById(UUID id);

    // Actualizamos este para que solo devuelva disponibles
    List<Inmueble> findInmuebleByPrecioBetween(Float min, Float max);

    Inmueble saveInmueble(Inmueble inmueble);
    void deleteInmueble(UUID id);
    Inmueble updateInmueble(Inmueble inmueble);
}