package es.unex.cum.mdai.compradoor.data.services;


import es.unex.cum.mdai.compradoor.data.model.Inmueble;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InmuebleService {

    List<Inmueble> findInmueblesDisponibles();

    List<Inmueble> findAllInmueble();

    Optional<Inmueble> findInmuebleById(UUID id);

    List<Inmueble> findInmuebleByPrecioBetween(Float min, Float max);

    Inmueble saveInmueble(Inmueble inmueble);

    void deleteInmueble(UUID id);

    Inmueble updateInmueble(Inmueble inmueble);
}