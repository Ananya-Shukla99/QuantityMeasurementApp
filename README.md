# Quantity Measurement Microservices

This project is a Spring Boot microservices setup for quantity conversion and comparison.

## Services

| Service | Port | Purpose |
|---|---:|---|
| `eureka-server` | `8761` | Service registry and discovery |
| `api-gateway` | `8080` | Single entry point for requests |
| `auth-service` | `8081` | Registration and login |
| `qma-service` | `8082` | Quantity operations and history |

## Quick start

Start the services in this order:

1. `eureka-server`
2. `auth-service`
3. `qma-service`
4. `api-gateway`

### Using Maven

```powershell
cd D:\workspace-spring-tools-for-eclipse-5.0.1.RELEASE\QuantityMeasurementApp
mvn -pl eureka-server spring-boot:run
mvn -pl auth-service spring-boot:run
mvn -pl qma-service spring-boot:run
mvn -pl api-gateway spring-boot:run
```

### Using Spring Tools for Eclipse

Right-click each project and run it as a Spring Boot application in the order above.

## URLs to open in your browser

- Eureka dashboard: `http://localhost:8761/`
- Auth Swagger UI: `http://localhost:8081/swagger-ui/index.html`
- QMA Swagger UI: `http://localhost:8082/swagger-ui/index.html`

## API routes

Use the gateway at `http://localhost:8080`.

### Auth routes

- `POST /api/v1/auth/register`
- `POST /api/v1/auth/login`

### Quantity routes

- `POST /api/v1/quantities/compare`
- `POST /api/v1/quantities/convert`
- `POST /api/v1/quantities/add`
- `POST /api/v1/quantities/subtract`
- `POST /api/v1/quantities/divide`
- `GET /api/v1/quantities/history/{operation}`
- `GET /api/v1/quantities/history/type/{measurementType}`
- `GET /api/v1/quantities/count/{operation}`

## Authentication flow

1. Register or login using the auth routes.
2. Copy the returned JWT token.
3. Send it in the request header:

```http
Authorization: Bearer <token>
```

## Local configuration

- All services share the same `JWT_SECRET` value for local development.
- Eureka is used for service discovery.
- The gateway routes requests to services by name.

## How to check everything is working

You should see these results:

- `http://localhost:8761/` opens the Eureka dashboard
- `http://localhost:8081/swagger-ui/index.html` opens the auth docs
- `http://localhost:8082/swagger-ui/index.html` opens the quantity docs
- Health endpoints return `200 OK`

### Health checks

```powershell
curl http://localhost:8761/actuator/health
curl http://localhost:8080/actuator/health
curl http://localhost:8081/actuator/health
curl http://localhost:8082/actuator/health
```

## Notes

- The Eureka server does not have Swagger UI.
- The gateway is the main entry point for API calls.
- If a service does not appear in Eureka, start `eureka-server` first and wait a few seconds.


