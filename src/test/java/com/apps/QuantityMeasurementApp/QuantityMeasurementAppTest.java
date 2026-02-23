package com.apps.QuantityMeasurementApp;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.apps.quantitymeasurement.LengthUnit;
import com.apps.quantitymeasurement.QuantityLength;

// Testing Class
public class QuantityMeasurementAppTest {

	private static final double DELTA = 1e-3;

	// Enum Constant Validation Tests
	@Test
	void testLengthUnitEnum_InchConstant() {
		assertEquals(1.0, LengthUnit.INCH.getConversionFactor(), DELTA);
	}

	@Test
	void testLengthUnitEnum_FeetConstant() {
		assertEquals(12.0, LengthUnit.FEET.getConversionFactor(), DELTA);
	}

	@Test
	void testLengthUnitEnum_YardsConstant() {
		assertEquals(36.0, LengthUnit.YARDS.getConversionFactor(), DELTA);
	}

	@Test
	void testLengthUnitEnum_CentimetersConstant() {
		assertEquals(1.0 / 2.54, LengthUnit.CENTIMETERS.getConversionFactor(), DELTA);
	}

	// Convert To Base Unit (INCH)

	@Test
	void testConvertToBaseUnit_FeetToInch() {
		assertEquals(60.0, LengthUnit.FEET.convertToBaseUnit(5.0), DELTA);
	}

	@Test
	void testConvertToBaseUnit_YardsToInch() {
		assertEquals(36.0, LengthUnit.YARDS.convertToBaseUnit(1.0), DELTA);
	}

	@Test
	void testConvertToBaseUnit_CentimetersToInch() {
		assertEquals(1.0, LengthUnit.CENTIMETERS.convertToBaseUnit(2.54), DELTA);
	}

	// Convert From Base Unit (INCH)

	@Test
	void testConvertFromBaseUnit_InchToFeet() {
		assertEquals(1.0, LengthUnit.FEET.convertFromBaseUnit(12.0), DELTA);
	}

	@Test
	void testConvertFromBaseUnit_InchToYards() {
		assertEquals(1.0, LengthUnit.YARDS.convertFromBaseUnit(36.0), DELTA);
	}

	@Test
	void testConvertFromBaseUnit_InchToCentimeters() {
		assertEquals(2.54, LengthUnit.CENTIMETERS.convertFromBaseUnit(1.0), DELTA);
	}

	// QuantityLength Tests

	@Test
	void testQuantityLengthRefactored_Equality() {
		assertEquals(new QuantityLength(1.0, LengthUnit.FEET), new QuantityLength(12.0, LengthUnit.INCH));
	}

	@Test
	void testQuantityLengthRefactored_ConvertTo() {
		QuantityLength result = new QuantityLength(1.0, LengthUnit.FEET).convertTo(LengthUnit.INCH);

		assertEquals(new QuantityLength(12.0, LengthUnit.INCH), result);
	}

	@Test
	void testQuantityLengthRefactored_Add() {
		QuantityLength result = new QuantityLength(1.0, LengthUnit.FEET).add(new QuantityLength(12.0, LengthUnit.INCH),
				LengthUnit.FEET);

		assertEquals(new QuantityLength(2.0, LengthUnit.FEET), result);
	}

	
	@Test
	void testQuantityLengthRefactored_NullUnit() {
		assertThrows(IllegalArgumentException.class, () -> new QuantityLength(1.0, null));
	}

	@Test
	void testQuantityLengthRefactored_InvalidValue() {
		assertThrows(IllegalArgumentException.class, () -> new QuantityLength(Double.NaN, LengthUnit.FEET));
	}

	@Test
	void testRoundTripConversion_RefactoredDesign() {
		QuantityLength result = new QuantityLength(1.0, LengthUnit.FEET).convertTo(LengthUnit.INCH)
				.convertTo(LengthUnit.FEET);

		assertEquals(new QuantityLength(1.0, LengthUnit.FEET), result);
	}

	// Architectural Scalability Test

	@Test
	void testArchitecturalScalability_MultipleCategories() {
		assertNotNull(LengthUnit.FEET);
		assertTrue(LengthUnit.FEET instanceof LengthUnit);
	}

	// Enum Immutability Test

	@Test
	void testUnitImmutability() {
		assertThrows(NoSuchMethodException.class, () -> LengthUnit.class.getDeclaredMethod("setConversionFactor"));
	}

}