# ==========================================================
# Script de comprobacion de entorno plantas-micro
# ==========================================================
# Comprueba estado de contenedores, Eureka, Gateway y Kafka
# ==========================================================

Write-Host "Comprobando contenedores..."
Write-Host "-------------------------------------"

$containers = @(
    "discovery",
    "gateway",
    "plant-service",
    "watering-service",
    "auth-service",
    "kafka",
    "zookeeper",
    "plant-db",
    "watering-db",
    "auth-db"
)

foreach ($c in $containers) {
    $status = docker inspect -f '{{.State.Status}}' $c 2>$null
    switch ($status) {
        "running" { Write-Host "OK: $c esta en ejecucion" -ForegroundColor Green }
        "exited"  { Write-Host "X: $c esta detenido" -ForegroundColor Red }
        default   { Write-Host "?: $c no existe o no esta inicializado" -ForegroundColor Yellow }
    }
}

Write-Host "`nComprobando Eureka..."
try {
    $eureka = Invoke-WebRequest -Uri "http://localhost:8761" -UseBasicParsing -ErrorAction Stop
    if ($eureka.Content -match "Eureka") {
        Write-Host "OK: Eureka esta activo en http://localhost:8761" -ForegroundColor Green
    } else {
        Write-Host "X: Eureka no responde correctamente" -ForegroundColor Red
    }
} catch {
    Write-Host "X: No se pudo conectar a Eureka" -ForegroundColor Red
}

Write-Host "`nComprobando Kafka..."
$kafkaLogs = docker logs kafka 2>&1
if ($kafkaLogs -match "started \(kafka.server.KafkaServer\)") {
    Write-Host "OK: Kafka esta operativo" -ForegroundColor Green
} else {
    Write-Host "X: Kafka aun no ha iniciado correctamente" -ForegroundColor Red
}

Write-Host "`nComprobando Gateway..."
try {
    $gateway = Invoke-WebRequest -Uri "http://localhost:8080/actuator/health" -UseBasicParsing -ErrorAction Stop
    if ($gateway.Content -match "UP") {
        Write-Host "OK: Gateway responde correctamente" -ForegroundColor Green
    } else {
        Write-Host "X: Gateway no responde como se espera" -ForegroundColor Red
    }
} catch {
    Write-Host "X: Gateway no esta accesible en http://localhost:8080/actuator/health" -ForegroundColor Red
}

Write-Host "`nComprobando microservicios registrados en Eureka..."
try {
    $apps = Invoke-WebRequest -Uri "http://localhost:8761/eureka/apps" -UseBasicParsing -ErrorAction Stop
    $count = ($apps.Content | Select-String "<app>").Count
    if ($count -ge 3) {
        Write-Host "OK: Se detectan multiples servicios registrados en Eureka ($count)" -ForegroundColor Green
    } else {
        Write-Host "Aviso: Eureka aun no tiene todos los servicios registrados ($count)" -ForegroundColor Yellow
    }
} catch {
    Write-Host "X: No se pudo consultar Eureka Apps" -ForegroundColor Red
}

Write-Host "`nComprobacion completada."
Write-Host "-------------------------------------"
Write-Host "Si todos los pasos son OK, tu stack esta correctamente levantado." -ForegroundColor Cyan
