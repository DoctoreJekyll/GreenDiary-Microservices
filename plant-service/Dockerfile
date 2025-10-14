# --- Etapa build ---
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
RUN apk add --no-cache bash maven git

COPY . .
RUN mvn clean package -DskipTests -pl plant-service -am

# --- Etapa runtime ---
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=build /app/plant-service/target/plant-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java","-jar","app.jar"]
