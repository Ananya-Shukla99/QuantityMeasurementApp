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
