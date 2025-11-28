package es.unex.cum.mdai.compradoor.data.model;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "compra")
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idCompra;

    public Compra() {}

    public Compra(Cliente cliente, float precioCompra, Inmueble inmueble) {
        this.cliente = cliente;
        this.precioCompra = precioCompra;
        this.inmueble = inmueble;
        this.fechaCompra = new Date();
    }

    @ManyToOne
    @JoinColumn(name = "inmueble_id")
    private Inmueble inmueble;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Temporal(TemporalType.DATE)
    private Date fechaCompra;

    private float precioCompra;

    // --- NUEVO: Relación inversa con Servicios ---
    // mappedBy = "compra" porque en Servicio.java el campo se llama 'compra'
    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Servicio> servicios = new ArrayList<>();

    // --- GETTERS Y SETTERS ---

    public List<Servicio> getServicios() {
        return servicios;
    }

    public void setServicios(List<Servicio> servicios) {
        this.servicios = servicios;
    }

    // Helper para añadir servicios fácilmente y mantener la coherencia
    public void addServicio(Servicio servicio) {
        servicios.add(servicio);
        servicio.setCompra(this);
    }

    public Inmueble getInmueble() { return inmueble; }
    public void setInmueble(Inmueble inmueble) { this.inmueble = inmueble; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public UUID getIdCompra() { return idCompra; }
    public void setIdCompra(UUID idCompra) { this.idCompra = idCompra; }

    public Date getFechaCompra() { return fechaCompra; }
    public void setFechaCompra(Date fechaCompra) { this.fechaCompra = fechaCompra; }

    public float getPrecioCompra() { return precioCompra; }
    public void setPrecioCompra(float precioCompra) { this.precioCompra = precioCompra; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Compra compra = (Compra) o;
        return Objects.equals(idCompra, compra.idCompra);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idCompra);
    }
}