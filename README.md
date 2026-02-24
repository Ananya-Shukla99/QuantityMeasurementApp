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

## Two Enum Styles Supported
- **Abstract method**: Each constant overrides `compute()` — clean for complex logic
- **Lambda (`DoubleBinaryOperator`)**: Concise, modern functional style

## Key Concepts
- All validation defined once → consistent errors across all operations
- Adding future operations (MULTIPLY, MODULO) requires only a new enum constant
- Private helpers reduce each public method to 2–3 lines
- All UC12 tests pass without modification

---
