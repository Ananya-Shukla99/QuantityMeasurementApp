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
