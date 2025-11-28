package es.unex.cum.mdai.compradoor.data.services;

import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class StorageService {

    private final Path rootLocation = Paths.get("src/main/resources/static/images");

    public StorageService() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("No se puede inicializar la carpeta de almacenamiento", e);
        }
    }

    public String generarNombreNuevaCarpeta() {
        try (Stream<Path> walk = Files.list(rootLocation)) {

            int maxId = walk.filter(Files::isDirectory)
                    .map(path -> path.getFileName().toString())
                    .filter(name -> name.startsWith("inmueble"))
                    .mapToInt(name -> {
                        try {
                            String numero = name.substring(8);
                            return Integer.parseInt(numero);
                        } catch (NumberFormatException | IndexOutOfBoundsException e) {
                            return 0;
                        }
                    })
                    .max()
                    .orElse(0);

            return "inmueble" + (maxId + 1);

        } catch (IOException e) {
            throw new RuntimeException("Fallo al leer carpetas existentes en images", e);
        }
    }

    public String store(MultipartFile file, String subCarpeta) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Fallo al guardar archivo vacío.");
            }

            Path destinationFolder = this.rootLocation.resolve(subCarpeta);

            if (!Files.exists(destinationFolder)) {
                Files.createDirectories(destinationFolder);
            }

            String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            Path destinationFile = destinationFolder.resolve(filename).normalize().toAbsolutePath();
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }

            return "/images/" + subCarpeta + "/" + filename;

        } catch (IOException e) {
            throw new RuntimeException("Fallo al guardar el archivo.", e);
        }
    }
}