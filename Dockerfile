# compilar el proyecto con maven
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# seleccionas la imagen final para producción
FROM eclipse-temurin:17-jre
WORKDIR /app

# copias el JAR q se genera con el mvn
COPY --from=build /app/target/*.jar app.jar

VOLUME ["/app/data", "/app/archivos"]

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
