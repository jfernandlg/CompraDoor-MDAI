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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "inmueble_fotos",
            joinColumns = @JoinColumn(name = "inmueble_id"))
    @Column(name = "url_foto")
    private List<String> pathFotos;

    @OneToMany(mappedBy = "inmueble")
    private List<Compra> compras = new ArrayList<>();

    public Inmueble() {}

    public Inmueble(String localidad, Float precio, String direccion) {
        this.localidad = localidad;
        this.precio = precio;
        this.direccion = direccion;
    }

    // --- GETTERS Y SETTERS ---
    public UUID getIdInmueble() { return idInmueble; }
    public void setIdInmueble(UUID idInmueble) { this.idInmueble = idInmueble; }
    public Venta getVenta() { return venta; }
    public void setVenta(Venta venta) { this.venta = venta; }
    public String getLocalidad() { return localidad; }
    public void setLocalidad(String localidad) { this.localidad = localidad; }
    public Float getPrecio() { return precio; }
    public void setPrecio(Float precio) { this.precio = precio; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public List<String> getPathFotos() { return pathFotos; }
    public void setPathFotos(List<String> pathFotos) { this.pathFotos = pathFotos; }
    public List<Compra> getCompras() { return compras; }
    public void setCompras(List<Compra> compras) { this.compras = compras; }

    // --- CORRECCIÓN CRÍTICA: ROMPER EL BUCLE INFINITO ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Inmueble inmueble = (Inmueble) o;
        return Objects.equals(idInmueble, inmueble.idInmueble); // Solo comparamos ID
    }

    @Override
    public int hashCode() {
        return Objects.hash(idInmueble); // Solo ID
    }

    @Override
    public String toString() {
        // NO INCLUIMOS 'venta' NI 'compras'
        return "Inmueble{" +
                "idInmueble=" + idInmueble +
                ", localidad='" + localidad + '\'' +
                ", precio=" + precio +
                ", direccion='" + direccion + '\'' +
                '}';
    }
}