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
