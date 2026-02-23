package com.apps.quantitymeasurement;

import java.util.Objects;

public class Quantity<U extends IMeasurable> {

	// Constant for floating point rounding precision (2 decimal places)
	private static final double ROUNDING_FACTOR = 100.0;

	// Attributes
	private final double value;
	private final U unit;

	// Constructor
	public Quantity(double value, U unit) {

		if (unit == null)
			throw new IllegalArgumentException("Unit cannot be null");

		if (!Double.isFinite(value))
			throw new IllegalArgumentException("Invalid value");

		this.value = value;
		this.unit = unit;
	}

	// Arithmetic Operation Enum (UC13)

	private enum ArithmeticOperation {

		ADD {
			double compute(double a, double b) {
				return a + b;
			}
		},
		SUBTRACT {
			double compute(double a, double b) {
				return a - b;
			}
		},
		DIVIDE {
			double compute(double a, double b) {
				if (b == 0)
					throw new ArithmeticException("Division by zero");
				return a / b;
			}
		};

		abstract double compute(double a, double b);
	}

	// Getters

	public double getValue() {
		return value;
	}

	public U getUnit() {
		return unit;
	}

	// Centralized Validation (UC13)
	private void validateArithmeticOperands(Quantity<U> other, U targetUnit, boolean targetUnitRequired) {

		if (other == null)
			throw new IllegalArgumentException("Operand quantity cannot be null");

		if (!unit.getClass().equals(other.unit.getClass()))
			throw new IllegalArgumentException("Incompatible measurement categories");

		if (!Double.isFinite(this.value) || !Double.isFinite(other.value))
			throw new IllegalArgumentException("Values must be finite numbers");

		if (targetUnitRequired && targetUnit == null)
			throw new IllegalArgumentException("Target unit cannot be null");
	}

	// Core Arithmetic Helper (UC13)
	private double performBaseArithmetic(Quantity<U> other, ArithmeticOperation operation) {

		double baseValue1 = unit.convertToBaseUnit(value);
		double baseValue2 = other.unit.convertToBaseUnit(other.value);

		return operation.compute(baseValue1, baseValue2);
	}

	// Conversion
	public Quantity<U> convertTo(U targetUnit) {

		if (targetUnit == null)
			throw new IllegalArgumentException("Target unit cannot be null");

		if (this.unit.getClass() != targetUnit.getClass())
			throw new IllegalArgumentException("Incompatible unit types");

		double baseValue = unit.convertToBaseUnit(value);
		double converted = targetUnit.convertFromBaseUnit(baseValue);

		return new Quantity<>(round(converted), targetUnit);
	}
	 
	// ADD
	public Quantity<U> add(Quantity<U> other) {

		validateArithmeticOperands(other, null, false);

		double baseResult = performBaseArithmetic(other, ArithmeticOperation.ADD);
		double result = unit.convertFromBaseUnit(baseResult);

		return new Quantity<>(round(result), unit);
	}

	public Quantity<U> add(Quantity<U> other, U targetUnit) {

		validateArithmeticOperands(other, targetUnit, true);

		double baseResult = performBaseArithmetic(other, ArithmeticOperation.ADD);
		double result = targetUnit.convertFromBaseUnit(baseResult);

		return new Quantity<>(round(result), targetUnit);
	}

	// SUBTRACT
	public Quantity<U> subtract(Quantity<U> other) {

		validateArithmeticOperands(other, null, false);

		double baseResult = performBaseArithmetic(other, ArithmeticOperation.SUBTRACT);
		double result = unit.convertFromBaseUnit(baseResult);

		return new Quantity<>(round(result), unit);
	}

	public Quantity<U> subtract(Quantity<U> other, U targetUnit) {

		validateArithmeticOperands(other, targetUnit, true);

		double baseResult = performBaseArithmetic(other, ArithmeticOperation.SUBTRACT);
		double result = targetUnit.convertFromBaseUnit(baseResult);

		return new Quantity<>(round(result), targetUnit);
	}

	// DIVIDE
	public double divide(Quantity<U> other) {

		validateArithmeticOperands(other, null, false);

		double result = performBaseArithmetic(other, ArithmeticOperation.DIVIDE);

		return round(result);
	}

	// equals & hashCode
	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;

		if (obj == null || getClass() != obj.getClass())
			return false;

		Quantity<?> that = (Quantity<?>) obj;

		if (this.unit.getClass() != that.unit.getClass())
			return false;

		double thisBase = this.unit.convertToBaseUnit(this.value);
		double thatBase = that.unit.convertToBaseUnit(that.value);

		return Double.compare(round(thisBase), round(thatBase)) == 0;
	}

	@Override
	public int hashCode() {

		double baseValue = round(unit.convertToBaseUnit(value));

		return Objects.hash(baseValue, unit.getClass());
	}

	@Override
	public String toString() {
		return value + " " + unit.getUnitName();
	}

	// Rounding Helper
	private double round(double value) {
		return Math.round(value * ROUNDING_FACTOR) / ROUNDING_FACTOR;
	}
}