package com.apps.quantitymeasurement;

import java.util.Objects;

public final class QuantityWeight {

    // Constant for floating point comparison precision
    private static final double EPSILON = 1e-6;

    // Attribute
    private final double value;
    private final WeightUnit unit;

    // Constructor
    public QuantityWeight(double value, WeightUnit unit) {

        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }

        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be finite");
        }

        this.value = value;
        this.unit = unit;
    }

    // Method to convert the given weight to base unit (kilogram)
    private double convertToBaseUnit() {
        return unit.convertToBaseUnit(value);
    }

    // Addition method with flexible targetUnit
    public QuantityWeight add(QuantityWeight other, WeightUnit targetUnit) {

        // Checking null operand
        if (other == null) {
            throw new IllegalArgumentException("Second operand cannot be null");
        }

        // Checking target unit
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }

        // Checking finite values
        if (!Double.isFinite(this.value) || !Double.isFinite(other.value)) {
            throw new IllegalArgumentException("Values must be finite");
        }

        double sumBase = this.convertToBaseUnit() + other.convertToBaseUnit();
        double resultValue = targetUnit.convertFromBaseUnit(sumBase);

        return new QuantityWeight(resultValue, targetUnit);
    }

    // Addition method - result in first operand's unit
    public QuantityWeight add(QuantityWeight other) {
        return add(other, this.unit);
    }

    // Static method to convert weight to target unit
    public static double convert(double value, WeightUnit source, WeightUnit target) {

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
    public QuantityWeight convertTo(WeightUnit target) {

        if (target == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }

        double baseValue = convertToBaseUnit();
        
        double convertedValue = target.convertFromBaseUnit(baseValue);

        return new QuantityWeight(convertedValue, target);
    }

    // Overriding equals method to compare two QuantityWeight objects
    @Override
    public boolean equals(Object obj) {

        // Checking same reference - Reflexive property
        if (this == obj)
            return true;

        // Checking null and class type (ensures category safety)
        if (obj == null || getClass() != obj.getClass())
            return false;

        // Type casting
        QuantityWeight other = (QuantityWeight) obj;

        // Comparing values after converting to base unit (kilogram)
        double difference = Math.abs(this.convertToBaseUnit() - other.convertToBaseUnit());

        return difference < EPSILON;
    }

    // Overriding hashCode method - consistent with equals
    @Override
    public int hashCode() {
        return Objects.hash(Math.round(convertToBaseUnit() * 1_000_000));
    }

    // Overriding toString method for readable output
    @Override
    public String toString() {
        return "QuantityWeight(" + value + ", " + unit + ")";
    }

	public WeightUnit getUnit() {
		
		return unit;
	}

	public Double getValue() {
		return value;
	}
}
