package com.app.quantitymeasurement.unit;

import java.util.function.Function;
import java.util.logging.Logger;

/**
 * TemperatureUnit enum defines all supported temperature units.
 * Base unit is CELSIUS.
 * Does NOT support arithmetic operations.
 */
public enum TemperatureUnit implements IMeasurable {

    CELSIUS(c -> c, c -> c),
    FAHRENHEIT(f -> (f - 32) * 5.0 / 9.0, c -> (c * 9.0 / 5.0) + 32),
    KELVIN(k -> k - 273.15, c -> c + 273.15);

    private static final Logger logger = Logger.getLogger(
            TemperatureUnit.class.getName()
    );

    private final Function<Double, Double> toCelsius;
    private final Function<Double, Double> fromCelsius;

    TemperatureUnit(Function<Double, Double> toCelsius,
                    Function<Double, Double> fromCelsius) {
        this.toCelsius   = toCelsius;
        this.fromCelsius = fromCelsius;
    }

    @Override
    public double convertToBaseUnit(double value) {
        return toCelsius.apply(value);
    }

    @Override
    public double convertFromBaseUnit(double baseValue) {
        return fromCelsius.apply(baseValue);
    }

    @Override
    public boolean supportsArithmetic() {
        return false;
    }

    @Override
    public void validateOperationSupport(String operation) {
        logger.severe("Temperature does not support: " + operation);
        throw new UnsupportedOperationException(
                "Temperature does not support arithmetic operation: "
                + operation
        );
    }

    @Override
    public String getUnitName() {
        return this.name();
    }

    @Override
    public String getMeasurementType() {
        return this.getClass().getSimpleName();
    }

    @Override
    public IMeasurable getUnitInstance(String unitName) {
        for (TemperatureUnit unit : TemperatureUnit.values()) {
            if (unit.getUnitName().equalsIgnoreCase(unitName)) {
                return unit;
            }
        }
        logger.severe("Invalid temperature unit: " + unitName);
        throw new IllegalArgumentException(
                "Invalid temperature unit: " + unitName
        );
    }
}