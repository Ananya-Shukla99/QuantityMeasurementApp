package com.apps.quantitymeasurement;

import java.util.Objects;

public class QuantityLength {

	// Attribute
	private final double value;
	private final LengthUnit unit;

	// Constructor
	public QuantityLength(double value, LengthUnit unit) {

		if (unit == null) {
			throw new IllegalArgumentException("Unit cannot be null");
		}

		if (!Double.isFinite(value))
			throw new IllegalArgumentException("Value must be finite");

		this.value = value;
		this.unit = unit;
	}

	// Method to convert the given length to base unit
	private double convertToBaseUnit() {
		return unit.convertToBaseUnit(value);
	}

	// Addition method with flexible targetUnit
	public QuantityLength add(QuantityLength other, LengthUnit targetUnit) {

		// Checking null operand
		if (other == null) {
			throw new IllegalArgumentException("Second operand cannot be null");
		}

		// Checking target unit
		if (targetUnit == null) {
			throw new IllegalArgumentException("Target unit cannot be null");
		}

		// Checking Finite value
		if (!Double.isFinite(this.value) || !Double.isFinite(other.value)) {
			throw new IllegalArgumentException("Values must be finite");
		}

		double sumBase = this.convertToBaseUnit() + other.convertToBaseUnit();
		double resultValue = targetUnit.convertFromBaseUnit(sumBase);

		return new QuantityLength(resultValue, targetUnit);
	}

	// Static method to convert to target type
	public static double convert(double value, LengthUnit source, LengthUnit target) {

		// Validation
		if (source == null || target == null) {
			throw new IllegalArgumentException("Unit cannot be null");
		}

		if (!Double.isFinite(value)) {
			throw new IllegalArgumentException("Value must be finite");
		}

		double baseValue = source.convertToBaseUnit(value);

		return target.convertFromBaseUnit(baseValue);
	}

	// Instance conversion method
	public QuantityLength convertTo(LengthUnit target) {

		if (target == null)
			throw new IllegalArgumentException("Target unit cannot be null");

		double baseValue = convertToBaseUnit();

		double convertedValue = target.convertFromBaseUnit(baseValue);

		return new QuantityLength(convertedValue, target);
	}

	// Overriding equals method to compare two QuantityLength objects
	@Override
	public boolean equals(Object obj) {

		// Checking same reference - Reflexive property
		if (this == obj)
			return true;

		// Checking null and class type
		if (obj == null || getClass() != obj.getClass())
			return false;

		if (!(obj instanceof QuantityLength))
			return false;

		// Type casting
		QuantityLength other = (QuantityLength) obj;

		// Comparing values after converting to base unit
		double difference = Math.abs(this.convertToBaseUnit() - other.convertToBaseUnit());

		return difference < 0.0001;
	}

	// Overriding hashCode method - consistent with equals
	@Override
	public int hashCode() {

		return Objects.hash(Math.round(convertToBaseUnit() * 10000));
	}

	// Overriding toString method for readable output
	@Override
	public String toString() {
		return "Quantity(" + value + ", " + unit + ")";
	}

}
