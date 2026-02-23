package com.apps.QuantityMeasurementApp;

import org.junit.jupiter.api.Test;

import com.apps.quantitymeasurement.LengthUnit;
import com.apps.quantitymeasurement.Quantity;
import com.apps.quantitymeasurement.VolumeUnit;
import com.apps.quantitymeasurement.WeightUnit;

import static org.junit.jupiter.api.Assertions.*;

// Testing Class
class QuantityMeasurementAppTest {

	private static final double EPSILON = 1e-5;

	// EQUALITY TESTS

	@Test
	void testEquality_LitreToLitre_SameValue() {
		assertEquals(new Quantity<>(1.0, VolumeUnit.LITRE), new Quantity<>(1.0, VolumeUnit.LITRE));
	}

	@Test
	void testEquality_LitreToLitre_DifferentValue() {
		assertNotEquals(new Quantity<>(1.0, VolumeUnit.LITRE), new Quantity<>(2.0, VolumeUnit.LITRE));
	}

	@Test
	void testEquality_LitreToMillilitre_EquivalentValue() {
		assertEquals(new Quantity<>(1.0, VolumeUnit.LITRE), new Quantity<>(1000.0, VolumeUnit.MILLILITRE));
	}

	@Test
	void testEquality_MillilitreToLitre_EquivalentValue() {
		assertEquals(new Quantity<>(1000.0, VolumeUnit.MILLILITRE), new Quantity<>(1.0, VolumeUnit.LITRE));
	}

	@Test
	void testEquality_LitreToGallon_EquivalentValue() {
		Quantity<VolumeUnit> litre = new Quantity<>(1.0, VolumeUnit.LITRE);
		Quantity<VolumeUnit> gallon = new Quantity<>(0.264172, VolumeUnit.GALLON);
		assertTrue(litre.equals(gallon));
	}

	@Test
	void testEquality_GallonToLitre_EquivalentValue() {
		Quantity<VolumeUnit> gallon = new Quantity<>(1.0, VolumeUnit.GALLON);
		Quantity<VolumeUnit> litre = new Quantity<>(3.78541, VolumeUnit.LITRE);
		assertTrue(gallon.equals(litre));
	}

	@Test
	void testEquality_VolumeVsLength_Incompatible() {
		assertNotEquals(new Quantity<>(1.0, VolumeUnit.LITRE), new Quantity<>(1.0, LengthUnit.FEET));
	}

	@Test
	void testEquality_VolumeVsWeight_Incompatible() {
		assertNotEquals(new Quantity<>(1.0, VolumeUnit.LITRE), new Quantity<>(1.0, WeightUnit.KILOGRAM));
	}

	@Test
	void testEquality_NullComparison() {
		assertFalse(new Quantity<>(1.0, VolumeUnit.LITRE).equals(null));
	}

	@Test
	void testEquality_SameReference() {
		Quantity<VolumeUnit> volume = new Quantity<>(1.0, VolumeUnit.LITRE);
		assertEquals(volume, volume);
	}

	@Test
	void testEquality_NullUnit() {
		assertThrows(IllegalArgumentException.class, () -> new Quantity<>(1.0, null));
	}

	@Test
	void testEquality_TransitiveProperty() {
		Quantity<VolumeUnit> a = new Quantity<>(1.0, VolumeUnit.LITRE);
		Quantity<VolumeUnit> b = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
		Quantity<VolumeUnit> c = new Quantity<>(1.0, VolumeUnit.LITRE);

		assertTrue(a.equals(b) && b.equals(c) && a.equals(c));
	}

	@Test
	void testEquality_ZeroValue() {
		assertEquals(new Quantity<>(0.0, VolumeUnit.LITRE), new Quantity<>(0.0, VolumeUnit.MILLILITRE));
	}

	@Test
	void testEquality_NegativeVolume() {
		assertEquals(new Quantity<>(-1.0, VolumeUnit.LITRE), new Quantity<>(-1000.0, VolumeUnit.MILLILITRE));
	}

	@Test
	void testEquality_LargeVolumeValue() {
		assertEquals(new Quantity<>(1000000.0, VolumeUnit.MILLILITRE), new Quantity<>(1000.0, VolumeUnit.LITRE));
	}

	@Test
	void testEquality_SmallVolumeValue() {
		assertEquals(new Quantity<>(0.001, VolumeUnit.LITRE), new Quantity<>(1.0, VolumeUnit.MILLILITRE));
	}

	// CONVERSION TESTS

	@Test
	void testConversion_LitreToMillilitre() {
		Quantity<VolumeUnit> result = new Quantity<>(1.0, VolumeUnit.LITRE).convertTo(VolumeUnit.MILLILITRE);

		assertEquals(1000.0, result.getValue(), EPSILON);
	}

	@Test
	void testConversion_MillilitreToLitre() {
		Quantity<VolumeUnit> result = new Quantity<>(1000.0, VolumeUnit.MILLILITRE).convertTo(VolumeUnit.LITRE);

		assertEquals(1.0, result.getValue(), EPSILON);
	}

	@Test
	void testConversion_LitreToGallon() {
		Quantity<VolumeUnit> result = new Quantity<>(3.78541, VolumeUnit.LITRE).convertTo(VolumeUnit.GALLON);

		assertEquals(1.0, result.getValue(), EPSILON);
	}

	@Test
	void testConversion_SameUnit() {
		Quantity<VolumeUnit> result = new Quantity<>(5.0, VolumeUnit.LITRE).convertTo(VolumeUnit.LITRE);

		assertEquals(5.0, result.getValue(), EPSILON);
	}

	@Test
	void testConversion_ZeroValue() {
		Quantity<VolumeUnit> result = new Quantity<>(0.0, VolumeUnit.LITRE).convertTo(VolumeUnit.MILLILITRE);

		assertEquals(0.0, result.getValue(), EPSILON);
	}

	@Test
	void testConversion_NegativeValue() {
		Quantity<VolumeUnit> result = new Quantity<>(-1.0, VolumeUnit.LITRE).convertTo(VolumeUnit.MILLILITRE);

		assertEquals(-1000.0, result.getValue(), EPSILON);
	}

	@Test
	void testConversion_RoundTrip() {
		Quantity<VolumeUnit> result = new Quantity<>(1.5, VolumeUnit.LITRE).convertTo(VolumeUnit.MILLILITRE)
				.convertTo(VolumeUnit.LITRE);

		assertEquals(1.5, result.getValue(), EPSILON);
	}

	// ADDITION TESTS

	@Test
	void testAddition_SameUnit_LitrePlusLitre() {
		Quantity<VolumeUnit> result = new Quantity<>(1.0, VolumeUnit.LITRE).add(new Quantity<>(2.0, VolumeUnit.LITRE));

		assertEquals(3.0, result.getValue(), EPSILON);
	}

	@Test
	void testAddition_SameUnit_MillilitrePlusMillilitre() {
		Quantity<VolumeUnit> result = new Quantity<>(500.0, VolumeUnit.MILLILITRE)
				.add(new Quantity<>(500.0, VolumeUnit.MILLILITRE));

		assertEquals(1000.0, result.getValue(), EPSILON);
	}

	@Test
	void testAddition_CrossUnit_LitrePlusMillilitre() {
		Quantity<VolumeUnit> result = new Quantity<>(1.0, VolumeUnit.LITRE)
				.add(new Quantity<>(1000.0, VolumeUnit.MILLILITRE));

		assertEquals(2.0, result.getValue(), EPSILON);
	}

	@Test
	void testAddition_CrossUnit_MillilitrePlusLitre() {
		Quantity<VolumeUnit> result = new Quantity<>(1000.0, VolumeUnit.MILLILITRE)
				.add(new Quantity<>(1.0, VolumeUnit.LITRE));

		assertEquals(2000.0, result.getValue(), EPSILON);
	}

	@Test
	void testAddition_WithZero() {
		Quantity<VolumeUnit> result = new Quantity<>(5.0, VolumeUnit.LITRE)
				.add(new Quantity<>(0.0, VolumeUnit.MILLILITRE));

		assertEquals(5.0, result.getValue(), EPSILON);
	}

	@Test
	void testAddition_NegativeValues() {
		Quantity<VolumeUnit> result = new Quantity<>(5.0, VolumeUnit.LITRE)
				.add(new Quantity<>(-2000.0, VolumeUnit.MILLILITRE));

		assertEquals(3.0, result.getValue(), EPSILON);
	}

	@Test
	void testAddition_LargeValues() {
		Quantity<VolumeUnit> result = new Quantity<>(1e6, VolumeUnit.LITRE).add(new Quantity<>(1e6, VolumeUnit.LITRE));

		assertEquals(2e6, result.getValue(), EPSILON);
	}

	// ENUM TESTS

	@Test
	void testVolumeUnitEnum_LitreConstant() {
		assertEquals(1.0, VolumeUnit.LITRE.getConversionFactor(), EPSILON);
	}

	@Test
	void testVolumeUnitEnum_MillilitreConstant() {
		assertEquals(0.001, VolumeUnit.MILLILITRE.getConversionFactor(), EPSILON);
	}

	@Test
	void testVolumeUnitEnum_GallonConstant() {
		assertEquals(3.78541, VolumeUnit.GALLON.getConversionFactor(), EPSILON);
	}

	@Test
	void testConvertToBaseUnit_GallonToLitre() {
		assertEquals(3.78541, VolumeUnit.GALLON.convertToBaseUnit(1.0), EPSILON);
	}

	@Test
	void testConvertFromBaseUnit_LitreToGallon() {
		assertEquals(1.0, VolumeUnit.GALLON.convertFromBaseUnit(3.78541), EPSILON);
	}

	// ARCHITECTURE TESTS

	@Test
	void testGenericQuantity_VolumeOperations_Consistency() {
		Quantity<VolumeUnit> volume = new Quantity<>(1.0, VolumeUnit.LITRE);

		Quantity<LengthUnit> length = new Quantity<>(1.0, LengthUnit.FEET);

		assertNotEquals(volume, length);
	}

	@Test
	void testScalability_VolumeIntegration() {
		Quantity<VolumeUnit> v1 = new Quantity<>(1.0, VolumeUnit.LITRE);

		Quantity<VolumeUnit> v2 = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);

		assertEquals(v1, v2);
	}

	// SUBTRACTION TESTS

	@Test
	void testSubtraction_SameUnit_FeetMinusFeet() {
		Quantity<LengthUnit> result = new Quantity<>(10.0, LengthUnit.FEET)
				.subtract(new Quantity<>(5.0, LengthUnit.FEET));

		assertEquals(new Quantity<>(5.0, LengthUnit.FEET), result);
	}

	@Test
	void testSubtraction_SameUnit_LitreMinusLitre() {
		Quantity<VolumeUnit> result = new Quantity<>(10.0, VolumeUnit.LITRE)
				.subtract(new Quantity<>(3.0, VolumeUnit.LITRE));

		assertEquals(new Quantity<>(7.0, VolumeUnit.LITRE), result);
	}

	@Test
	void testSubtraction_CrossUnit_FeetMinusInches() {
		Quantity<LengthUnit> result = new Quantity<>(10.0, LengthUnit.FEET)
				.subtract(new Quantity<>(6.0, LengthUnit.INCH));

		assertEquals(new Quantity<>(9.5, LengthUnit.FEET), result);
	}

	@Test
	void testSubtraction_CrossUnit_InchesMinusFeet() {
		Quantity<LengthUnit> result = new Quantity<>(120.0, LengthUnit.INCH)
				.subtract(new Quantity<>(5.0, LengthUnit.FEET));

		assertEquals(new Quantity<>(60.0, LengthUnit.INCH), result);
	}

	@Test
	void testSubtraction_ExplicitTargetUnit_Feet() {
		Quantity<LengthUnit> result = new Quantity<>(10.0, LengthUnit.FEET)
				.subtract(new Quantity<>(6.0, LengthUnit.INCH), LengthUnit.FEET);

		assertEquals(new Quantity<>(9.5, LengthUnit.FEET), result);
	}

	@Test
	void testSubtraction_ExplicitTargetUnit_Inches() {
		Quantity<LengthUnit> result = new Quantity<>(10.0, LengthUnit.FEET)
				.subtract(new Quantity<>(6.0, LengthUnit.INCH), LengthUnit.INCH);

		assertEquals(new Quantity<>(114.0, LengthUnit.INCH), result);
	}

	@Test
	void testSubtraction_ExplicitTargetUnit_Millilitre() {
		Quantity<VolumeUnit> result = new Quantity<>(5.0, VolumeUnit.LITRE)
				.subtract(new Quantity<>(2.0, VolumeUnit.LITRE), VolumeUnit.MILLILITRE);

		assertEquals(new Quantity<>(3000.0, VolumeUnit.MILLILITRE), result);
	}

	@Test
	void testSubtraction_ResultingInNegative() {
		Quantity<LengthUnit> result = new Quantity<>(5.0, LengthUnit.FEET)
				.subtract(new Quantity<>(10.0, LengthUnit.FEET));

		assertEquals(new Quantity<>(-5.0, LengthUnit.FEET), result);
	}

	@Test
	void testSubtraction_ResultingInZero() {
		Quantity<LengthUnit> result = new Quantity<>(10.0, LengthUnit.FEET)
				.subtract(new Quantity<>(120.0, LengthUnit.INCH));

		assertEquals(new Quantity<>(0.0, LengthUnit.FEET), result);
	}

	@Test
	void testSubtraction_WithZeroOperand() {
		Quantity<LengthUnit> result = new Quantity<>(5.0, LengthUnit.FEET)
				.subtract(new Quantity<>(0.0, LengthUnit.INCH));

		assertEquals(new Quantity<>(5.0, LengthUnit.FEET), result);
	}

	@Test
	void testSubtraction_WithNegativeValues() {
		Quantity<LengthUnit> result = new Quantity<>(5.0, LengthUnit.FEET)
				.subtract(new Quantity<>(-2.0, LengthUnit.FEET));

		assertEquals(new Quantity<>(7.0, LengthUnit.FEET), result);
	}

	@Test
	void testSubtraction_NonCommutative() {
		Quantity<LengthUnit> a = new Quantity<>(10.0, LengthUnit.FEET);

		Quantity<LengthUnit> b = new Quantity<>(5.0, LengthUnit.FEET);

		assertNotEquals(a.subtract(b), b.subtract(a));
	}

	@Test
	void testSubtraction_WithLargeValues() {
		Quantity<WeightUnit> result = new Quantity<>(1e6, WeightUnit.KILOGRAM)
				.subtract(new Quantity<>(5e5, WeightUnit.KILOGRAM));

		assertEquals(new Quantity<>(5e5, WeightUnit.KILOGRAM), result);
	}

	@Test
	void testSubtraction_NullOperand() {
		assertThrows(IllegalArgumentException.class, () -> new Quantity<>(10.0, LengthUnit.FEET).subtract(null));
	}

	@Test
	void testSubtraction_NullTargetUnit() {
		assertThrows(IllegalArgumentException.class,
				() -> new Quantity<>(10.0, LengthUnit.FEET).subtract(new Quantity<>(5.0, LengthUnit.FEET), null));
	}

	@Test
	void testSubtraction_ChainedOperations() {
		Quantity<LengthUnit> result = new Quantity<>(10.0, LengthUnit.FEET)
				.subtract(new Quantity<>(2.0, LengthUnit.FEET)).subtract(new Quantity<>(1.0, LengthUnit.FEET));

		assertEquals(new Quantity<>(7.0, LengthUnit.FEET), result);
	}

	// DIVISION TESTS

	@Test
	void testDivision_SameUnit_FeetDividedByFeet() {
		double result = new Quantity<>(10.0, LengthUnit.FEET).divide(new Quantity<>(2.0, LengthUnit.FEET));

		assertEquals(5.0, result, EPSILON);
	}

	@Test
	void testDivision_CrossUnit_FeetDividedByInches() {
		double result = new Quantity<>(24.0, LengthUnit.INCH).divide(new Quantity<>(2.0, LengthUnit.FEET));

		assertEquals(1.0, result, EPSILON);
	}

	@Test
	void testDivision_RatioLessThanOne() {
		double result = new Quantity<>(5.0, LengthUnit.FEET).divide(new Quantity<>(10.0, LengthUnit.FEET));

		assertEquals(0.5, result, EPSILON);
	}

	@Test
	void testDivision_ByZero() {
		assertThrows(ArithmeticException.class,
				() -> new Quantity<>(10.0, LengthUnit.FEET).divide(new Quantity<>(0.0, LengthUnit.FEET)));
	}

	@Test
	void testDivision_NullOperand() {
		assertThrows(IllegalArgumentException.class, () -> new Quantity<>(10.0, LengthUnit.FEET).divide(null));
	}

	@Test
	void testSubtraction_Immutability() {
		Quantity<LengthUnit> original = new Quantity<>(10.0, LengthUnit.FEET);

		original.subtract(new Quantity<>(5.0, LengthUnit.FEET));

		assertEquals(new Quantity<>(10.0, LengthUnit.FEET), original);
	}

	@Test
	void testDivision_Immutability() {
		Quantity<LengthUnit> original = new Quantity<>(10.0, LengthUnit.FEET);

		original.divide(new Quantity<>(5.0, LengthUnit.FEET));

		assertEquals(new Quantity<>(10.0, LengthUnit.FEET), original);
	}
}