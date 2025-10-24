package es.unex.cum.mdai.compradoor;

import es.unex.cum.mdai.compradoor.data.model.Cliente;
import es.unex.cum.mdai.compradoor.data.model.Tarjeta;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    @Test
    void clienteUUIDAutomatico() {
        Cliente c = new Cliente();
        assertNull(c.getId(), "El id no se asigna automaticamente");
        Cliente c1 = new Cliente();
        assertNull(c1.getId(), "El id no se asigna automaticamente");
        Cliente c2 = new Cliente();
        assertNull(c2.getId(), "El id no se asigna automaticamente");
    }
    @Test
    void tarjetaUUIDAutomatico() {
        Tarjeta t = new Tarjeta();
        assertNull(t.getId(), "El id no se asigna automaticamente");
        Tarjeta t1 = new Tarjeta();
        assertNull(t1.getId(), "El id no se asigna automaticamente");
        Tarjeta t2 = new Tarjeta();
        assertNull(t2.getId(), "El id no se asigna automaticamente");
    }

}
