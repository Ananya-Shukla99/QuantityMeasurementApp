package com.app.quantitymeasurement.unit;

import java.util.logging.Logger;

/**
 * WeightUnit enum defines all supported weight units.
 * Base unit is GRAM.
 */
public enum WeightUnit implements IMeasurable {

    GRAM(1.0),
    KILOGRAM(1000.0),
    POUND(453.592);

    private static final Logger logger = Logger.getLogger(
            WeightUnit.class.getName()
    );

    // Conversion factor relative to base unit (GRAM)
    private final double conversionFactor;

    WeightUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    @Override
    public double convertToBaseUnit(double value) {
        return value * conversionFactor;
    }

    @Override
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / conversionFactor;
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
        for (WeightUnit unit : WeightUnit.values()) {
            if (unit.getUnitName().equalsIgnoreCase(unitName)) {
                return unit;
            }
        }
        logger.severe("Invalid weight unit: " + unitName);
        throw new IllegalArgumentException(
                "Invalid weight unit: " + unitName
        );
    }
}