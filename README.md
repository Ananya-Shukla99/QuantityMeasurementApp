# Quantity Measurement App

## Overview

The Quantity Measurement App is a Java-based application designed to compare different physical quantities such as length and weight across various units.

The application is developed incrementally using clearly defined use cases. Each use case introduces a specific level of functionality, ensuring controlled scope, maintainability, and clean architecture.

The project emphasizes:
- Object-Oriented Design principles
- DRY (Don’t Repeat Yourself)
- Incremental development
- Clean code practices
- Proper unit testing

---  

## Objective

The primary goal of this application is to:

- Compare quantities across different units
- Support unit conversion
- Maintain correctness using test-driven development
- Gradually evolve toward supporting quantity arithmetic
- Demonstrate scalable design using enums and generic modeling

---

## Development Approach

This project follows a Use Case Driven Development approach.

Each use case:
- Has a clearly defined scope
- Introduces a small, testable feature
- Avoids unnecessary complexity
- Builds upon the previous implementation

Note:
The scope must remain limited to the current use case requirements.  
Over-engineering or prematurely adding features reduces maintainability and clarity, especially in enterprise environments where domain knowledge may not always be fully available.

---
# Quantity Measurement Application

A Java-based application that demonstrates measurement equality comparison using object-oriented principles.

---

## UC1: Feet Measurement Equality

## Description
Checks equality of two numerical values in feet, handling null, type mismatch, and floating-point precision.

## Flow
1. Input two numerical values in feet.
2. Validate inputs are numeric.
3. Compare for equality → return `true` or `false`.

## Key Concepts
- Override `equals()` using `Double.compare()` instead of `==`
- `private final` field for immutability
- Null & type safety to prevent exceptions

---

## UC2: Feet and Inches Measurement Equality

## Description

Extends UC1 to support equality checks for both Feet and Inches independently using separate classes. Reduces main method dependency via static helper methods.

## Flow
1. Static method validates two feet values → compares equality.
2. Static method validates two inches values → compares equality.
3. Returns `true` / `false` for each comparison.

## Key Concepts
- Separate `Inches` class mirroring `Feet` (same equality logic)
- Static methods for Feet and Inches equality checks
- Violates DRY principle (addressed in UC3)

---

## UC3: Generic Quantity Class (DRY Principle)

## Description
Refactors Feet and Inches into a single `QuantityLength` class using a `LengthUnit` enum. Eliminates code duplication and supports cross-unit equality (e.g., 1 foot == 12 inches).

## Flow
1. Input value + unit type → validate.
2. Convert both values to base unit (feet).
3. Compare converted values → return `true` / `false`.

## Key Concepts
- `LengthUnit` enum with conversion factors
- Single class handles all unit types (DRY)
- Cross-unit equality via base unit normalization

---


## UC4: Extended Unit Support (Yards & Centimeters)

## Description
Extends UC3 by adding YARDS (1 yd = 3 ft) and CENTIMETERS (1 cm = 0.393701 in) to the `LengthUnit` enum. No changes to `QuantityLength` class required.

## Flow
1. Input value + unit (feet/inches/yards/cm) → validate.
2. Convert both to base unit (feet).
3. Compare → return `true` / `false`.

## Conversion Factors
| Unit | Factor (to feet) |
|------|-----------------|
| YARDS | 3.0 |
| CENTIMETERS | ~0.0328 |

---

## UC5: Unit-to-Unit Conversion

## Description
Extends UC4 by exposing an explicit `convert(value, sourceUnit, targetUnit)` method. Normalizes to base unit (feet) then converts to target unit. Introduces method overloading and JavaDoc documentation.

## Flow
1. Validate value (finite), sourceUnit and targetUnit (non-null).
2. Convert value → base unit using `sourceUnit.getConversionFactor()`.
3. Convert base unit → target using `targetUnit.getConversionFactor()`.
4. Return converted numeric value.

## Key Concepts
- Method overloading: `demonstrateLengthConversion(double, LengthUnit, LengthUnit)` and `(QuantityLength, LengthUnit)`
- Private helper methods for encapsulation
- `toString()` override for readability
- Formula: `result = value × (source.factor / target.factor)`

---

## UC6: Addition of Two Length Units

## Description
Extends UC5 by adding two `QuantityLength` objects (potentially different units). Result is expressed in the unit of the first operand. Both operands are normalized to base unit before summing.

## Flow
1. Validate both operands (non-null, finite, valid units).
2. Convert both to base unit (feet).
3. Sum converted values.
4. Convert sum → first operand's unit.
5. Return new `QuantityLength` (immutability preserved).

## Key Concepts
- Immutability: addition returns new instance
- Commutativity: `add(A, B)` = `add(B, A)`
- Method overloading for flexible API

---

## UC7: Addition with Explicit Target Unit

## Description
Extends UC6 by allowing the caller to specify any supported unit as the result unit, regardless of the operands' units. Uses a private utility method to avoid code duplication across overloaded `add()` methods.

## Flow
1. Validate operands and target unit (non-null, finite).
2. Convert both to base unit → sum.
3. Convert sum → explicitly specified `targetUnit`.
4. Return new `QuantityLength` in target unit.

## Key Concepts
- Method overloading: `add(l1, l2)` implicit vs `add(l1, l2, targetUnit)` explicit
- Private utility method eliminates DRY violation between overloads
- Commutativity holds for any target unit

---

## UC8: Refactoring LengthUnit to Standalone Enum

## Description
Extracts `LengthUnit` from inside `QuantityLength` into a standalone top-level class. Assigns conversion responsibility to the enum itself. `QuantityLength` is simplified to delegate all conversions to unit methods. All UC1–UC7 functionality preserved.

## Flow
1. `LengthUnit` enum handles `convertToBaseUnit()` and `convertFromBaseUnit()`.
2. `QuantityLength` delegates all conversions to unit methods.
3. Public API remains unchanged → backward compatible.

## Key Concepts
- Single Responsibility: `LengthUnit` converts, `QuantityLength` compares/adds
- Eliminates circular dependency for multi-category scaling
- Pattern template for future `WeightUnit`, `VolumeUnit`, etc.

---

## UC9: Weight Measurement (Equality, Conversion & Addition)

## Description
Introduces a new `WeightUnit` enum and `QuantityWeight` class mirroring the UC8 length pattern. Supports equality, conversion, and addition for KILOGRAM, GRAM, and POUND. Weight and length are incompatible categories.

## Conversion Factors (base: KILOGRAM)
| Unit | Factor |
|------|--------|
| KILOGRAM | 1.0 |
| GRAM | 0.001 |
| POUND | 0.453592 |

## Key Concepts
- `WeightUnit` standalone enum with `convertToBaseUnit()` / `convertFromBaseUnit()`
- Category type safety: `Quantity(1.0, KG).equals(Quantity(1.0, FOOT))` → `false`
- Overloaded `add()`: implicit (first operand unit) and explicit (target unit)
- `hashCode()` overridden consistently with `equals()`

---

## UC10: Generic Quantity Class with IMeasurable Interface

## Description
Refactors `QuantityLength` and `QuantityWeight` into a single generic `Quantity<U extends IMeasurable>` class. Eliminates code duplication across categories using a common interface. All UC1–UC9 functionality preserved.

## Architecture
| Component | Responsibility |
|-----------|---------------|
| `IMeasurable` | Defines unit conversion contract |
| `LengthUnit` / `WeightUnit` | Implement `IMeasurable` with conversion factors |
| `Quantity<U>` | Handles equality, conversion, addition for any unit |
| `QuantityMeasurementApp` | Generic demonstration only |

## Key Concepts
- Bounded type parameter `<U extends IMeasurable>` for compile-time type safety
- Cross-category prevention via `unit.getClass()` comparison
- `equals()`, `convertTo()`, `add()` implemented once — reused for all categories
- Adding new categories requires ONLY a new enum implementing `IMeasurable`

---

## UC11: Volume Measurement (Litre, Millilitre, Gallon)

## Description
Adds a third measurement category — volume — by creating a `VolumeUnit` enum implementing `IMeasurable`. No changes to `Quantity<U>`, `QuantityMeasurementApp`, or existing tests required. Proves the UC10 architecture scales linearly.

## Conversion Factors (base: LITRE)
| Unit | Factor |
|------|--------|
| LITRE | 1.0 |
| MILLILITRE | 0.001 |
| GALLON | 3.78541 |

## Key Concepts
- Only a new enum needed to add a full measurement category
- Cross-category safety: `1.0 LITRE ≠ 1.0 KILOGRAM` and `1.0 LITRE ≠ 1.0 FOOT`
- All generic `Quantity<U>` operations work automatically

---

## UC12: Subtraction and Division Operations

## Description
Extends `Quantity<U>` with subtraction (returns `Quantity<U>`) and division (returns dimensionless `double`). Both operations support cross-unit arithmetic within the same category and maintain immutability.

## Operations
| Method | Returns | Notes |
|--------|---------|-------|
| `subtract(other)` | `Quantity<U>` | Result in first operand's unit |
| `subtract(other, targetUnit)` | `Quantity<U>` | Result in explicit unit |
| `divide(other)` | `double` | Dimensionless ratio |

## Key Concepts
- Subtraction is **non-commutative**: `A - B ≠ B - A`
- Division is **non-commutative**: `A ÷ B ≠ B ÷ A`
- Division by zero throws `ArithmeticException`
- Cross-category operations throw `IllegalArgumentException`

---

# UC13: Centralized Arithmetic Logic (DRY Refactoring)

## Description
Refactors UC12's `add()`, `subtract()`, and `divide()` to eliminate duplicated validation and conversion logic by introducing a centralized private helper method and an `ArithmeticOperation` enum. Public API is unchanged; all UC12 behavior preserved.

## Internal Architecture
| Component | Role |
|-----------|------|
| `ArithmeticOperation` enum | Dispatches ADD, SUBTRACT, DIVIDE via `compute(a, b)` |
| `validateArithmeticOperands()` | Centralized null, category, finiteness checks |
| `performBaseArithmetic()` | Converts to base unit → executes operation → returns result |


## Key Concepts
- All validation defined once → consistent errors across all operations

---

## UC14: Temperature Measurement (Non-Linear Units & Arithmetic Restrictions)

### Description
Extends the generic measurement system by introducing **temperature units**.  
Unlike other measurements, temperature conversion is **non-linear** and **does not support arithmetic operations** such as addition, subtraction, or division.

This UC proves the architecture can support special-case measurement categories while preserving backward compatibility.

---

### Flow
1. Input temperature value with unit (Celsius or Fahrenheit).
2. Validate unit and numeric value.
3. Convert both values to base unit (**Celsius**) for comparison.
4. Perform equality or conversion operations.
5. Block unsupported arithmetic operations → throw exception.

---

### Supported Units

| Unit | Base Unit | Conversion Formula |
|------|-----------|-------------------|
| CELSIUS | Celsius | C = C |
| FAHRENHEIT | Celsius | C = (F − 32) × 5/9 |

---

### Key Concepts
- Temperature uses **formula-based conversion**, not multiplication.
- Base unit normalization enables cross-unit equality.
- Arithmetic operations are **intentionally disabled**.
- Uses `UnsupportedOperationException` for unsupported operations.
- Demonstrates extensibility of `Quantity<U>` architecture.
- Backward compatibility preserved for Length, Weight, and Volume.

---

### Behavior Examples

#### Equality
- `0°C == 32°F` → true  
- `100°C == 212°F` → true  
- `-40°C == -40°F` → true  

#### Conversion
- `0°C → 32°F`  
- `50°C → 122°F`

---

### UC15 — N-Tier Architecture Refactor
**Description:**
Complete refactor of the application into a professional
N-Tier package structure with proper separation of concerns.

**Package Structure:**
```
com.apps.quantitymeasurement
├── controller/
├── service/
├── repository/
├── entity/
├── exception/
├── unit/
└── core/
```

**Key Changes:**
- Moved all classes into proper layer packages
- Added `QuantityMeasurementEntity` for data storage
- Added `QuantityDTO` for data transfer
- Introduced H2 embedded database
- Basic JDBC connection management

---
### UC16 — JDBC Database Integration
**Description:**
Introduces professional database persistence through
JDBC with H2 embedded database, connection pooling,
transaction management and comprehensive test coverage.

**Key Features:**
- `DatabaseConfig` — loads config from `application.properties`
- `ConnectionPool` — manages reusable JDBC connections
- `QuantityMeasurementDatabaseRepository` — full CRUD operations
- Transaction management (commit/rollback)
- SQL injection prevention via PreparedStatement
- Separate production and test databases
- Full unit and integration test coverage

**Repository Operations:**
| Method | Description |
|--------|-------------|
| `save()` | Persists entity to database |
| `getAllMeasurements()` | Retrieves all records |
| `findByOperation()` | Filter by operation type |
| `findByMeasurementType()` | Filter by measurement type |
| `getCount()` | Total record count |
| `deleteAll()` | Clear all records |
| `getPoolStatistics()` | Connection pool stats |
| `releaseResources()` | Shutdown pool |

**Database Schema:**
```sql
quantity_measurement_entity   ← main table
quantity_measurement_history  ← audit trail
```

**Switch Repository:**
```properties
# application.properties
app.repository.type=database  ← use H2 database
app.repository.type=cache     ← use in-memory cache
```

---
### UC17 – Spring Backend for Quantity Measurement

- Migrates the backend of the Quantity Measurement Application to a **Spring-based architecture** for enterprise-grade scalability and maintainability  
- Introduces **Spring Boot** to simplify project setup, dependency management, application configuration, and runtime execution  
- Applies core **Spring concepts** such as **Inversion of Control (IoC)** and **Dependency Injection (DI)** to manage components cleanly  
- Builds RESTful APIs using **Spring MVC** and **Spring Controllers** for quantity operations such as comparison, conversion, and arithmetic  
- Moves business rules into dedicated **Spring Services**, keeping controllers lightweight and focused on request handling  
- Establishes a structured backend design using layered architecture with controllers, services, and repositories  
- Integrates persistence more cleanly through **Spring Data / JPA concepts** for improved database interaction and future ORM support  
- Uses **Spring Scopes** and managed beans to control object lifecycle where appropriate  
- Adds application-level **logging** for request tracing, debugging, and operational visibility  
- Improves maintainability, modularity, and testability through Spring’s ecosystem and annotation-driven development  
- Provides a scalable backend foundation for authentication, frontend integration, and future deployment
-  
---
### UC18 – Google Authentication and User Management for Quantity Measurement

- Introduces secure **user authentication and authorization** for the Quantity Measurement Application  
- Integrates **Google Authentication** using **OAuth 2.0** for simplified and secure third-party login  
- Adds **user management** capabilities to support authenticated access to application features and personalized data handling  
- Uses **Spring Security** to protect application endpoints and enforce access control rules  
- Implements **JWT (JSON Web Token)** based authentication for stateless session handling in REST APIs  
- Combines **OAuth 2.0 + JWT** to authenticate users via Google and issue application-level secure tokens  
- Secures backend APIs so only authorized users can access protected quantity operations or persisted records  
- Improves user session management while maintaining scalability for frontend and API clients  
- Establishes a foundation for role-based access, account linking, and future identity provider integrations  
- Enhances the application’s production readiness with modern security practices and clean authentication flow

---
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

---

