# --- Etapa 1: build ---
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copiamos Maven Wrapper y POM
COPY mvnw .
COPY .mvn/ .mvn/
COPY pom.xml .
COPY src ./src

# Instala bash (Alpine)
RUN apk add --no-cache bash

# Hacemos mvnw ejecutable
RUN chmod +x mvnw

# Compilamos el JAR
RUN ./mvnw clean package -DskipTests

# --- Etapa 2: run ---
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copiamos el JAR generado
COPY --from=build /app/target/discovery-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]
