# build-all.ps1
Write-Host "Compilando todos los microservicios..."

$services = @("discovery-service", "gateway-service", "auth-service", "plant-service", "watering-service")

foreach ($svc in $services) {
    Write-Host "==> Compilando $svc ..."
    Set-Location $PSScriptRoot\$svc
    mvn clean package -DskipTests
    Set-Location $PSScriptRoot
}

Write-Host "✅ Compilación completada para todos los servicios."
Write-Host "Ahora puedes hacer: docker compose up --build"
