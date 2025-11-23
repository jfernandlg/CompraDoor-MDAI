package es.unex.cum.mdai.compradoor.data.services;

import es.unex.cum.mdai.compradoor.data.model.Inmueble;
import es.unex.cum.mdai.compradoor.data.repository.InmuebleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InmuebleServiceImpl  implements InmuebleService {

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
            throw new NullPointerException("Id de Inmueble no válido");
        }
        return Optional.empty();
    }

    @Override
    public List<Inmueble> findInmuebleByPrecioBetween(Float min, Float max) {
        return List.of();
    }

    @Override
    public Inmueble saveInmueble(Inmueble inmueble) {
        return null;
    }

    @Override
    public void deleteInmueble(UUID id) {

    }
}
