package com.apps.quantitymeasurement;

public enum LengthUnit {
	
	// Base unit :Feet, Inches convert to feet 
	FEET(1.0), INCH(1.0 / 12.0);
	
	// Conversion factor
	private final double conversionFactor;

	//Constructor
    LengthUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    // Converting to base unit
    public double getConversionFactor() {
        return conversionFactor;
    }
}
