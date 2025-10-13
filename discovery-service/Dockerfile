# --- Etapa 1: BUILD (Compilación) ---
# Usa una imagen con JDK completo para compilar
FROM eclipse-temurin:21-jdk-alpine AS build

# Establece el directorio de trabajo
WORKDIR /app

# 1. Copia el pom.xml RAÍZ y el wrapper
# Este es el paso clave: necesitamos la configuración de dependencias y el mvnw
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# 2. Copia el módulo de Eureka completo
# Copia la carpeta del módulo, incluyendo su pom.xml y código fuente
COPY discovery-service discovery-service

# 3. COMPILACIÓN: Usa el pom.xml del módulo para compilarlo
# La flag -f (file) le indica a Maven qué pom debe ejecutar.
# Esto compila el módulo y todas sus dependencias (incluyendo el pom padre)
RUN ./mvnw clean package -DskipTests -f discovery-service/pom.xml

# --- Etapa 2: RUN (Ejecución) ---
# Usa una imagen ligera (JRE) para la ejecución final
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Define el nombre del JAR basándote en el pom del discovery-service
# IMPORTANTE: La ruta es /app/discovery-service/target/nombre-del-jar
COPY --from=build /app/discovery-service/target/discovery-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8761
ENTRYPOINT ["java","-jar","app.jar"]