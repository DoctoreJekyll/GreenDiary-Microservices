# ---- Build stage ----
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Instalar herramientas necesarias
RUN apk add --no-cache bash maven git

# Copiamos el proyecto (como contexto es la raíz, podemos usar rutas relativas internas)
COPY . .

# Compilamos solo el módulo auth-service
RUN mvn clean package -DskipTests -pl gateway-service -am

# ---- Runtime stage ----
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copiamos el jar compilado desde la etapa anterior
COPY --from=build /app/gateway-service/target/gateway-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]
