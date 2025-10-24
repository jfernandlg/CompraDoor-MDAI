package es.unex.cum.mdai.compradoor.data.model;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "servicios")
public class Servicios {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idServicio;

    private TipoServicio tipoServicio;
    private String descripcion;


    @ManyToOne
    @JoinColumn(name = "servicios_aplicados_id")
    private ServiciosAplicados serviciosAplicados;

    public ServiciosAplicados getServiciosAplicado() {
        return serviciosAplicados;
    }

    public void setServiciosAplicado(ServiciosAplicados serviciosAplicados) {
        this.serviciosAplicados = serviciosAplicados;
    }

    public UUID getIdServicio() {
        return idServicio;
    }

    public TipoServicio getTipoServicio() {
        return tipoServicio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setIdServicio(UUID idServicio) {
        this.idServicio = idServicio;
    }

    public void setTipoServicio(TipoServicio tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Servicios servicios = (Servicios) o;
        return Objects.equals(idServicio, servicios.idServicio) && tipoServicio == servicios.tipoServicio && Objects.equals(descripcion, servicios.descripcion) && Objects.equals(serviciosAplicados, servicios.serviciosAplicados);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idServicio, tipoServicio, descripcion, serviciosAplicados);
    }

    @Override
    public String toString() {
        return "Servicios{" +
                "idServicio=" + idServicio +
                ", tipoServicio=" + tipoServicio +
                ", descripcion='" + descripcion + '\'' +
                ", serviciosAplicados=" + serviciosAplicados +
                '}';
    }
}
