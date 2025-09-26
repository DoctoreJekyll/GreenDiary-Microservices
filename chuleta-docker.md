# Construir imágenes (cuando cambias código y vuelves a empaquetar los jars)
docker compose build

# Levantar todo (en segundo plano)
docker compose up -d

# Ver estado de contenedores
docker compose ps

# Ver logs de todos los contenedores
docker compose logs -f

# Ver logs de un servicio concreto
docker compose logs -f nombre_servicio
# Ej: docker compose logs -f plant-service

# Reiniciar un servicio concreto (tras cambiar JAR y reconstruir)
docker compose up -d --build nombre_servicio

# Parar todos los contenedores
docker compose down

# Parar + borrar volúmenes (cuidado, borra datos persistentes de BD)
docker compose down -v
