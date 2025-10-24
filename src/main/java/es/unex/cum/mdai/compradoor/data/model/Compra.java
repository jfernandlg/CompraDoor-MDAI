package es.unex.cum.mdai.compradoor.data.model;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "compra")
public class Compra {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idCompra;

    public Compra() {

    }

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

    private Date fechaCompra;
    private float precioCompra;

    public Inmueble getInmueble() {
        return inmueble;
    }

    public void setInmueble(Inmueble inmueble) {
        this.inmueble = inmueble;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public UUID getIdCompra() {
        return idCompra;
    }

    public Date getFechaCompra() {
        return fechaCompra;
    }

    public float getPrecioCompra() {
        return precioCompra;
    }

    public void setIdCompra(UUID idCompra) {
        this.idCompra = idCompra;
    }

    public void setFechaCompra(Date fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public void setPrecioCompra(float precioCompra) {
        this.precioCompra = precioCompra;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Compra compra = (Compra) o;
        return Float.compare(precioCompra, compra.precioCompra) == 0 && Objects.equals(idCompra, compra.idCompra) && Objects.equals(inmueble, compra.inmueble) && Objects.equals(cliente, compra.cliente) && Objects.equals(fechaCompra, compra.fechaCompra);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCompra, inmueble, cliente, fechaCompra, precioCompra);
    }

    @Override
    public String toString() {
        return "Compra{" +
                "idCompra=" + idCompra +
                ", inmueble=" + inmueble +
                ", cliente=" + cliente +
                ", fechaCompra=" + fechaCompra +
                ", precioCompra=" + precioCompra +
                '}';
    }
}
