package es.unex.cum.mdai.compradoor.data.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "servicios")
public class Servicios {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID idServicio;

    private TipoServicio tipoServicio;
    private String descripcion;


    @ManyToOne
    @JoinColumn(name = "servicios_aplicado_id")
    private ServiciosAplicados serviciosAplicado;

    public ServiciosAplicados getServiciosAplicado() {
        return serviciosAplicado;
    }

    public void setServiciosAplicado(ServiciosAplicados serviciosAplicado) {
        this.serviciosAplicado = serviciosAplicado;
    }

    public void setId(UUID id) {
        this.idServicio = id;
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

}
