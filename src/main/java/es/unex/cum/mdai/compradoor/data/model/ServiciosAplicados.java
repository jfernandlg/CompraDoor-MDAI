package es.unex.cum.mdai.compradoor.data.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "servicios_aplicados")
public class ServiciosAplicados {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID idServicioAplicado;

    @ManyToOne
    @JoinColumn(name = "venta_id")
    private Venta venta;

    private float coste;
    private Date fechaAplicacion;

    @OneToMany(mappedBy = "servicios_aplicados", cascade = {CascadeType.ALL})
    private List<Servicios> servicios = new ArrayList<>();

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public void setId(UUID id) {
        this.idServicioAplicado = id;
    }

    public UUID getId() {
        return idServicioAplicado;
    }


}
