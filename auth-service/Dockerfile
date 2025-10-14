# --- Etapa build ---
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Instalamos bash, Maven y git
RUN apk add --no-cache bash maven git

# Copiamos  el proyecto para que Maven pueda resolver el parent POM
COPY . .

# Compilamos solo auth-service y lo que dependa de él
RUN mvn clean package -DskipTests -pl auth-service -am

# --- Etapa runtime ---
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copiamos el JAR compilado
COPY --from=build /app/auth-service/target/auth-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8084
ENTRYPOINT ["java","-jar","app.jar"]
