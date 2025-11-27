package es.unex.cum.mdai.compradoor.data.services;

import es.unex.cum.mdai.compradoor.data.model.Cliente;
import es.unex.cum.mdai.compradoor.data.model.Compra;
import es.unex.cum.mdai.compradoor.data.model.Inmueble;

import java.util.Date;
import java.util.List;

public interface CompraService {

    List<Compra> findAllComprasByCliente(Cliente cliente);

    List<Compra> findAllComprasByInmueble(Inmueble inmueble);

    List<Compra> findAllComprasByFecha(Date fechaInicio, Date fechaFin);

    List<Compra> findAllCompras();

    Compra saveCompra(Compra compra);

    void deleteCompra(Compra compra);
}

