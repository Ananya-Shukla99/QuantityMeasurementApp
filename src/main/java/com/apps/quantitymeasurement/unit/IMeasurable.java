package com.apps.quantitymeasurement.unit;

public interface IMeasurable {

	double convertToBaseUnit(double value);

	double convertFromBaseUnit(double baseValue);

	String getUnitName();

	/*
	 * This is essential for ensuring that comparisons and conversions are only
	 * performed between compatible types.
	 */
	String getMeasurementType();

	// This method is essential for converting QuantityDTO to IMeasurable units.
	IMeasurable getUnitInstance(String unitName);

	// default lambda → arithmetic supported
	default boolean supportsArithmetic() {
		return true;
	}

	// default validation
	default void validateOperationSupport(String operation) {
		// default: supported
	}

}