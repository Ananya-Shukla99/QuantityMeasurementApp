package com.app.qma.unit;

public enum TemperatureUnit implements IMeasurable {
    CELSIUS,
    FAHRENHEIT,
    KELVIN;

    @Override
    public double convertToBaseUnit(double value) {
        return switch (this) {
            case CELSIUS -> value;
            case FAHRENHEIT -> (value - 32) * 5.0 / 9.0;
            case KELVIN -> value - 273.15;
        };
    }

    @Override
    public double convertFromBaseUnit(double baseValue) {
        return switch (this) {
            case CELSIUS -> baseValue;
            case FAHRENHEIT -> (baseValue * 9.0 / 5.0) + 32;
            case KELVIN -> baseValue + 273.15;
        };
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
        return TemperatureUnit.valueOf(unitName.toUpperCase());
    }

    @Override
    public void validateOperationSupport(String operation) {
        throw new UnsupportedOperationException("Temperature does not support arithmetic operation: " + operation);
    }
}

