package es.unex.cum.mdai.compradoor.data.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "inmueble")
public class Inmueble {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idInmueble;

    @OneToOne(mappedBy = "inmueble")
    private Venta venta;

    private String localidad;
    private Float precio;
    @Size(min = 5, max = 50, message = "La dirección debe tener entre 5 y 50 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9ñÑáéíóúÁÉÍÓÚüÜ][a-zA-Z0-9ñÑáéíóúÁÉÍÓÚüÜ\\s,./ºª\\-()]*+$",
            message = "La dirección contiene caracteres no válidos")
    private String direccion;

    //    @ElementCollection
//    @CollectionTable(name = "inmueble_fotos", joinColumns = @JoinColumn(name = "inmueble_id"))
//    @Column(name = "foto_path")
    private List<String> pathFotos;

    @OneToMany(mappedBy = "inmueble")
    private List<Compra> compras = new ArrayList<>();

    public Inmueble() {

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

    public List<Compra> getCompras() {
        return compras;
    }

    public void setCompras(List<Compra> compras) {
        this.compras = compras;
    }

    public Inmueble(UUID uuid, List<String> pathFotos, String localidad, Float precio, String direccion) {
        this.idInmueble = uuid;
        this.pathFotos = pathFotos;
        this.localidad = localidad;
        this.precio = precio;
        this.direccion = direccion;
    }

    public Inmueble(Venta venta, String localidad, Float precio, String direccion) {
        this.venta = venta;
        this.localidad = localidad;
        this.precio = precio;
        this.direccion = direccion;
    }

    public Inmueble(String localidad, Float precio, String direccion) {
        this.localidad = localidad;
        this.precio = precio;
        this.direccion = direccion;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Inmueble inmueble = (Inmueble) o;
        return Objects.equals(idInmueble, inmueble.idInmueble) && Objects.equals(venta, inmueble.venta) && Objects.equals(localidad, inmueble.localidad) && Objects.equals(precio, inmueble.precio) && Objects.equals(direccion, inmueble.direccion) && Objects.equals(pathFotos, inmueble.pathFotos) && Objects.equals(compras, inmueble.compras);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idInmueble, venta, localidad, precio, direccion, pathFotos, compras);
    }

    @Override
    public String toString() {
        return "Inmueble{" +
                "idInmueble=" + idInmueble +
                ", venta=" + venta +
                ", localidad='" + localidad + '\'' +
                ", precio=" + precio +
                ", direccion='" + direccion + '\'' +
                ", pathFotos=" + pathFotos +
                ", compras=" + compras +
                '}';
    }
}
