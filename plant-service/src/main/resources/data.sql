-- Plantas de prueba (solo si no existen)
INSERT INTO plants (name, species, location, notes, last_watered, owner_username)
SELECT 'Rosa', 'Super rosa', 'Salon', 'Notas', now(), 'jose'
WHERE NOT EXISTS (
    SELECT 1 FROM plants WHERE name = 'Rosa' AND owner_username = 'jose'
);

INSERT INTO plants (name, species, location, notes, last_watered, owner_username)
SELECT 'Aloe Vera', 'Aloe', 'Cocina', 'Notas de aloe', now(), 'jose'
WHERE NOT EXISTS (
    SELECT 1 FROM plants WHERE name = 'Aloe Vera' AND owner_username = 'jose'
);