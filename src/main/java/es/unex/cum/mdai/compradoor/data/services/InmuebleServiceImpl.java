package es.unex.cum.mdai.compradoor.data.services;

import es.unex.cum.mdai.compradoor.data.model.Inmueble;
import es.unex.cum.mdai.compradoor.data.repository.InmuebleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
public class InmuebleServiceImpl implements InmuebleService {

    private final InmuebleRepository inmuebleRepository;

    @Autowired
    public InmuebleServiceImpl(InmuebleRepository inmuebleRepository) {
        this.inmuebleRepository = inmuebleRepository;
    }

    // --- MÉTODOS DE BÚSQUEDA ---

    @Override
    public List<Inmueble> findAllInmueble() {
        return inmuebleRepository.findAll();
    }

    @Override
    public List<Inmueble> findInmueblesDisponibles() {
        return inmuebleRepository.findByVentaIsNull();
    }

    @Override
    public Optional<Inmueble> findInmuebleById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Id de Inmueble no válido");
        }
        return inmuebleRepository.findById(id);
    }

    @Override
    public List<Inmueble> findInmuebleByPrecioBetween(Float min, Float max) {
        if (min == null || max == null) {
            throw new IllegalArgumentException("Rango de precio no válido");
        }
        if (min > max) {
            throw new IllegalArgumentException("El precio mínimo no puede ser mayor al máximo");
        }
        // Usamos el filtro 'AndVentaIsNull' para que el buscador no muestre casas vendidas
        return inmuebleRepository.findByVentaIsNullAndPrecioBetween(min, max);
    }

    // --- MÉTODOS DE PERSISTENCIA ---

    @Override
    public Inmueble saveInmueble(Inmueble inmueble) {
        if (inmueble == null) {
            throw new IllegalArgumentException("Inmueble no válido");
        }
        if (inmueble.getDireccion() == null || inmueble.getDireccion().trim().isEmpty()) {
            throw new IllegalArgumentException("La dirección es obligatoria");
        }
        if (inmueble.getPrecio() == null || inmueble.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }
        if (inmueble.getLocalidad() == null || inmueble.getLocalidad().trim().isEmpty()) {
            throw new IllegalArgumentException("La localidad es obligatoria");
        }
        return inmuebleRepository.save(inmueble);
    }

    @Override
    public Inmueble updateInmueble(Inmueble inmueble) {
        if (inmueble.getIdInmueble() == null || !inmuebleRepository.existsById(inmueble.getIdInmueble())) {
            throw new IllegalArgumentException("Inmueble no encontrado para actualizar");
        }

        Inmueble inmuebleUpdate = inmuebleRepository.findById(inmueble.getIdInmueble())
                .orElseThrow(() -> new IllegalArgumentException("Inmueble no encontrado"));

        // Solo actualizamos campos si vienen con datos válidos
        if (inmueble.getPrecio() != null && inmueble.getPrecio() > 0) {
            inmuebleUpdate.setPrecio(inmueble.getPrecio());
        }

        if (inmueble.getDireccion() != null && !inmueble.getDireccion().trim().isEmpty()) {
            inmuebleUpdate.setDireccion(inmueble.getDireccion());
        }

        if (inmueble.getLocalidad() != null && !inmueble.getLocalidad().trim().isEmpty()) {
            inmuebleUpdate.setLocalidad(inmueble.getLocalidad());
        }

        // Si se han subido nuevas fotos (la lista no es null ni vacía), las actualizamos
        if (inmueble.getPathFotos() != null && !inmueble.getPathFotos().isEmpty()) {
            // Ojo: Esto reemplaza las fotos anteriores. Si quisieras añadir, usarías addAll.
            // Para simplificar, asumimos reemplazo o gestión en el controller.
            inmuebleUpdate.setPathFotos(inmueble.getPathFotos());
        }

        return inmuebleRepository.save(inmuebleUpdate);
    }

    @Override
    public void deleteInmueble(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID no puede ser nulo");
        }
        if (!inmuebleRepository.existsById(id)) {
            throw new IllegalArgumentException("No se puede borrar: Inmueble no existe");
        }
        inmuebleRepository.deleteById(id);
    }
}