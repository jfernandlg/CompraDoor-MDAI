package es.unex.cum.mdai.compradoor.data.services;

import es.unex.cum.mdai.compradoor.data.model.Inmueble;
import es.unex.cum.mdai.compradoor.data.repository.InmuebleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
public class InmuebleServiceImpl implements InmuebleService {

    private final InmuebleRepository inmuebleRepository;

    public InmuebleServiceImpl(InmuebleRepository inmuebleRepository) {
        this.inmuebleRepository = inmuebleRepository;
    }

    @Override
    public List<Inmueble> findAllInmueble() {
        return inmuebleRepository.findAll();
    }

    @Override
    public Optional<Inmueble> findInmuebleById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Id de Inmueble no válido");
        }
        return Optional.empty();
    }

    @Override
    public List<Inmueble> findInmuebleByPrecioBetween(Float min, Float max) {
        if (min == null || max == null) {
            throw new IllegalArgumentException("Precio de Inmueble no valido");
        }
        if (min > max) {
            throw new IllegalArgumentException("Precio de Inmueble no valido");
        }
        if (min <= 0) {
            throw new IllegalArgumentException("Precio de Inmueble no valido");
        }
        return inmuebleRepository.findByPrecioBetween(min, max);
    }

    @Override
    public Inmueble saveInmueble(Inmueble inmueble) {
        if (inmueble.getPrecio() == null || inmueble.getPrecio() <= 0) {
            throw new IllegalArgumentException("Precio de Inmueble no válido");
        }
        if (inmueble.getDireccion() == null) {
            throw new IllegalArgumentException("Direccion de Inmueble no puede ser nula");
        }
        if (inmueble.getLocalidad() == null) {
            throw new IllegalArgumentException("Localidad de Inmueble no puede ser nula");
        }
        return inmuebleRepository.save(inmueble);
    }

    @Override
    public void deleteInmueble(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Identificador de inmueble no puede ser nulo");
        }
        if (!inmuebleRepository.existsById(id)) {
            throw new IllegalArgumentException("Inmueble no encontrado");
        }
        inmuebleRepository.deleteById(id);
    }
}
