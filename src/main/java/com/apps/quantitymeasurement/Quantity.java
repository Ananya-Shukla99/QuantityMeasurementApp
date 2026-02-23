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
}