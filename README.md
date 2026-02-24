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
