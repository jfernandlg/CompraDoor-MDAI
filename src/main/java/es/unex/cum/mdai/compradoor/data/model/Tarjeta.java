package es.unex.cum.mdai.compradoor.data.model;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "tarjeta")
public class Tarjeta {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(unique = true)
    private String codigoTarjeta;
    private boolean valida;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Tarjeta tarjeta = (Tarjeta) o;
        return valida == tarjeta.valida && Objects.equals(id, tarjeta.id) && Objects.equals(codigoTarjeta, tarjeta.codigoTarjeta) && Objects.equals(cliente, tarjeta.cliente);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, codigoTarjeta, valida, cliente);
    }

    @Override
    public String toString() {
        return "Tarjeta{" +
                "id=" + id +
                ", codigoTarjeta='" + codigoTarjeta + '\'' +
                ", valida=" + valida +
                ", cliente=" + cliente +
                '}';
    }
}
