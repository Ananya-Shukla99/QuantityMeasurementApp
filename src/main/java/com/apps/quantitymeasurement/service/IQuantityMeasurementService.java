package com.apps.quantitymeasurement.service;

import com.apps.quantitymeasurement.QuantityDTO;

public interface IQuantityMeasurementService {

	QuantityDTO convert(QuantityDTO quantity, String targetUnit);

	boolean compare(QuantityDTO quantity1, QuantityDTO quantity2);

	QuantityDTO add(QuantityDTO quantity1, QuantityDTO quantity2);

	QuantityDTO add(QuantityDTO quantity1, QuantityDTO quantity2, String targetUnit);

	QuantityDTO subtract(QuantityDTO quantity1, QuantityDTO quantity2);

	double divide(QuantityDTO quantity1, QuantityDTO quantity2);
}