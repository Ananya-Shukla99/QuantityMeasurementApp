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
