# Etapa 1: Builder (Construcción)
FROM eclipse-temurin:21-jdk AS builder

# Set the working directory
WORKDIR /app

# Copiar el código de la aplicación
COPY . .

# Dar permisos de ejecución al script mvnw
RUN chmod +x ./mvnw

# Construir la aplicación (requiere Maven o Gradle)
RUN ./mvnw clean package -DskipTests

# Etapa 2: Run (Ejecución)
FROM eclipse-temurin:21-jre

# Set the working directory
WORKDIR /app

# Copiar el archivo JAR de la etapa de construcción
COPY --from=builder /app/target/*.jar app.jar


# Exponer el puerto para la aplicación (ajustar si es necesario)
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
