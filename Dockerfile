# Etapa 1: Compilar el proyecto con Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: Imagen final para producción
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copia el JAR generado
COPY --from=build /app/target/*.jar app.jar

# Copia la base de datos H2 (si existe)
COPY data/ ./data/

# Copia los archivos de recursos (por ejemplo, archivos subidos)
COPY src/main/resources/archivos/ ./src/main/resources/archivos/

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
