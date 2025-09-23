-- Usuario dummy para probar (solo si no existe)
INSERT INTO users (username, password)
SELECT 'jose', '{bcrypt}$2a$10$K0i63yZqJ3KOn5YQG2EJfuwEelSfxrZ5VpoVbO5LzNMQM3FlMB9MG'
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE username = 'jose'
);

-- Rol dummy para ese usuario (solo si no existe)
INSERT INTO user_roles (user_id, role)
SELECT id, 'ROLE_USER'
FROM users
WHERE username = 'jose'
  AND NOT EXISTS (
    SELECT 1 FROM user_roles ur
                      JOIN users u ON ur.user_id = u.id
    WHERE u.username = 'jose' AND ur.role = 'ROLE_USER'
);
