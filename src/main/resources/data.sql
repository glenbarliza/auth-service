-- Roles
INSERT INTO roles (id, name) VALUES (1, 'ROLE_USER');
INSERT INTO roles (id, name) VALUES (2, 'ROLE_ADMIN');

-- Usuarios
INSERT INTO users ( name, email, password) VALUES
  ( 'Usuario Normal', 'user@example.com', '$2a$10$bFXIgo9yCwgrATpmLz7D5ucAdwzLzDAZL8E.S3nOMsSb5H/Ow7V4O'), -- password: user123
  ('Administrador', 'admin@example.com', '$2a$10$S39l7KRno.rCP16Qz6WrnezOAiy6N6sy1nH1h2cuPGjuabsERsY2G'); -- password: admin123

-- Relación usuarios_roles
INSERT INTO usuarios_roles (usuarios_id, rol_id) VALUES (1, 1); -- user → ROLE_USER
INSERT INTO usuarios_roles (usuarios_id, rol_id) VALUES (2, 2); -- admin → ROLE_ADMIN
