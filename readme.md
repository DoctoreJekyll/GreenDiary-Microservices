# 🌱 Plant Diary — Microservicios (README)

Este README es **una guía completa y única** para levantar, desarrollar y depurar el proyecto "Plant Diary" (microservicios: plant, watering, auth, gateway, discovery) con Kafka, Postgres y Docker Compose.

---

# Índice

1. Descripción breve
2. Arquitectura (qué hace cada servicio)
3. Tecnologías
4. Estructura del repositorio
5. Requisitos previos
6. Construcción (jar + docker)
7. Docker Compose (levantar todo)
8. Variables de entorno importantes / cómo configurar
9. Kafka: crear topics y probar con console-producer/consumer
10. Flujo de eventos sugerido y ejemplos JSON
11. Pruebas (curl / Postman ejemplos)
12. Errores habituales y soluciones rápidas
---

# 1) Descripción

Aplicación compuesta por microservicios para llevar un diario de plantas: registrar plantas, guardar riegos (historico), autenticación con JWT, comunicación síncrona (REST) y asincrónica (Kafka). Está pensada para ser reproducible localmente con Docker Compose.

---

# 2) Arquitectura

* **Discovery (Eureka)** — registro de instancias
* **API Gateway** — entrada unificada (reescribe path hacia /api/xxx)
* **Plant Service** — CRUD de plantas + consumidor Kafka (actualiza lastWatered)
* **Watering Service** — CRUD de riegos + productor Kafka (emite evento cuando se crea un riego)
* **Auth Service** — login/register + gestión usuarios y roles
* **Kafka + Zookeeper** — mensajería asíncrona
* **Postgres** — bases de datos por microservicio (plantdb, wateringdb, authdb)

Topología simplificada:

```
Client -> Gateway (8080) -> (via Eureka) -> Plant (8081) / Watering (8082) / Auth (8084)
                       \-> Kafka (9092) -> topics -> Plant consumer / otros
```

---

# 3) Tecnologías

* Java 21, Spring Boot 3.x
* Spring Cloud (Feign, Gateway, Eureka)
* Spring Security (JWT)
* Spring Kafka
* PostgreSQL
* Docker, Docker Compose
* Maven

---

# 4) Estructura del repositorio (sugerida)

```
/ (raíz)
  docker-compose.yml
  /plant-service
    Dockerfile
    pom.xml
    src/...
  /watering-service
    Dockerfile
    pom.xml
    src/...
  /auth-service
  /gateway-service
  /discovery-service
  /common-lib  (artefacto java compartido: DTOs, excepciones)
```

`common-lib` es una librería compartida: **no** es un servicio, sino un módulo que compilas con Maven para que los otros servicios lo consuman en tiempo de compilación.

---

# 5) Requisitos previos (local sin Docker)

* Java 21 y Maven (si quieres compilar jars antes de docker)
* Docker Desktop (o Docker Engine) + Docker Compose

---

# 6) Construcción local (jars)

Desde la raíz del repo (si es multi-módulo):

```bash
mvn clean package -DskipTests
```

* Esto genera `target/*.jar` en cada microservicio. Si ves `no main manifest attribute, in app.jar` significa que no generaste el jar correcto (ejecuta `mvn package`).

---

# 7) Docker: build & up (todo el stack)

**1) Opcional**: construir imágenes si en Dockerfile haces COPY de `target/*.jar` (recomendado):

```bash
# desde la raíz, si tus Dockerfiles usan COPY target/*.jar
mvn clean package -DskipTests
```

**2) Construir imágenes y levantar**

```bash
# build y levantado (desde la raíz con docker-compose.yml)
docker compose build
# arrancar en background
docker compose up -d
# ver contenedores
docker compose ps
# ver logs
docker compose logs -f
```

**NOTA importante sobre hosts**

* Dentro de Docker Compose, los servicios se ven por nombre de servicio (p. ej. `kafka:9092`, `plant-db:5432`).
* Desde tu host (Postman, navegador) usas `localhost` y los puertos mapeados (ej. `localhost:8080` para gateway).

---

# 8) Variables y application.yml — cómo mapearlas en docker-compose

En tu `docker-compose.yml` pasarás variables que overridean `application.yml` usando `SPRING_` env vars. Ejemplos (tu compose ya tiene estos):

```yaml
environment:
  SPRING_DATASOURCE_URL: jdbc:postgresql://plant-db:5432/plantdb
  SPRING_DATASOURCE_USERNAME: plantuser
  SPRING_DATASOURCE_PASSWORD: plantpass
  SPRING_KAFKA_BOOTSTRAP_SERVERS: kafka:9092
  EUREKA_CLIENT_SERVICEURL_DEFAULTZONE: http://discovery:8761/eureka
```

En `application.yml` de cada servicio deja una propiedad por defecto, pero permite override con env vars:

```yaml
spring:
  datasource:
    url: ${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5433/plantdb}
  kafka:
    bootstrap-servers: ${SPRING_KAFKA_BOOTSTRAP_SERVERS:localhost:9092}
```

**Recuerda:** cuando los servicios corren dentro del mismo compose, `bootstrap-servers` debe ser `kafka:9092`. Si pruebas desde tu host, `localhost:9092`.

---

# 9) Kafka — crear topic y probar

**Crear topic (ejecutarlo cuando kafka esté listo)**

Windows / PowerShell: ejecuta en una línea (evita el uso de `\` multilínea):

```powershell
docker exec -it kafka bash -c "kafka-topics --create --bootstrap-server localhost:9092 --topic plant-events --partitions 1 --replication-factor 1"
```

Linux/macOS (o WSL):

```bash
docker exec -it kafka bash -c 'kafka-topics --create --bootstrap-server localhost:9092 --topic plant-events --partitions 1 --replication-factor 1'
```

**Consumir todo el topic (desde el inicio)**

```bash
docker exec -it kafka bash -c "kafka-console-consumer --bootstrap-server localhost:9092 --topic plant-events --from-beginning"
```

**Producir mensajes manualmente**

```bash
docker exec -it kafka bash -c "kafka-console-producer --broker-list localhost:9092 --topic plant-events"
# luego escribe JSON en la consola
```

**Notas sobre errores que viste antes**

* `LEADER_NOT_AVAILABLE`: Kafka aún no ha electo un líder para la partición. Espera 5-10s y vuelve a crear/consumir. Si persiste, revisa `KAFKA_ADVERTISED_LISTENERS` y que el contenedor kafka esté realmente UP.
* `kafka-console-consumer : El término 'kafka-console-consumer' no se reconoce...`: en PowerShell no tienes las binarios localmente: usa `docker exec -it kafka bash -c "kafka-console-consumer ..."` para ejecutarlo dentro del contenedor.

---

# 10) Flujo de eventos sugerido (ejemplo práctico)

**Tipo de eventos** (ejemplos):

* `PLANT_CREATED` — cuando se crea una planta
* `PLANT_UPDATED` — cambios en la planta
* `WATERING_CREATED` — nuevo riego (lo usará plant-service para actualizar lastWatered)
* `WATERING_DELETED` — opcional

**Ejemplo JSON para `WATERING_CREATED`**

```json
{
  "eventType": "WATERING_CREATED",
  "plantId": 5,
  "owner": "jose",
  "wateredAt": "2025-09-25T17:45:00"
}
```

**Productor (WateringService)**

* Después de `save()` del riego, produce el evento al topic `plant-events`.
* Usa `JsonSerializer` para el `value`.

**Consumidor (PlantService)**

* `@KafkaListener(topics = "plant-events")` recibe eventos.
* Si `eventType == WATERING_CREATED` → llama método que actualiza `plants.lastWatered` con `wateredAt` del evento.

---

# 11) Snippets de código (configuración rápida)

**application.yml (kafka producer/consumer)**

```yaml
spring:
  kafka:
    bootstrap-servers: ${SPRING_KAFKA_BOOTSTRAP_SERVERS:localhost:9092}
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
    consumer:
      group-id: plant-service
      auto-offset-reset: earliest
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.springframework.kafka.support.serializer.JsonDeserializer
      properties:
        spring.json.trusted.packages: '*'
```

**Producer Java (simplificado)**

```java
@Service
public class PlantEventProducer {
  private final KafkaTemplate<String, Object> kafka;
  public PlantEventProducer(KafkaTemplate<String, Object> kafka) { this.kafka = kafka; }
  public void send(String topic, Object event) {
    kafka.send(topic, event).addCallback();
  }
}
```

**Consumer Java (simplificado)**

```java
@Service
public class PlantEventConsumer {
  private final PlantService plantService;
  @KafkaListener(topics = "plant-events", groupId = "plant-service")
  public void listen(PlantEvent event) {
    if ("WATERING_CREATED".equals(event.getEventType())) {
      plantService.updateLastWatered(event.getPlantId(), event.getWateredAt());
    }
  }
}
```

---

# 12) Pruebas: curl / Postman ejemplos

**Login (Auth)**

```bash
POST http://localhost:8080/auth/login
Content-Type: application/json
{
  "username":"jose",
  "password":"password"
}
```

**Crear planta (via Gateway)**

```bash
POST http://localhost:8080/plants
Authorization: Bearer <token>
Content-Type: application/json
{
  "name":"Ficus",
  "species":"Ficus elastica",
  "location":"Salon"
}
```

**Crear riego (via Gateway)**

```bash
POST http://localhost:8080/plants/5/watering
Authorization: Bearer <token>
Content-Type: application/json
{
  "wateringDate":"2025-09-25T17:45:00",
  "notes":"Regado por la tarde"
}
```

Después de crear riego deberías ver en el consumidor (o en consola kafka-console-consumer) el JSON del evento.

---

# 13) Errores comunes y soluciones rápidas

* `no main manifest attribute, in app.jar` → compilación Maven no generó JAR correcto. Ejecuta `mvn clean package`.
* `relation "users" does not exist` → la tabla no existe aún: revisa `ddl-auto` (en dev usa `update` o ejecuta `data.sql`) y que la DB se haya inicializado correctamente.
* `duplicate key value violates unique constraint` al ejecutar `data.sql` → usa `INSERT ... WHERE NOT EXISTS` o `ON CONFLICT DO NOTHING` para evitar recrear entradas.
* Kafka `LEADER_NOT_AVAILABLE` → espera que kafka termine de arrancar o revisa `KAFKA_ADVERTISED_LISTENERS`.
* `Couldn't resolve server kafka:9092` desde host: tu app dentro de docker debe usar `kafka:9092`. Desde anfitrión (host) usa `localhost:9092`.

---
