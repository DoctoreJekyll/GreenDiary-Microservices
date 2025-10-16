# --- Etapa build ---
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Instalamos bash, Maven y git
RUN apk add --no-cache bash maven git

COPY .. .
RUN mvn -f pom.xml clean package -DskipTests

# --- Etapa runtime ---
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copiamos el JAR compilado
COPY --from=build /app/target/auth-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8084
ENTRYPOINT ["java","-jar","app.jar"]
