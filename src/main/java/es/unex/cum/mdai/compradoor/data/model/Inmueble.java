package es.unex.cum.mdai.compradoor.data.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "inmueble")
public class Inmueble {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID idInmueble;
<<<<<<< HEAD
    private String Localidad;
    private Float Precio;
    private String Direccion;
    private List<String> PathFotos;
    
=======

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_venta")
    Venta venta;
    private String localidad;
    private Float precio;
    private String direccion;
    private List<String> pathFotos;

    public Inmueble() {
>>>>>>> origin/main

    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setPathFotos(List<String> pathFotos) {
        this.pathFotos = pathFotos;
    }

    public String getLocalidad() {
        return localidad;
    }

    public Float getPrecio() {
        return precio;
    }

    public String getDireccion() {
        return direccion;
    }

    public List<String> getPathFotos() {
        return pathFotos;
    }

    public UUID getIdInmueble() {
        return idInmueble;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setIdInmueble(UUID idInmueble) {
        this.idInmueble = idInmueble;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public Inmueble(UUID uuid, List<String> pathFotos, String localidad, Float precio, String direccion) {
        this.idInmueble = uuid;
        this.pathFotos = pathFotos;
        this.localidad = localidad;
        this.precio = precio;
        this.direccion = direccion;
    }
}
