package com.apps.quantitymeasurement;

public interface IMeasurable {

	double convertToBaseUnit(double value);

	double convertFromBaseUnit(double baseValue);

	String getUnitName();

	// default lambda → arithmetic supported
	default boolean supportsArithmetic() {
		return true;
	}

	// default validation
	default void validateOperationSupport(String operation) {
		// default: supported
	}

}