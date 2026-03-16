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
