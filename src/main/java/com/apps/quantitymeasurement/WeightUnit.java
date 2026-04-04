package com.apps.quantitymeasurement;

public enum WeightUnit implements IMeasurable {

	GRAM(1.0), KILOGRAM(1000.0), POUND(453.592);

	// relative to gram conversion factor
	private final double conversionFactor;

	// constructor
	WeightUnit(double conversionFactor) {
		this.conversionFactor = conversionFactor;
	}

	@Override
	public double getConversionFactor() {
		return conversionFactor;
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
}