package com.apps.QuantityMeasurementApp;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.apps.quantitymeasurement.Feet;
import com.apps.quantitymeasurement.QuantityMeasurementApp;

//Test Class
public class FeetMeasurementTesting {

	// Checking same value
	@Test
	public void testEquality_SameValue() {
		boolean result = QuantityMeasurementApp.areFeetEqual(0.1, 0.1);

		assertEquals(true, result);
	}

	// Checking different values
	@Test
	public void testEquality_DifferentValue() {

		boolean result = QuantityMeasurementApp.areFeetEqual(0.2, 0.1);

		assertEquals(false, result);
	}

	// Checking the null value comparison
	@Test
	public void testEquality_NullComparison() {

		Feet f1 = new Feet(0.1);

		assertEquals(false, f1.equals(null));

	}

	// Checking non numeric value
	@Test
	public void testEquality_NonNumericInput() {

		Feet f1 = new Feet(0.1);

		assertEquals(false, f1.equals("Hello"));

	}

	// Checking same reference
	@Test
	public void testEquality_SameReference() {

		Feet f = new Feet(0.1);

		assertEquals(true, f.equals(f));
	}
}
