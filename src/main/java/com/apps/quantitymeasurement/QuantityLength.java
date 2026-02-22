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

	// Method to convert the given length to base unit (Inches)
    private double convertToBaseUnit() {
        return this.value * unit.getConversionFactor();
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

        // Type casting
        QuantityLength other = (QuantityLength) obj;

        // Comparing values after converting to base unit
        double difference = Math.abs(
                this.convertToBaseUnit() - other.convertToBaseUnit()
        );

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
