# --- Etapa 1: BUILD (Compilación Multi-Módulo) ---
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# 1. Copia los archivos de la RAÍZ (pom.xml padre, mvnw, .mvn)
# Estos son esenciales para el Maven Wrapper y la estructura multi-módulo.
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# 2. Copia los módulos necesarios (Auth y Common-Lib)
# El código fuente del servicio y sus dependencias locales.
COPY auth-service auth-service
COPY common-lib common-lib

# 3. Compilación: Usa el Maven Wrapper para compilar SOLO el módulo de autenticación.
# La compilación se dirige al pom.xml del módulo.
RUN ./mvnw clean package -DskipTests -f auth-service/pom.xml

# --- Etapa 2: RUN (Ejecución) ---
# Usamos JRE para un contenedor más ligero
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# 4. Copia el JAR desde la ubicación CORRECTA (dentro de la carpeta del módulo)
COPY --from=build /app/auth-service/target/auth-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8084

# 5. Comando de inicio (CMD)
# Forzamos el comando correcto para anular la detección fallida de Railway.
CMD ["java", "-jar", "app.jar"]