package es.unex.cum.mdai.compradoor.data.model;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "compra")
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idCompra;

    @ManyToOne
    @JoinColumn(name = "inmueble_id")
    private Inmueble inmueble;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Temporal(TemporalType.DATE)
    private Date fechaCompra;

    private float precioCompra;

    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Servicio> servicios = new ArrayList<>();

    public Compra() {}

    public Compra(Cliente cliente, float precioCompra, Inmueble inmueble) {
        this.cliente = cliente;
        this.precioCompra = precioCompra;
        this.inmueble = inmueble;
        this.fechaCompra = new Date();
    }

    // --- GETTERS Y SETTERS ---
    public List<Servicio> getServicios() { return servicios; }
    public void setServicios(List<Servicio> servicios) { this.servicios = servicios; }
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

    // --- CORRECCIÓN CRÍTICA ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Compra compra = (Compra) o;
        return Objects.equals(idCompra, compra.idCompra); // Solo ID
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCompra); // Solo ID
    }

    @Override
    public String toString() {
        // Limpiamos referencias circulares
        return "Compra{" +
                "idCompra=" + idCompra +
                ", fechaCompra=" + fechaCompra +
                ", precioCompra=" + precioCompra +
                '}';
    }
}