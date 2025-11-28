package es.unex.cum.mdai.compradoor.data.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "servicios")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idServicio;

    @Enumerated(EnumType.STRING)
    private TipoServicio tipoServicio;

    private String descripcion;
    private float coste;

    @Temporal(TemporalType.DATE)
    private Date fechaAplicacion;

    // --- CAMBIO PRINCIPAL: Ahora apunta a COMPRA ---
    @ManyToOne
    @JoinColumn(name = "compra_id")
    private Compra compra;

    public Servicio() {}

    // Getters y Setters
    public UUID getIdServicio() { return idServicio; }
    public void setIdServicio(UUID idServicio) { this.idServicio = idServicio; }

    public TipoServicio getTipoServicio() { return tipoServicio; }
    public void setTipoServicio(TipoServicio tipoServicio) { this.tipoServicio = tipoServicio; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public float getCoste() { return coste; }
    public void setCoste(float coste) { this.coste = coste; }

    public Date getFechaAplicacion() { return fechaAplicacion; }
    public void setFechaAplicacion(Date fechaAplicacion) { this.fechaAplicacion = fechaAplicacion; }

    // Getter y Setter para Compra
    public Compra getCompra() { return compra; }
    public void setCompra(Compra compra) { this.compra = compra; }
}
