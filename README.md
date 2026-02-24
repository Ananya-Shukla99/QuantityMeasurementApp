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
