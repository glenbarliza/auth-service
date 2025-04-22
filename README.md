# Auth Service 🔐

Microservicio de autenticación con JWT para una arquitectura de microservicios basada en Spring Boot.

---

## 🚀 Tecnologías usadas

- Java 17
- Spring Boot 3.x
- Spring Security (con JWT)
- Spring Data JPA
- H2 Database (modo desarrollo)
- Swagger / OpenAPI 3
- Lombok

---

## 📌 Objetivo

Este microservicio se encarga de:

- Registrar nuevos usuarios (`/auth/register`)
- Autenticar usuarios con credenciales (`/auth/login`)
- Emitir JWT firmados con `HS256`
- Validar y extraer datos desde tokens
- Documentar y probar la API con Swagger UI

---

## 🛠️ Cómo ejecutar el proyecto

1. Clonar el repositorio o descargar el ZIP
2. Asegurarse de tener Java 17 y Maven instalados
3. Ejecutar:

```bash
mvn clean spring-boot:run
```

4. Acceder a la consola:

- Swagger: [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)
- H2 Console: [http://localhost:8081/h2-console](http://localhost:8081/h2-console)
    - JDBC URL: `jdbc:h2:mem:userdb`

---

## 🔐 Endpoints principales

### 🔸 POST `/auth/register`
Registra un nuevo usuario y devuelve un JWT.

```json
{
  "name": "Juan",
  "email": "juan@example.com",
  "password": "123456",
  "roles": ["ROLE_USER"]
}
```

### 🔸 POST `/auth/login`
Autentica un usuario registrado.

```json
{
  "email": "juan@example.com",
  "password": "123456"
}
```

📥 **Respuesta:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6..."
}
```

---

## 🔒 Seguridad

- El token JWT contiene el email y los roles como claims.
- Se espera el header `Authorization: Bearer <token>` en rutas protegidas.
- No se usa `httpBasic()`, ni sesión. Todo es **stateless**.

---

## ✏️ Consideraciones adicionales

- Usa encriptación `BCrypt` para contraseñas.
- Swagger no requiere autenticación.
- Estructura lista para escalar y conectar con `user-service`.

---

## 📬 Contacto

Para soporte técnico o contribuciones, contactar a: `barliza@gmail.com`