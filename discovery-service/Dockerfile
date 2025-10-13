# --- Etapa 1: BUILD (Compilación) ---
# Usamos un JDK más completo para la fase de compilación
FROM eclipse-temurin:21-jdk-alpine AS build

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia los archivos de configuración de Maven (pom.xml) y el wrapper
COPY pom.xml .
# IMPORTANTE: Copiamos el pom.xml del módulo Eureka
COPY discovery-service/pom.xml discovery-service/

# Copia el código fuente del módulo (necesario para el build)
COPY discovery-service/src discovery-service/src

# Usa Maven para hacer 'package'. El resultado estará en discovery-service/target/
# Debemos indicar que navegue a la carpeta del módulo para construirlo.
RUN ./mvnw clean package -DskipTests -f discovery-service/pom.xml

# --- Etapa 2: RUN (Ejecución) ---
# Usamos una imagen ligera para la ejecución
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copia el JAR del módulo desde la etapa 'build'
# La ruta ahora es relativa al directorio de trabajo interno de la etapa build
COPY --from=build /app/discovery-service/target/discovery-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8761
ENTRYPOINT ["java","-jar","app.jar"]