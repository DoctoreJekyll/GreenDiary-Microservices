# --- Etapa 1: build ---
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copiamos solo los pom.xml primero para aprovechar cache
COPY pom.xml .
COPY discovery-service/pom.xml ./discovery-service/pom.xml
# (si hay parent pom, copialo también)
RUN apk add --no-cache bash
RUN ./mvnw dependency:resolve

# Copiamos el resto del código
COPY . .

# Compilamos y generamos el JAR
RUN ./mvnw clean package -DskipTests

# --- Etapa 2: run ---
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=build /app/discovery-service/target/discovery-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]
