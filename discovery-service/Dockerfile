# --- Etapa 1: build ---
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copiamos el Maven Wrapper y pom.xml
COPY mvnw .
COPY pom.xml .
COPY .mvn/ .mvn/

# Instalamos bash para Alpine
RUN apk add --no-cache bash

# Copiamos el resto del código
COPY src ./src

# Compilamos el JAR
RUN ./mvnw clean package -DskipTests

# --- Etapa 2: run ---
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copiamos el JAR generado
COPY --from=build /app/target/discovery-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]
