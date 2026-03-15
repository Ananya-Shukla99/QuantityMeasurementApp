package com.app.quantitymeasurement.service;

import com.app.quantitymeasurement.entity.QuantityDTO;

/**
 * IQuantityMeasurementService defines the contract for all quantity measurement
 * operations.
 */
public interface IQuantityMeasurementService {

	// CONVERT
	QuantityDTO convert(QuantityDTO quantity, String targetUnit);

	// COMPARE
	boolean compare(QuantityDTO quantity1, QuantityDTO quantity2);

	// ADD
	QuantityDTO add(QuantityDTO quantity1, QuantityDTO quantity2);

	QuantityDTO add(QuantityDTO quantity1, QuantityDTO quantity2, String targetUnit);

	// SUBTRACT
	QuantityDTO subtract(QuantityDTO quantity1, QuantityDTO quantity2);

	// DIVIDE
	double divide(QuantityDTO quantity1, QuantityDTO quantity2);
}