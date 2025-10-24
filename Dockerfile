# Usamos una imagen base de Java 17 (puedes cambiar la versión si usas otra)
FROM openjdk:21-slim

# Definimos un argumento para el nombre del JAR (lo estándar de Spring Boot)
ARG JAR_FILE=target/*.jar

# Copiamos el .jar construido a la imagen del contenedor
COPY ${JAR_FILE} app.jar

# Exponemos el puerto en el que corre Spring Boot (usualmente 8080)
EXPOSE 8080

# El comando para arrancar la aplicación cuando el contenedor inicie
ENTRYPOINT ["java", "-jar", "/app.jar"]