package es.unex.cum.mdai.compradoor.data.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.ArrayList;
import java.util.List;

import java.util.UUID;

@Entity
@Table(name = "cliente")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "^\\d{8}[A-Z]$", message = "El DNI debe tener 8 nÚmeros y 1 letra mayúscula")
    private String dni;
    @NotBlank(message = "El nombre es obligatorio")
    @Pattern(regexp = "^[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s'-]+$",
            message = "El nombre contiene caracteres no válidos")
    private String nombre;
    @NotBlank(message = "El email es obligatorio")
    @Email(regexp = ".+@.+\\..+", message = "El formato del email no es válido")
    private String email;
    //    "^(?:(?:\\+|00)?34)?+[ . -]*+[6789](?:[ . -]*+[0-9]){8}$"
    @Pattern(regexp = "^\\+?[0-9\\s\\-]{9,15}$", message = "Formato inválido")
    private String telefono;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Venta> ventas = new ArrayList<>();

    @OneToMany(mappedBy = "cliente", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<Tarjeta> tarjetas = new ArrayList<>();

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Compra> compras = new ArrayList<>();

    private boolean admin = false;

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

    public String getTelefono() {
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

    public void setTelefono(String telefono) {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
