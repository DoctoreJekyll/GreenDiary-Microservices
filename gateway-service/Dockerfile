# --- Etapa build ---
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
RUN apk add --no-cache bash maven git

COPY .. .
RUN mvn -f pom.xml clean package -DskipTests

# --- Etapa runtime ---
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=build /app/gateway-service/target/gateway-service-0.0.1-SNAPSHOT.jar app.jar
COPY --from=build /app/target/gateway-service-0.0.1-SNAPSHOT.jar app.jar


EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
