package es.unex.cum.mdai.compradoor.config;

import es.unex.cum.mdai.compradoor.data.model.Cliente;
import es.unex.cum.mdai.compradoor.data.model.Inmueble;
import es.unex.cum.mdai.compradoor.data.repository.ClienteRepository;
import es.unex.cum.mdai.compradoor.data.repository.InmuebleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDataInmueble(InmuebleRepository inmuebleRepository) {
        return args -> {
            if (inmuebleRepository.count() == 0) {

                Inmueble inmueble1 = new Inmueble();
                inmueble1.setDireccion("Calle Colón, 15");
                inmueble1.setLocalidad("Cáceres");
                inmueble1.setPrecio(400_000f);

                inmueble1.setPathFotos(Arrays.asList(
                        "/images/inmueble1/minh-pham-1_B4Zzh7UpQ-unsplash.jpg",
                        "/images/inmueble1/minh-pham-7pCFUybP_P8-unsplash.jpg",
                        "/images/inmueble1/minh-pham-OtXADkUh3-I-unsplash.jpg",
                        "/images/inmueble1/minh-pham-Y6HuHkhrKYU-unsplash.jpg",
                        "/images/inmueble1/naomi-hebert-MP0bgaS_d1c-unsplash.jpg",
                        "/images/inmueble1/scott-webb-1ddol8rgUH8-unsplash.jpg"
                ));
                inmuebleRepository.save(inmueble1);

                Inmueble inmueble2 = new Inmueble();
                inmueble2.setDireccion("Av. Juan Carlos I, 30");
                inmueble2.setLocalidad("Mérida");
                inmueble2.setPrecio(650_000f);
                inmueble2.setPathFotos(Arrays.asList(
                        "/images/inmueble2/pexels-fotoaibe-1643389.jpg",
                        "/images/inmueble2/pexels-fotoaibe-1647227.jpg",
                        "/images/inmueble2/pexels-fotoaibe-1647233.jpg",
                        "/images/inmueble2/pexels-fotoaibe-1647238.jpg",
                        "/images/inmueble2/pexels-fotoaibe-1666878.jpg",
                        "/images/inmueble2/pexels-fotoaibe-1668860.jpg",
                        "/images/inmueble2/pexels-fotoaibe-1669799.jpg",
                        "/images/inmueble2/pexels-fotoaibe-1743226.jpg",
                        "/images/inmueble2/pexels-fotoaibe-1743227.jpg",
                        "/images/inmueble2/pexels-fotoaibe-1743231.jpg",
                        "/images/inmueble2/pexels-fotoaibe-3741313.jpg",
                        "/images/inmueble2/pexels-fotoaibe-3741316.jpg",
                        "/images/inmueble2/pexels-fotoaibe-3741317.jpg"
                ));
                inmuebleRepository.save(inmueble2);

                Inmueble inmueble3 = new Inmueble();
                inmueble3.setDireccion("Plaza Mayor s/n");
                inmueble3.setLocalidad("Badajoz");
                inmueble3.setPrecio(300_000f);
                inmueble3.setPathFotos(Arrays.asList(
                        "/images/inmueble3/pexels-cara-denison-886614634-34958535.jpg",
                        "/images/inmueble3/pexels-curtis-adams-1694007-15580487.jpg",
                        "/images/inmueble3/pexels-curtis-adams-1694007-15580488.jpg",
                        "/images/inmueble3/pexels-curtis-adams-1694007-15580489.jpg",
                        "/images/inmueble3/pexels-curtis-adams-1694007-15667605.jpg",
                        "/images/inmueble3/pexels-curtis-adams-1694007-15668077.jpg",
                        "/images/inmueble3/pexels-curtis-adams-1694007-15668078.jpg"
                ));

                inmuebleRepository.save(inmueble3);

                Inmueble inmueble4 = new Inmueble();
                inmueble4.setDireccion("Carretera Madrid, 8");
                inmueble4.setLocalidad("Badajoz");
                inmueble4.setPrecio(380_000f);
                inmueble4.setPathFotos(Arrays.asList(
                        "/images/inmueble4/pexels-andreaedavis-10593591.jpg",
                        "/images/inmueble4/pexels-andreaedavis-10820241.jpg",
                        "/images/inmueble4/pexels-andreaedavis-10854290.jpg",
                        "/images/inmueble4/pexels-andreaedavis-11899060.jpg",
                        "/images/inmueble4/pexels-andreaedavis-12316662.jpg",
                        "/images/inmueble4/pexels-andreaedavis-12639981.jpg",
                        "/images/inmueble4/pexels-andreaedavis-13743557.jpg",
                        "/images/inmueble4/pexels-andreaedavis-14007199.jpg",
                        "/images/inmueble4/pexels-andreaedavis-14369630.jpg",
                        "/images/inmueble4/pexels-andreaedavis-14369631.jpg",
                        "/images/inmueble4/pexels-andreaedavis-17965206.jpg"
                ));

                inmuebleRepository.save(inmueble4);

                Inmueble inmueble5 = new Inmueble();
                inmueble5.setDireccion("Calle del Conde de Peñalver, 90, 28006 Madrid");
                inmueble5.setLocalidad("Salamanca");
                inmueble5.setPrecio(650_000f);
                inmueble5.setPathFotos(Arrays.asList(
                        "/images/inmueble5/pexels-curtis-adams-1694007-14495871.jpg",
                        "/images/inmueble5/pexels-curtis-adams-1694007-14495877.jpg",
                        "/images/inmueble5/pexels-curtis-adams-1694007-14495890.jpg",
                        "/images/inmueble5/pexels-curtis-adams-1694007-14495936.jpg",
                        "/images/inmueble5/pexels-curtis-adams-1694007-14495968.jpg"
                ));

                inmuebleRepository.save(inmueble5);

                Inmueble inmueble6 = new Inmueble();
                inmueble6.setDireccion("Calle de la Torrecilla del Leal, 12, 28012 Madrid");
                inmueble6.setLocalidad("Centro");
                inmueble6.setPrecio(700_000f);
                inmueble6.setPathFotos(Arrays.asList(
                        "/images/inmueble6/pexels-expect-best-79873-323780.jpg",
                        "/images/inmueble6/francesca-tosolini-4TlrOY2IyUA-unsplash.jpg",
                        "/images/inmueble6/francesca-tosolini-DmOhItSo49k-unsplash.jpg",
                        "/images/inmueble6/francesca-tosolini-Gh_UjjYoVwk-unsplash.jpg",
                        "/images/inmueble6/francesca-tosolini-GyCD3-QbBdw-unsplash (1).jpg",
                        "/images/inmueble6/francesca-tosolini-lLDh9JppH2c-unsplash.jpg",
                        "/images/inmueble6/francesca-tosolini-XyGvEj587Mc-unsplash.jpg"

                ));

                inmuebleRepository.save(inmueble6);

                Inmueble inmueble7 = new Inmueble();
                inmueble7.setDireccion("Carrer de Montcada, 15-23, 08003 Barcelona");
                inmueble7.setLocalidad("Ciutat Vella");
                inmueble7.setPrecio(250_000f);
                inmueble7.setPathFotos(Arrays.asList(
                        "/images/inmueble7/pexels-binyaminmellish-1396122.jpg",
                        "/images/inmueble7/francesca-tosolini-87mjpwLYXBw-unsplash.jpg",
                        "/images/inmueble7/francesca-tosolini-I5uy6tQL4VM-unsplash.jpg",
                        "/images/inmueble7/francesca-tosolini-kDtuOUIkwrk-unsplash.jpg",
                        "/images/inmueble7/francesca-tosolini-PKBn5iy1MMA-unsplash.jpg"
                ));

                inmuebleRepository.save(inmueble7);


                System.out.println("---------------------------------------------");
                System.out.println(" CARGA DE DATOS INICIALES COMPLETADA ");
                System.out.println("---------------------------------------------");

            }
        };
    }

    @Bean
    CommandLineRunner initDataCliente(ClienteRepository clienteRepository) {
        return (args) -> {
            if (clienteRepository.count() == 0) {
                Cliente admin = new Cliente();
                admin.setNombre("Admin");
                admin.setDni("00000000A");
                admin.setEmail("admin@compradoor.com");
                admin.setPassword("admin");
                admin.setAdmin(true);
                clienteRepository.save(admin);
            }

        };
    }
}
