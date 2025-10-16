# ---- Build stage ----
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Instalar herramientas necesarias
RUN apk add --no-cache bash maven git

# Copiamos el proyecto (como contexto es la raíz, podemos usar rutas relativas internas)
COPY . .

# Compilamos solo el módulo auth-service
RUN mvn clean package -DskipTests -pl auth-service -am

# ---- Runtime stage ----
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copiamos el jar compilado desde la etapa anterior
COPY --from=build /app/auth-service/target/auth-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]

#Postgres cluster empty-bush-8872 created
 #  Username:    postgres
 #  Password:    cw4MSfhIXYkSD7J
 #  Hostname:    empty-bush-8872.internal
 #  Flycast:     fdaa:30:c8:0:1::2
 #  Proxy port:  5432
 #  Postgres port:  5433
 #  Connection string: postgres://postgres:cw4MSfhIXYkSD7J@empty-bush-8872.flycast:5432