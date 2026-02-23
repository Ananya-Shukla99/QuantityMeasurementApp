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

		// Checking null unit
		if (unit == null)
			throw new IllegalArgumentException("Unit cannot be null");

		// Checking finite value
		if (!Double.isFinite(value))
			throw new IllegalArgumentException("Invalid value");

		this.value = value;
		this.unit = unit;
	}

	// Getter for value
	public double getValue() {
		return value;
	}

	// Getter for unit
	public U getUnit() {
		return unit;
	}

	// Method to convert current quantity to target unit
	public Quantity<U> convertTo(U targetUnit) {

		// Checking null target unit
		if (targetUnit == null)
			throw new IllegalArgumentException("Target unit cannot be null");

		// Ensuring both units belong to same measurement category
		if (this.unit.getClass() != targetUnit.getClass())
			throw new IllegalArgumentException("Incompatible unit types");

		// Convert current value to base unit
		double baseValue = unit.convertToBaseUnit(value);

		// Convert base value to target unit
		double converted = targetUnit.convertFromBaseUnit(baseValue);

		return new Quantity<>(round(converted), targetUnit);
	}

	// Addition method - result in first operand's unit
	public Quantity<U> add(Quantity<U> other) {
		return add(other, this.unit);
	}

	// Addition method with explicit target unit
	public Quantity<U> add(Quantity<U> other, U targetUnit) {

		// Checking null operand
		if (other == null)
			throw new IllegalArgumentException("Cannot add null quantity");

		// Ensuring both quantities belong to same measurement category
		if (this.unit.getClass() != other.unit.getClass())
			throw new IllegalArgumentException("Incompatible measurement categories");

		// Convert both quantities to base unit
		double base1 = this.unit.convertToBaseUnit(this.value);
		double base2 = other.unit.convertToBaseUnit(other.value);

		// Add in base unit
		double sumBase = base1 + base2;

		// Convert result back to target unit
		double result = targetUnit.convertFromBaseUnit(sumBase);

		return new Quantity<>(round(result), targetUnit);
	}

	// Overriding equals method to compare two Quantity objects
	@Override
	public boolean equals(Object obj) {

		// Checking same reference - Reflexive property
		if (this == obj)
			return true;

		// Checking null and class type (ensures category safety)
		if (obj == null || getClass() != obj.getClass())
			return false;

		Quantity<?> that = (Quantity<?>) obj;

		// Ensuring both belong to same measurement category
		if (this.unit.getClass() != that.unit.getClass())
			return false;

		// Convert both values to base unit
		double thisBase = this.unit.convertToBaseUnit(this.value);
		double thatBase = that.unit.convertToBaseUnit(that.value);

		// Compare after rounding to avoid floating point precision issues
		return Double.compare(round(thisBase), round(thatBase)) == 0;
	}

	// Subtracts another quantity from this quantity
	public Quantity<U> subtract(Quantity<U> other) {
		return subtract(other, this.unit);
	}

	// Subtraction method with explicit target unit
	public Quantity<U> subtract(Quantity<U> other, U targetUnit) {

		// Checking null operand
		if (other == null)
			throw new IllegalArgumentException("Cannot subtract null quantity");

		// Checking null target unit
		if (targetUnit == null)
			throw new IllegalArgumentException("Target unit cannot be null");

		// Ensuring both quantities belong to same measurement category
		if (this.unit.getClass() != other.unit.getClass())
			throw new IllegalArgumentException("Incompatible measurement categories");

		// Checking finite values
		if (!Double.isFinite(this.value) || !Double.isFinite(other.value))
			throw new IllegalArgumentException("Invalid numeric value");

		// Convert both quantities to base unit
		double base1 = this.unit.convertToBaseUnit(this.value);
		double base2 = other.unit.convertToBaseUnit(other.value);

		// Subtract in base unit
		double differenceBase = base1 - base2;

		// Convert result back to target unit
		double result = targetUnit.convertFromBaseUnit(differenceBase);

		return new Quantity<>(round(result), targetUnit);
	}

	// Division method - returns dimensionless ratio
	public double divide(Quantity<U> other) {

		// Checking null operand
		if (other == null)
			throw new IllegalArgumentException("Cannot divide by null quantity");

		// Ensuring both quantities belong to same measurement category
		if (this.unit.getClass() != other.unit.getClass())
			throw new IllegalArgumentException("Incompatible measurement categories");

		// Checking finite values
		if (!Double.isFinite(this.value) || !Double.isFinite(other.value))
			throw new IllegalArgumentException("Invalid numeric value");

		// Convert both quantities to base unit
		double base1 = this.unit.convertToBaseUnit(this.value);
		double base2 = other.unit.convertToBaseUnit(other.value);

		// Prevent division by zero
		if (base2 == 0)
			throw new ArithmeticException("Division by zero");

		// Return dimension less result
		return round(base1 / base2);
	}

	// Overriding hashCode method - consistent with equals
	@Override
	public int hashCode() {

		// Hashing based on rounded base unit value and measurement category
		double baseValue = round(unit.convertToBaseUnit(value));

		return Objects.hash(baseValue, unit.getClass());
	}

	// Overriding toString method for readable output
	@Override
	public String toString() {
		return value + " " + unit.getUnitName();
	}

	// Private helper method to round values to 2 decimal places
	private double round(double value) {
		return Math.round(value * ROUNDING_FACTOR) / ROUNDING_FACTOR;
	}

	public Object divide(Object other) {
		return divide(other, this.unit);
	}

	private Object divide(Object other, U unit2) {
		return null;
	}

	public Object subtract(Object other) {
		return null;
	}
}