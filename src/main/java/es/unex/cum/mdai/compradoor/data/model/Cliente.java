package es.unex.cum.mdai.compradoor.data.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "cliente")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;
    private String dni;
    private String nombre;
    private String email;
    private int telefono;

    @OneToMany(mappedBy = "cliente", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<Tarjeta> tarjetas = new ArrayList<Tarjeta>();

    public Cliente(String dni, String nombre) {
        this.dni = dni;
        this.nombre = nombre;
    }

    public Cliente() {

    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public String getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public int getTelefono() {
        return telefono;
    }

    public List<Tarjeta> getTarjetas() {
        return tarjetas;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public void setTarjetas(List<Tarjeta> tarjetas) {
        this.tarjetas = tarjetas;
    }


}
