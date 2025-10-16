# --- Etapa build ---
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
RUN apk add --no-cache bash maven git

COPY .. .
RUN mvn -f pom.xml clean package -DskipTests

# --- Etapa runtime ---
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=build /app/watering-service/target/watering-service-0.0.1-SNAPSHOT.jar app.jar


EXPOSE 8082
ENTRYPOINT ["java","-jar","app.jar"]
