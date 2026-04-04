package com.app.qma.unit;

public enum WeightUnit implements IMeasurable {
    GRAM(1.0),
    KILOGRAM(1000.0),
    POUND(453.592);

    private final double factor;

    WeightUnit(double factor) {
        this.factor = factor;
    }

    @Override
    public double convertToBaseUnit(double value) {
        return value * factor;
    }

    @Override
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / factor;
    }

    @Override
    public String getUnitName() {
        return name();
    }

    @Override
    public String getMeasurementType() {
        return getClass().getSimpleName();
    }

    @Override
    public IMeasurable getUnitInstance(String unitName) {
        return WeightUnit.valueOf(unitName.toUpperCase());
    }
}

