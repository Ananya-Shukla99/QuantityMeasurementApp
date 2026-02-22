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

		this.value = value;
		this.unit = unit;
	}

	// method convert to base unit
	private double convertToBaseUnit() {
		return this.value * unit.getConversionFactor();
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;

		if (obj == null || getClass() != obj.getClass())
			return false;

		QuantityLength other = (QuantityLength) obj;

		return Double.compare(this.convertToBaseUnit(), other.convertToBaseUnit()) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(convertToBaseUnit());
	}

	@Override
	public String toString() {
		return "Quantity(" + value + ", " + unit + ")";
	}
}
