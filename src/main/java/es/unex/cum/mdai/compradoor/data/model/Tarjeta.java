package es.unex.cum.mdai.compradoor.data.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "tarjeta")
public class Tarjeta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;
    private String codigoTarjeta;
    private boolean valida;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente")
    Cliente cliente;

    public Tarjeta() {

    }


    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public String getCodigoTarjeta() {
        return codigoTarjeta;
    }

    public boolean isValida() {
        return valida;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCodigoTarjeta(String codigoTarjeta) {
        this.codigoTarjeta = codigoTarjeta;
    }

    public void setValida(boolean valida) {
        this.valida = valida;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Tarjeta(String codigoTarjeta, boolean valida, Cliente cliente) {
        this.codigoTarjeta = codigoTarjeta;
        this.valida = valida;
        this.cliente = cliente;
    }
}
