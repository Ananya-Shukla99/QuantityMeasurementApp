package com.app.qma.service;

import com.app.qma.unit.IMeasurable;

public class QuantityValue<U extends IMeasurable> {

    private static final double ROUNDING_FACTOR = 100.0;

    private final double value;
    private final U unit;

    public QuantityValue(double value, U unit) {
        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public U getUnit() {
        return unit;
    }

    public QuantityValue<U> convertTo(U targetUnit) {
        double base = unit.convertToBaseUnit(value);
        return new QuantityValue<>(round(targetUnit.convertFromBaseUnit(base)), targetUnit);
    }

    public QuantityValue<U> add(QuantityValue<U> other, U resultUnit) {
        unit.validateOperationSupport("ADD");
        double sumBase = unit.convertToBaseUnit(value) + other.unit.convertToBaseUnit(other.value);
        return new QuantityValue<>(round(resultUnit.convertFromBaseUnit(sumBase)), resultUnit);
    }

    public QuantityValue<U> subtract(QuantityValue<U> other, U resultUnit) {
        unit.validateOperationSupport("SUBTRACT");
        double subBase = unit.convertToBaseUnit(value) - other.unit.convertToBaseUnit(other.value);
        return new QuantityValue<>(round(resultUnit.convertFromBaseUnit(subBase)), resultUnit);
    }

    public double divide(QuantityValue<U> other) {
        unit.validateOperationSupport("DIVIDE");
        double divisor = other.unit.convertToBaseUnit(other.value);
        if (divisor == 0) {
            throw new ArithmeticException("Division by zero");
        }
        double result = unit.convertToBaseUnit(value) / divisor;
        return round(result);
    }

    public boolean equivalentTo(QuantityValue<U> other) {
        double a = round(unit.convertToBaseUnit(value));
        double b = round(other.unit.convertToBaseUnit(other.value));
        return Double.compare(a, b) == 0;
    }

    private double round(double input) {
        return Math.round(input * ROUNDING_FACTOR) / ROUNDING_FACTOR;
    }
}

