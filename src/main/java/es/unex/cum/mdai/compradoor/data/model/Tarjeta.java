package es.unex.cum.mdai.compradoor.data.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "tarjeta")
public class Tarjeta {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(unique = true)
    @NotBlank(message = "El código de la tarjeta es obligatoria")
    @Pattern(regexp = "^(?:(?:\\d{4}[- ]?){3}\\d{4}|\\d{16})$",
            message = "La tarjeta debe tener 16 dígitos (ej: 1234 5678 1234 5678) ")
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
