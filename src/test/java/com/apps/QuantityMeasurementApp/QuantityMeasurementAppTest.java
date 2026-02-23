package com.apps.QuantityMeasurementApp;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.apps.quantitymeasurement.LengthUnit;
import com.apps.quantitymeasurement.QuantityLength;
import com.apps.quantitymeasurement.QuantityWeight;
import com.apps.quantitymeasurement.WeightUnit;

// Testing Class
public class QuantityMeasurementAppTest {

	private static final double DELTA = 1e-3;

	// Enum Constant Validation Tests for Length
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

	// Enum Constant Validation test for weight

	private static final double EPSILON = 1e-6;

	// Equality Tests
	@Test
	void testEquality_KilogramToKilogram_SameValue() {
		assertEquals(new QuantityWeight(1.0, WeightUnit.KILOGRAM), new QuantityWeight(1.0, WeightUnit.KILOGRAM));
	}

	@Test
	void testEquality_KilogramToKilogram_DifferentValue() {
		assertNotEquals(new QuantityWeight(1.0, WeightUnit.KILOGRAM), new QuantityWeight(2.0, WeightUnit.KILOGRAM));
	}

	@Test
	void testEquality_KilogramToGram_EquivalentValue() {
		assertEquals(new QuantityWeight(1.0, WeightUnit.KILOGRAM), new QuantityWeight(1000.0, WeightUnit.GRAM));
	}

	@Test
	void testEquality_GramToKilogram_EquivalentValue() {
		assertEquals(new QuantityWeight(1000.0, WeightUnit.GRAM), new QuantityWeight(1.0, WeightUnit.KILOGRAM));
	}

	@Test
	void testEquality_WeightVsLength_Incompatible() {
		QuantityWeight weight = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
		QuantityLength length = new QuantityLength(1.0, LengthUnit.FEET);
		assertNotEquals(weight, length);
	}

	@Test
	void testEquality_NullComparison() {
		assertNotEquals(new QuantityWeight(1.0, WeightUnit.KILOGRAM), null);
	}

	@Test
	void testEquality_SameReference() {
		QuantityWeight weight = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
		assertEquals(weight, weight);
	}

	@Test
	void testEquality_NullUnit() {
		assertThrows(IllegalArgumentException.class, () -> new QuantityWeight(1.0, null));
	}

	@Test
	void testEquality_TransitiveProperty() {
		QuantityWeight a = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
		QuantityWeight b = new QuantityWeight(1000.0, WeightUnit.GRAM);
		QuantityWeight c = new QuantityWeight(1.0, WeightUnit.KILOGRAM);

		assertEquals(a, b);
		assertEquals(b, c);
		assertEquals(a, c);
	}

	@Test
	void testEquality_ZeroValue() {
		assertEquals(new QuantityWeight(0.0, WeightUnit.KILOGRAM), new QuantityWeight(0.0, WeightUnit.GRAM));
	}

	@Test
	void testEquality_NegativeWeight() {
		assertEquals(new QuantityWeight(-1.0, WeightUnit.KILOGRAM), new QuantityWeight(-1000.0, WeightUnit.GRAM));
	}

	@Test
	void testEquality_LargeWeightValue() {
		assertEquals(new QuantityWeight(1000000.0, WeightUnit.GRAM), new QuantityWeight(1000.0, WeightUnit.KILOGRAM));
	}

	@Test
	void testEquality_SmallWeightValue() {
		assertEquals(new QuantityWeight(0.001, WeightUnit.KILOGRAM), new QuantityWeight(1.0, WeightUnit.GRAM));
	}

	// Conversion Tests

	@Test
	void testConversion_SameUnit() {
		QuantityWeight result = new QuantityWeight(5.0, WeightUnit.KILOGRAM).convertTo(WeightUnit.KILOGRAM);

		assertEquals(5.0, result.getValue(), EPSILON);
	}

	@Test
	void testConversion_ZeroValue() {
		QuantityWeight result = new QuantityWeight(0.0, WeightUnit.KILOGRAM).convertTo(WeightUnit.GRAM);

		assertEquals(0.0, result.getValue(), EPSILON);
	}

	@Test
	void testConversion_NegativeValue() {
		QuantityWeight result = new QuantityWeight(-1.0, WeightUnit.KILOGRAM).convertTo(WeightUnit.GRAM);

		assertEquals(-1000.0, result.getValue(), EPSILON);
	}

	@Test
	void testConversion_RoundTrip() {
		QuantityWeight result = new QuantityWeight(1.5, WeightUnit.KILOGRAM).convertTo(WeightUnit.GRAM)
				.convertTo(WeightUnit.KILOGRAM);

		assertEquals(1.5, result.getValue(), EPSILON);
	}

	// Addition Tests

	@Test
	void testAddition_SameUnit_KilogramPlusKilogram() {
		QuantityWeight result = new QuantityWeight(1.0, WeightUnit.KILOGRAM)
				.add(new QuantityWeight(2.0, WeightUnit.KILOGRAM));

		assertEquals(3.0, result.getValue(), EPSILON);
	}

	@Test
	void testAddition_CrossUnit_KilogramPlusGram() {
		QuantityWeight result = new QuantityWeight(1.0, WeightUnit.KILOGRAM)
				.add(new QuantityWeight(1000.0, WeightUnit.GRAM));

		assertEquals(2.0, result.getValue(), EPSILON);
	}

	@Test
	void testAddition_ExplicitTargetUnit_Kilogram() {
		QuantityWeight result = new QuantityWeight(1.0, WeightUnit.KILOGRAM)
				.add(new QuantityWeight(1000.0, WeightUnit.GRAM), WeightUnit.GRAM);

		assertEquals(2000.0, result.getValue(), EPSILON);
	}

	@Test
	void testAddition_Commutativity() {
		QuantityWeight a = new QuantityWeight(1.0, WeightUnit.KILOGRAM)
				.add(new QuantityWeight(1000.0, WeightUnit.GRAM));

		QuantityWeight b = new QuantityWeight(1000.0, WeightUnit.GRAM)
				.add(new QuantityWeight(1.0, WeightUnit.KILOGRAM));

		assertEquals(a.convertTo(WeightUnit.KILOGRAM), b.convertTo(WeightUnit.KILOGRAM));
	}

	@Test
	void testAddition_WithZero() {
		QuantityWeight result = new QuantityWeight(5.0, WeightUnit.KILOGRAM)
				.add(new QuantityWeight(0.0, WeightUnit.GRAM));

		assertEquals(5.0, result.getValue(), EPSILON);
	}

	@Test
	void testAddition_NegativeValues() {
		QuantityWeight result = new QuantityWeight(5.0, WeightUnit.KILOGRAM)
				.add(new QuantityWeight(-2000.0, WeightUnit.GRAM));

		assertEquals(3.0, result.getValue(), EPSILON);
	}

	@Test
	void testAddition_LargeValues() {
		QuantityWeight result = new QuantityWeight(1e6, WeightUnit.KILOGRAM)
				.add(new QuantityWeight(1e6, WeightUnit.KILOGRAM));

		assertEquals(2e6, result.getValue(), EPSILON);
	}

}