package es.unex.cum.mdai.compradoor.data.repository;

import es.unex.cum.mdai.compradoor.data.model.Cliente;
import es.unex.cum.mdai.compradoor.data.model.Inmueble;
import es.unex.cum.mdai.compradoor.data.model.Venta;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import javax.swing.*;
import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class InmuebleRepositoryTest {

    @Autowired
    private InmuebleRepository inmuebleRepository;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    void testFindByLocalidad() {
        Inmueble i = new Inmueble("Badajoz", 250_000f, "Avenida de América, 25");
        Inmueble i2 = new Inmueble("Cáceres", 470_000f, "Avenida de la Hispanidad, 35");
        Inmueble i3 = new Inmueble("Badajoz", 170_000f, "Avenida de España, 10");
        Inmueble i4 = new Inmueble("Badajoz", 545_000f, "Paseo Fluvial, 16");
        inmuebleRepository.save(i);
        inmuebleRepository.save(i2);
        inmuebleRepository.save(i3);
        inmuebleRepository.save(i4);

        List<Inmueble> findByLocalidad = inmuebleRepository.findByLocalidadIgnoreCase("Badajoz");
        assertThat(findByLocalidad).hasSize(3);
    }

    @Test
    void testPriceBetween() {
        Inmueble i = new Inmueble("Badajoz", 250_000f, "Avenida de América, 25");
        Inmueble i2 = new Inmueble("Cáceres", 470_000f, "Avenida de la Hispanidad, 35");
        Inmueble i3 = new Inmueble("Badajoz", 170_000f, "Avenida de España, 10");
        Inmueble i4 = new Inmueble("Badajoz", 545_000f, "Paseo Fluvial, 16");
        inmuebleRepository.save(i);
        inmuebleRepository.save(i2);
        inmuebleRepository.save(i3);
        inmuebleRepository.save(i4);

        List<Inmueble> ByPriceBetween = inmuebleRepository.findByPrecioBetween(100_000f, 500_000f);
        assertThat(ByPriceBetween).hasSize(3);
    }

    @Test
    void testVentaIsNull(){
        Cliente c = new Cliente("12345678A", "Juan Perez");
        c.setEmail("juan@example.com");
        clienteRepository.save(c);

        Inmueble i = new Inmueble("Badajoz", 250_000f, "Avenida de América, 25");
        Inmueble i2 = new Inmueble("Cáceres", 470_000f, "Avenida de la Hispanidad, 35");
        Inmueble i3 = new Inmueble("Mérida", 340_000f, "Avenida Reina Sofía, 12");
        inmuebleRepository.save(i);
        inmuebleRepository.save(i2);
        inmuebleRepository.save(i3);

        Venta v = new Venta(i, 270_000f, c);
        ventaRepository.save(v);

        List<Inmueble> byVenta = inmuebleRepository.findByVentaIsNull();
        assertThat(byVenta).hasSize(2);
        assertThat(byVenta).extracting("idInmueble").containsExactlyInAnyOrder(i2.getIdInmueble(), i3.getIdInmueble());








    }

}
