package es.unex.cum.mdai.compradoor.data.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;

import java.util.List;
import java.util.UUID;

@Entity
public class Inmueble {
    @OneToOne(mappedBy = "Inmueble", cascade = {CascadeType.ALL})
    private UUID idVenta;
    private UUID idInmueble;
    private String Localidad;
    private Float Precio;
    private String Direccion;
    private List<String> PathFotos;


    public void setUuid(UUID uuid) {
        this.idInmueble = uuid;
    }

    public void setLocalidad(String localidad) {
        Localidad = localidad;
    }

    public void setPrecio(Float precio) {
        Precio = precio;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public void setPathFotos(List<String> pathFotos) {
        PathFotos = pathFotos;
    }

    public String getLocalidad() {
        return Localidad;
    }

    public UUID getUuid() {
        return idInmueble;
    }

    public Float getPrecio() {
        return Precio;
    }

    public String getDireccion() {
        return Direccion;
    }

    public List<String> getPathFotos() {
        return PathFotos;
    }

    public Inmueble(UUID uuid, List<String> pathFotos, String localidad, Float precio, String direccion) {
        this.idInmueble = uuid;
        PathFotos = pathFotos;
        Localidad = localidad;
        Precio = precio;
        Direccion = direccion;
    }
}
