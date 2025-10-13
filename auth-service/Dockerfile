# --- Etapa 1: BUILD (Compilación) ---
# Usamos una imagen con JDK completo para compilar
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# 1. Copia los archivos de la RAÍZ (pom padre y wrapper)
# Esto es necesario para que el mvnw funcione en el contexto multi-módulo
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# 2. Copia la carpeta del módulo y sus dependencias locales (como common-lib)
COPY auth-service auth-service
COPY common-lib common-lib
# Repite COPY para cualquier otra dependencia interna (si aplica)

# 3. COMPILACIÓN: Dirige Maven para que compile SOLO el módulo de autenticación
RUN ./mvnw clean package -DskipTests -f auth-service/pom.xml

# --- Etapa 2: RUN (Ejecución) ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# 4. CORRECCIÓN CLAVE: Copia el JAR desde la ruta correcta (dentro de la carpeta del módulo)
COPY --from=build /app/auth-service/target/auth-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8084
CMD ["java", "-jar", "app.jar"]