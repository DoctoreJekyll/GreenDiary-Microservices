# --- Etapa 1: build ---
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Instalamos bash, Maven y git
RUN apk add --no-cache bash maven git

# Copiamos todo el proyecto (parent POM y módulos)
COPY . .

# Compilamos solo discovery-service y sus dependencias
RUN mvn clean package -DskipTests -pl discovery-service -am

# --- Etapa 2: run ---
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copiamos el JAR compilado
COPY --from=build /app/discovery-service/target/discovery-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]
