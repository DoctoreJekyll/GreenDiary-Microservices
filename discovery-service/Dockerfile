# --- Etapa 1: build ---
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copiamos Maven Wrapper y POMs
COPY mvnw .
COPY .mvn/ .mvn/
COPY pom.xml .                             # parent pom
COPY discovery-service/pom.xml ./discovery-service/pom.xml
COPY gateway-service/pom.xml ./gateway-service/pom.xml
COPY plant-service/pom.xml ./plant-service/pom.xml
COPY watering-service/pom.xml ./watering-service/pom.xml
COPY auth-service/pom.xml ./auth-service/pom.xml

# Copiamos los sources
COPY discovery-service/src ./discovery-service/src
COPY gateway-service/src ./gateway-service/src
COPY plant-service/src ./plant-service/src
COPY watering-service/src ./watering-service/src
COPY auth-service/src ./auth-service/src

# Instalamos bash
RUN apk add --no-cache bash

# Hacemos mvnw ejecutable
RUN chmod +x mvnw

# Compilamos discovery-service y sus dependencias (incluyendo parent)
RUN ./mvnw clean package -DskipTests -pl discovery-service -am

# --- Etapa 2: run ---
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copiamos el JAR compilado
COPY --from=build /app/discovery-service/target/discovery-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]
