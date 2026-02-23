package com.apps.quantitymeasurement;

public enum LengthUnit {

	INCH(1.0), 
	FEET(12.0), 
	YARDS(36.0), 
	CENTIMETERS(0.393701);

	private final double conversionFactor;

	LengthUnit(double conversionFactor) {
		this.conversionFactor = conversionFactor;
	}

	// Returns conversion factor relative to base unit
	public double getConversionFactor() {
		return conversionFactor;
	}

	// Convert given value to base unit 
	public double convertToBaseUnit(double value) {
		return value * conversionFactor;
	}

	// Convert from base unit to current unit
	public double convertFromBaseUnit(double baseValue) {
		return baseValue / conversionFactor;
	}

}
