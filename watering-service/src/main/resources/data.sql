-- Riegos de prueba (solo si no existen)
INSERT INTO waterings (plant_id, watering_date, notes, owner_username)
SELECT 1, now(), 'primer riego', 'jose'
WHERE NOT EXISTS (
    SELECT 1 FROM waterings WHERE plant_id = 1 AND notes = 'primer riego' AND owner_username = 'jose'
);

INSERT INTO waterings (plant_id, watering_date, notes, owner_username)
SELECT 1, now() - interval '7 day', 'riego anterior', 'jose'
WHERE NOT EXISTS (
    SELECT 1 FROM waterings WHERE plant_id = 1 AND notes = 'riego anterior' AND owner_username = 'jose'
);
