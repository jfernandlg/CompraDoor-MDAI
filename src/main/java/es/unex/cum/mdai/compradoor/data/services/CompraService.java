package es.unex.cum.mdai.compradoor.data.services;

import es.unex.cum.mdai.compradoor.data.model.Cliente;
import es.unex.cum.mdai.compradoor.data.model.Compra;
import es.unex.cum.mdai.compradoor.data.model.Inmueble;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CompraService {

    Optional<Compra> findCompraById(UUID id);

    List<Compra> findAllComprasByClienteId(UUID clienteId);

    List<Compra> findAllComprasByInmuebleId(UUID inmuebleId);

    List<Compra> findAllComprasByFecha(Date fechaInicio, Date fechaFin);

    List<Compra> findAllCompras();

    void realizarCompra(Cliente cliente, Inmueble inmueble);

    Compra saveCompra(Compra compra);

    void deleteCompra(Compra compra);
}

