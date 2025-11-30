package es.unex.cum.mdai.compradoor.data.model;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.*;

@Entity
@Table(name = "venta")
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idVenta;

    @OneToOne
    @JoinColumn(name = "idInmueble")
    private Inmueble inmueble;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaVenta;
    private float precioVenta;

    @ManyToOne
    @JoinColumn(name = "idCliente")
    private Cliente cliente;

    @OneToMany(mappedBy = "venta")
    private List<Servicio> servicios = new ArrayList<>();

    public Venta() {}

    public Venta(Inmueble inmueble, float precioVenta, Cliente cliente) {
        this.inmueble = inmueble;
        this.fechaVenta = new Date();
        this.precioVenta = precioVenta;
        this.cliente = cliente;
    }

    // --- GETTERS Y SETTERS ---
    public UUID getIdVenta() { return idVenta; }
    public void setIdVenta(UUID idVenta) { this.idVenta = idVenta; }
    public Inmueble getInmueble() { return inmueble; }
    public void setInmueble(Inmueble inmueble) { this.inmueble = inmueble; }
    public Date getFechaVenta() { return fechaVenta; }
    public void setFechaVenta(Date fechaVenta) { this.fechaVenta = fechaVenta; }
    public float getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(float precioVenta) { this.precioVenta = precioVenta; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public List<Servicio> getServicios() {
        return servicios;
    }

    public void setServicios(List<Servicio> servicios) {
        this.servicios = servicios;
    }

    // --- CORRECCIÓN CRÍTICA: ROMPER EL BUCLE INFINITO ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Venta venta = (Venta) o;
        return Objects.equals(idVenta, venta.idVenta); // Solo ID
    }

    @Override
    public int hashCode() {
        return Objects.hash(idVenta); // Solo ID
    }

    @Override
    public String toString() {
        // NO INCLUIMOS 'inmueble' NI 'cliente'
        return "Venta{" +
                "idVenta=" + idVenta +
                ", fechaVenta=" + fechaVenta +
                ", precioVenta=" + precioVenta +
                '}';
    }
}