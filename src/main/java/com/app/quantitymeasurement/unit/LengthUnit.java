package com.app.quantitymeasurement.unit;

import java.util.logging.Logger;

/**
 * LengthUnit enum defines all supported length units.
 * Base unit is INCH.
 */
public enum LengthUnit implements IMeasurable {

    INCH(1.0),
    FEET(12.0),
    YARDS(36.0),
    CENTIMETERS(0.393701);

    private static final Logger logger = Logger.getLogger(
            LengthUnit.class.getName()
    );

    // Conversion factor relative to base unit (INCH)
    private final double conversionFactor;

    LengthUnit(double conversionFactor) {
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
        for (LengthUnit unit : LengthUnit.values()) {
            if (unit.getUnitName().equalsIgnoreCase(unitName)) {
                return unit;
            }
        }
        logger.severe("Invalid length unit: " + unitName);
        throw new IllegalArgumentException(
                "Invalid length unit: " + unitName
        );
    }
}