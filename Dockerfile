# Etapa 1: Construcción (Build)
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
# Descargar dependencias primero para aprovechar caché
RUN ./mvnw dependency:go-offline -B

COPY src ./src
# Compilar y empaquetar saltando las pruebas (ya se habrán pasado en CI/CD)
RUN ./mvnw clean package -DskipTests

# Etapa 2: Ejecución (Runtime)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Copiar el jar generado desde la etapa de construcción
COPY --from=builder /app/target/*.jar app.jar

# Exponer el puerto configurado de Spring Boot
EXPOSE 8082

# Definir la zona horaria y el perfil activo de Spring Boot
ENV TZ=America/Bogota
ENV SPRING_PROFILES_ACTIVE=docker
ENV SERVER_PORT=8082

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
