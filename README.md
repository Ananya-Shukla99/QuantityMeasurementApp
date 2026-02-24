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
