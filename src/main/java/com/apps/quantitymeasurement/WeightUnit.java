package com.apps.quantitymeasurement;

public enum WeightUnit {

	GRAM(1.0), KILOGRAM(1000.0), POUND(453.592);

	// relative to gram conversion factor
	private final double conversionFactorToBase;

	// constructor
	WeightUnit(double conversionFactorToBase) {
		this.conversionFactorToBase = conversionFactorToBase;
	}

	// Getter for conversion factor
	public double getConversionFactor() {
		return conversionFactorToBase;
	}

	// Convert value to base unit (gram)
	public double convertToBaseUnit(double value) {
		return value * conversionFactorToBase;
	}

	// Convert from base unit (gram)
	public double convertFromBaseUnit(double baseValue) {
		return baseValue / conversionFactorToBase;
	}
}