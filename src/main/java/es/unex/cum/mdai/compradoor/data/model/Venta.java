package es.unex.cum.mdai.compradoor.data.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "venta")
public class Venta {
    @Id
    private UUID idVenta;

    public void setId(UUID id) {
        this.idVenta = id;
    }

    public UUID getId() {
        return idVenta;
    }

    @OneToOne(mappedBy = "venta",cascade = CascadeType.ALL)
    private UUID idInmueble;

}
