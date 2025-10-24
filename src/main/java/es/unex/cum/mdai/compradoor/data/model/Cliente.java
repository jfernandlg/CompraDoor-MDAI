package es.unex.cum.mdai.compradoor.data.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "cliente")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String dni;
    private String nombre;
    private String email;
    private int telefono;

    @OneToMany(mappedBy = "cliente")
    private List<Venta> ventas =  new ArrayList<>();

    @OneToMany(mappedBy = "cliente", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<Tarjeta> tarjetas = new ArrayList<>();

    @OneToMany(mappedBy = "cliente", cascade = {CascadeType.ALL})
    private List<Compra> compras = new ArrayList<>();

    public Cliente(String dni, String nombre) {
        this.dni = dni;
        this.nombre = nombre;
        this.compras = new ArrayList<>();
        this.tarjetas = new ArrayList<>();
        this.ventas = new ArrayList<>();
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

    public List<Compra> getCompras() {
        return compras;
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

    public List<Venta> getVentas() {
        return ventas;
    }

    public void setVentas(List<Venta> ventas) {
        this.ventas = ventas;
    }

    public void setCompras(List<Compra> compras) {
        this.compras = compras;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return telefono == cliente.telefono && Objects.equals(id, cliente.id) && Objects.equals(dni, cliente.dni) && Objects.equals(nombre, cliente.nombre) && Objects.equals(email, cliente.email) && Objects.equals(ventas, cliente.ventas) && Objects.equals(tarjetas, cliente.tarjetas) && Objects.equals(compras, cliente.compras);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dni, nombre, email, telefono, ventas, tarjetas, compras);
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", dni='" + dni + '\'' +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", telefono=" + telefono +
                ", ventas=" + ventas +
                ", tarjetas=" + tarjetas +
                ", compras=" + compras +
                '}';
    }
}
