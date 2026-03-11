package com.apps.quantitymeasurement.controller;

import com.apps.quantitymeasurement.QuantityDTO;
import com.apps.quantitymeasurement.QuantityMeasurementException;
import com.apps.quantitymeasurement.service.IQuantityMeasurementService;

public class QuantityMeasurementController {

	// Service dependency — injected via constructor (DIP)
	private final IQuantityMeasurementService service;

	// Common display utility
	private static final String SEPARATOR = " : ";

	/**
	 * Constructor injection of the service dependency. 
	 */
	public QuantityMeasurementController(IQuantityMeasurementService service) {
		this.service = service;
	}

	// COMPARE

	/**
	 * Performs an equality comparison between two quantities. Delegates to the
	 */
	public boolean performComparison(QuantityDTO q1, QuantityDTO q2) {
		try {
			boolean result = service.compare(q1, q2);
			displayResult("Equality Check (" + q1 + ", " + q2 + ")", result);
			return result;
		} catch (QuantityMeasurementException e) {
			System.out.println("Comparison Error: " + e.getMessage());
			return false;
		}
	}

	// CONVERT

	/**
	 * Performs a unit conversion on a given quantity. Delegates to the service
	 * layer for business logic.
	 */
	public QuantityDTO performConversion(QuantityDTO quantity, String targetUnit) {
		try {
			QuantityDTO result = service.convert(quantity, targetUnit);
			displayResult(quantity + " converted to " + targetUnit, result);
			return result;
		} catch (QuantityMeasurementException e) {
			System.out.println("Conversion Error: " + e.getMessage());
			return null;
		}
	}

	// ADD

	/**
	 * Performs addition of two quantities, returning result in the unit of the
	 * first operand.
	 */
	public QuantityDTO performAddition(QuantityDTO q1, QuantityDTO q2) {
		try {
			QuantityDTO result = service.add(q1, q2);
			displayResult("Addition (" + q1 + " + " + q2 + ")", result);
			return result;
		} catch (QuantityMeasurementException e) {
			System.out.println("Addition Error: " + e.getMessage());
			return null;
		}
	}

	/**
	 * Performs addition of two quantities, returning result in the specified target
	 * unit.
	 */
	public QuantityDTO performAddition(QuantityDTO q1, QuantityDTO q2, String targetUnit) {
		try {
			QuantityDTO result = service.add(q1, q2, targetUnit);
			displayResult("Addition (" + q1 + " + " + q2 + ") in " + targetUnit, result);
			return result;
		} catch (QuantityMeasurementException e) {
			System.out.println("Addition Error: " + e.getMessage());
			return null;
		}
	}

	// SUBTRACT

	/**
	 * Performs subtraction of the second quantity from the first.
	 */
	public QuantityDTO performSubtraction(QuantityDTO q1, QuantityDTO q2) {
		try {
			QuantityDTO result = service.subtract(q1, q2);
			displayResult("Subtraction (" + q1 + " - " + q2 + ")", result);
			return result;
		} catch (QuantityMeasurementException e) {
			System.out.println("Subtraction Error: " + e.getMessage());
			return null;
		}
	}

	// DIVIDE

	/**
	 * Performs division of the first quantity by the second.
	 */
	public double performDivision(QuantityDTO q1, QuantityDTO q2) {
		try {
			double result = service.divide(q1, q2);
			displayResult("Division (" + q1 + " / " + q2 + ")", result);
			return result;
		} catch (QuantityMeasurementException e) {
			System.out.println("Division Error: " + e.getMessage());
			return 0.0;
		}
	}

	// DISPLAY UTILITY

	/**
	 * Common display utility for printing operation results to console.
	 */
	private void displayResult(String operation, Object result) {
		System.out.println(operation + SEPARATOR + result);
	}
}