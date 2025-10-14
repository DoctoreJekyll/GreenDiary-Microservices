# --- Etapa 1: build ---
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Instalamos bash y Maven
RUN apk add --no-cache bash maven git

# Copiamos el parent pom y todos los módulos
COPY pom.xml .
COPY discovery-service/pom.xml ./discovery-service/pom.xml
COPY gateway-service/pom.xml ./gateway-service/pom.xml
COPY plant-service/pom.xml ./plant-service/pom.xml
COPY watering-service/pom.xml ./watering-service/pom.xml
COPY auth-service/pom.xml ./auth-service/pom.xml

# Copiamos los src solo de discovery-service (las dependencias de otros módulos se resolverán por parent)
COPY discovery-service/src ./discovery-service/src

# Compilamos discovery-service y lo que dependa de él
RUN mvn clean package -DskipTests -pl discovery-service -am

# --- Etapa 2: run ---
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copiamos el JAR compilado
COPY --from=build /app/discovery-service/target/discovery-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]
