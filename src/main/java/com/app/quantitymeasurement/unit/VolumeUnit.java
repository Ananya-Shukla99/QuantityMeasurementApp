package com.app.quantitymeasurement.unit;

import java.util.logging.Logger;

/**
 * VolumeUnit enum defines all supported volume units.
 * Base unit is LITRE.
 */
public enum VolumeUnit implements IMeasurable {

    LITRE(1.0),
    MILLILITRE(0.001),
    GALLON(3.78541);

    private static final Logger logger = Logger.getLogger(
            VolumeUnit.class.getName()
    );

    // Conversion factor relative to base unit (LITRE)
    private final double conversionFactor;

    VolumeUnit(double conversionFactor) {
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
        for (VolumeUnit unit : VolumeUnit.values()) {
            if (unit.getUnitName().equalsIgnoreCase(unitName)) {
                return unit;
            }
        }
        logger.severe("Invalid volume unit: " + unitName);
        throw new IllegalArgumentException(
                "Invalid volume unit: " + unitName
        );
    }
}