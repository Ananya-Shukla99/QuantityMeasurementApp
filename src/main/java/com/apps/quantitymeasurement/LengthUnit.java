package com.apps.quantitymeasurement;

public enum LengthUnit {

	INCH(1.0),
    FEET(12.0),
    YARDS(36.0),
    CENTIMETERS(1.0 / 2.54);   // 1 cm = 0.393701 inch

    // Conversion factor relative to base unit (inch)
    private final double conversionFactorToBase;

    //constructor 
    LengthUnit(double conversionFactorToBase) {
        this.conversionFactorToBase = conversionFactorToBase;
    }

    // Convert value in this unit → base unit (inch)
    public double convertToBaseUnit(double value) {
        return value * conversionFactorToBase;
    }

    // Convert value from base unit (inch) → this unit
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / conversionFactorToBase;
    }

    // Getter for conversion factor
    public double getConversionFactor() {
        return conversionFactorToBase;
    }
}
