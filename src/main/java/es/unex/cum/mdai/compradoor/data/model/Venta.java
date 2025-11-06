package es.unex.cum.mdai.compradoor.data.model;

import jakarta.persistence.*;

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

    private Date fechaVenta;
    private float precioVenta;

    @OneToMany(mappedBy = "venta")
    private List<Servicio> servicios = new ArrayList<>();


    @ManyToOne
    @JoinColumn(name = "idCliente")
    private Cliente cliente;

    public Venta(Inmueble inmueble, float precioVenta, Cliente cliente) {
        this.inmueble = inmueble;
        this.fechaVenta = new Date();
        this.precioVenta = precioVenta;
        this.cliente = cliente;
    }

    public Venta() {

    }

    public List<Servicios> getServicios() {
        return servicios;
    }

    public void setServicios(List<Servicios> servicios) {
        this.servicios = servicios;
    }

    public void setId(UUID id) {
        this.idVenta = id;
    }

    public UUID getIdVenta() {
        return idVenta;
    }

    public Inmueble getInmueble() {
        return inmueble;
    }

    public Date getFechaVenta() {
        return fechaVenta;
    }

    public float getPrecioVenta() {
        return precioVenta;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setIdVenta(UUID idVenta) {
        this.idVenta = idVenta;
    }

    public void setInmueble(Inmueble inmueble) {
        this.inmueble = inmueble;
    }

    public void setFechaVenta(Date fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public void setPrecioVenta(float precioVenta) {
        this.precioVenta = precioVenta;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Venta venta = (Venta) o;
        return Float.compare(precioVenta, venta.precioVenta) == 0 && Objects.equals(idVenta, venta.idVenta) && Objects.equals(inmueble, venta.inmueble) && Objects.equals(fechaVenta, venta.fechaVenta) && Objects.equals(servicios, venta.servicios) && Objects.equals(cliente, venta.cliente);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idVenta, inmueble, fechaVenta, precioVenta, servicios, cliente);
    }

    @Override
    public String toString() {
        return "Venta{" +
                "idVenta=" + idVenta +
                ", inmueble=" + inmueble +
                ", fechaVenta=" + fechaVenta +
                ", precioVenta=" + precioVenta +
                ", cliente=" + cliente +
                '}';
    }
}
