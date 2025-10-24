package es.unex.cum.mdai.compradoor.data.model;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "servicios_aplicados")
public class ServiciosAplicados {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idServicioAplicado;

    @ManyToOne
    @JoinColumn(name = "venta_id")
    private Venta venta;

    private float coste;
    private Date fechaAplicacion;

    @OneToMany(mappedBy = "serviciosAplicados", cascade = {CascadeType.ALL})
    private List<Servicios> servicios = new ArrayList<>();

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public UUID getIdServicioAplicado() {
        return idServicioAplicado;
    }

    public float getCoste() {
        return coste;
    }

    public Date getFechaAplicacion() {
        return fechaAplicacion;
    }

    public List<Servicios> getServicios() {
        return servicios;
    }

    public void setIdServicioAplicado(UUID idServicioAplicado) {
        this.idServicioAplicado = idServicioAplicado;
    }

    public void setCoste(float coste) {
        this.coste = coste;
    }

    public void setFechaAplicacion(Date fechaAplicacion) {
        this.fechaAplicacion = fechaAplicacion;
    }

    public void setServicios(List<Servicios> servicios) {
        this.servicios = servicios;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ServiciosAplicados that = (ServiciosAplicados) o;
        return Float.compare(coste, that.coste) == 0 && Objects.equals(idServicioAplicado, that.idServicioAplicado) && Objects.equals(venta, that.venta) && Objects.equals(fechaAplicacion, that.fechaAplicacion) && Objects.equals(servicios, that.servicios);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idServicioAplicado, venta, coste, fechaAplicacion, servicios);
    }

    @Override
    public String toString() {
        return "ServiciosAplicados{" +
                "idServicioAplicado=" + idServicioAplicado +
                ", venta=" + venta +
                ", coste=" + coste +
                ", fechaAplicacion=" + fechaAplicacion +
                ", servicios=" + servicios +
                '}';
    }
}
