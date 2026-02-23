package com.apps.QuantityMeasurementApp;

import org.junit.jupiter.api.Test;

import com.apps.quantitymeasurement.IMeasurable;
import com.apps.quantitymeasurement.LengthUnit;
import com.apps.quantitymeasurement.Quantity;
import com.apps.quantitymeasurement.WeightUnit;

import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

// Testing Class
public class QuantityMeasurementAppTest {

	    // IMeasurable Interface Tests
	    @Test
	    @DisplayName("WeightUnit correctly implements IMeasurable")
	    void testIMeasurableInterface_WeightUnitImplementation() {
	        assertNotNull(WeightUnit.KILOGRAM.getUnitName());
	        assertEquals(1.0, WeightUnit.KILOGRAM.convertFromBaseUnit(
	                WeightUnit.KILOGRAM.convertToBaseUnit(1.0)));
	    }

	    @Test
	    @DisplayName("LengthUnit and WeightUnit behave consistently")
	    void testIMeasurableInterface_ConsistentBehavior() {
	        assertEquals(
	                LengthUnit.FEET.convertToBaseUnit(1.0),
	                LengthUnit.INCH.convertToBaseUnit(12.0)
	        );

	        assertEquals(
	                WeightUnit.KILOGRAM.convertToBaseUnit(1.0),
	                WeightUnit.GRAM.convertToBaseUnit(1000.0)
	        );
	    }

	    // Equality Tests

	    @Test
	    void testGenericQuantity_LengthOperations_Equality() {
	        Quantity<LengthUnit> q1 = new Quantity<>(1.0, LengthUnit.FEET);
	        Quantity<LengthUnit> q2 = new Quantity<>(12.0, LengthUnit.INCH);
	        assertEquals(q1, q2);
	    }

	    @Test
	    void testGenericQuantity_WeightOperations_Equality() {
	        Quantity<WeightUnit> q1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);
	        Quantity<WeightUnit> q2 = new Quantity<>(1000.0, WeightUnit.GRAM);
	        assertEquals(q1, q2);
	    }

	    // Conversion Tests

	    @Test
	    void testGenericQuantity_LengthOperations_Conversion() {
	        Quantity<LengthUnit> q =
	                new Quantity<>(1.0, LengthUnit.FEET);

	        Quantity<LengthUnit> converted =
	                q.convertTo(LengthUnit.INCH);

	        assertEquals(new Quantity<>(12.0, LengthUnit.INCH), converted);
	    }

	    @Test
	    void testGenericQuantity_WeightOperations_Conversion() {
	        Quantity<WeightUnit> q =
	                new Quantity<>(1.0, WeightUnit.KILOGRAM);

	        Quantity<WeightUnit> converted =
	                q.convertTo(WeightUnit.GRAM);

	        assertEquals(new Quantity<>(1000.0, WeightUnit.GRAM), converted);
	    }

	    // Addition Tests

	    @Test
	    void testGenericQuantity_LengthOperations_Addition() {
	        Quantity<LengthUnit> q1 =
	                new Quantity<>(1.0, LengthUnit.FEET);

	        Quantity<LengthUnit> q2 =
	                new Quantity<>(12.0, LengthUnit.INCH);

	        Quantity<LengthUnit> result =
	                q1.add(q2, LengthUnit.FEET);

	        assertEquals(new Quantity<>(2.0, LengthUnit.FEET), result);
	    }

	    @Test
	    void testGenericQuantity_WeightOperations_Addition() {
	        Quantity<WeightUnit> q1 =
	                new Quantity<>(1.0, WeightUnit.KILOGRAM);

	        Quantity<WeightUnit> q2 =
	                new Quantity<>(1000.0, WeightUnit.GRAM);

	        Quantity<WeightUnit> result =
	                q1.add(q2, WeightUnit.KILOGRAM);

	        assertEquals(new Quantity<>(2.0, WeightUnit.KILOGRAM), result);
	    }

	    // Cross Category Safety

	    @Test
	    void testCrossCategoryPrevention_LengthVsWeight() {
	        Quantity<LengthUnit> length =
	                new Quantity<>(1.0, LengthUnit.FEET);

	        Quantity<WeightUnit> weight =
	                new Quantity<>(1.0, WeightUnit.KILOGRAM);

	        assertNotEquals(length, weight);
	    }

	    // Constructor Validation

	    @Test
	    void testGenericQuantity_ConstructorValidation_NullUnit() {
	        assertThrows(IllegalArgumentException.class,
	                () -> new Quantity<>(1.0, null));
	    }

	    @Test
	    void testGenericQuantity_ConstructorValidation_InvalidValue() {
	        assertThrows(IllegalArgumentException.class,
	                () -> new Quantity<>(Double.NaN, LengthUnit.FEET));
	    }

	    // HashCode & Equals Contract

	    @Test
	    void testHashCode_GenericQuantity_Consistency() {
	        Quantity<LengthUnit> q1 =
	                new Quantity<>(1.0, LengthUnit.FEET);

	        Quantity<LengthUnit> q2 =
	                new Quantity<>(12.0, LengthUnit.INCH);

	        assertEquals(q1.hashCode(), q2.hashCode());
	    }

	    @Test
	    void testEquals_GenericQuantity_ContractPreservation() {
	        Quantity<LengthUnit> a =
	                new Quantity<>(1.0, LengthUnit.FEET);

	        Quantity<LengthUnit> b =
	                new Quantity<>(12.0, LengthUnit.INCH);

	        Quantity<LengthUnit> c =
	                new Quantity<>(1.0, LengthUnit.FEET);

	        assertTrue(a.equals(a));       // Reflexive
	        assertTrue(a.equals(b));       // Symmetric
	        assertTrue(a.equals(b) && b.equals(c) && a.equals(c)); // Transitive
	    }

	    // Immutability Test

	    @Test
	    void testImmutability_GenericQuantity() {
	        Quantity<LengthUnit> q =
	                new Quantity<>(1.0, LengthUnit.FEET);

	        Quantity<LengthUnit> converted =
	                q.convertTo(LengthUnit.INCH);

	        assertNotSame(q, converted);
	    }

	    // Scalability Test – New Category Integration

	    enum VolumeUnit implements IMeasurable {
	        LITER(1.0),
	        MILLILITER(0.001);

	        private final double factor;

	        VolumeUnit(double factor) {
	            this.factor = factor;
	        }

	        public double convertToBaseUnit(double value) {
	            return value * factor;
	        }

	        public double convertFromBaseUnit(double baseValue) {
	            return baseValue / factor;
	        }

	        public String getUnitName() {
	            return name();
	        }

			@Override
			public double getConversionFactor() {
				return 0;
			}
	    }

	    @Test
	    void testScalability_NewUnitEnumIntegration() {

	        Quantity<VolumeUnit> v1 =
	                new Quantity<>(1.0, VolumeUnit.LITER);

	        Quantity<VolumeUnit> v2 =
	                new Quantity<>(1000.0, VolumeUnit.MILLILITER);

	        assertEquals(v1, v2);
	    }

//	private static final double DELTA = 1e-3;
//
//	// Enum Constant Validation Tests for Length
//	@Test
//	void testLengthUnitEnum_InchConstant() {
//		assertEquals(1.0, LengthUnit.INCH.getConversionFactor(), DELTA);
//	}
//
//	@Test
//	void testLengthUnitEnum_FeetConstant() {
//		assertEquals(12.0, LengthUnit.FEET.getConversionFactor(), DELTA);
//	}
//
//	@Test
//	void testLengthUnitEnum_YardsConstant() {
//		assertEquals(36.0, LengthUnit.YARDS.getConversionFactor(), DELTA);
//	}
//
//	@Test
//	void testLengthUnitEnum_CentimetersConstant() {
//		assertEquals(1.0 / 2.54, LengthUnit.CENTIMETERS.getConversionFactor(), DELTA);
//	}
//
//	// Convert To Base Unit (INCH)
//
//	@Test
//	void testConvertToBaseUnit_FeetToInch() {
//		assertEquals(60.0, LengthUnit.FEET.convertToBaseUnit(5.0), DELTA);
//	}
//
//	@Test
//	void testConvertToBaseUnit_YardsToInch() {
//		assertEquals(36.0, LengthUnit.YARDS.convertToBaseUnit(1.0), DELTA);
//	}
//
//	@Test
//	void testConvertToBaseUnit_CentimetersToInch() {
//		assertEquals(1.0, LengthUnit.CENTIMETERS.convertToBaseUnit(2.54), DELTA);
//	}
//
//	// Convert From Base Unit (INCH)
//
//	@Test
//	void testConvertFromBaseUnit_InchToFeet() {
//		assertEquals(1.0, LengthUnit.FEET.convertFromBaseUnit(12.0), DELTA);
//	}
//
//	@Test
//	void testConvertFromBaseUnit_InchToYards() {
//		assertEquals(1.0, LengthUnit.YARDS.convertFromBaseUnit(36.0), DELTA);
//	}
//
//	@Test
//	void testConvertFromBaseUnit_InchToCentimeters() {
//		assertEquals(2.54, LengthUnit.CENTIMETERS.convertFromBaseUnit(1.0), DELTA);
//	}
//
//	// QuantityLength Tests
//
//	@Test
//	void testQuantityLengthRefactored_Equality() {
//		assertEquals(new QuantityLength(1.0, LengthUnit.FEET), new QuantityLength(12.0, LengthUnit.INCH));
//	}
//
//	@Test
//	void testQuantityLengthRefactored_ConvertTo() {
//		QuantityLength result = new QuantityLength(1.0, LengthUnit.FEET).convertTo(LengthUnit.INCH);
//
//		assertEquals(new QuantityLength(12.0, LengthUnit.INCH), result);
//	}
//
//	@Test
//	void testQuantityLengthRefactored_Add() {
//		QuantityLength result = new QuantityLength(1.0, LengthUnit.FEET).add(new QuantityLength(12.0, LengthUnit.INCH),
//				LengthUnit.FEET);
//
//		assertEquals(new QuantityLength(2.0, LengthUnit.FEET), result);
//	}
//
//	@Test
//	void testQuantityLengthRefactored_NullUnit() {
//		assertThrows(IllegalArgumentException.class, () -> new QuantityLength(1.0, null));
//	}
//
//	@Test
//	void testQuantityLengthRefactored_InvalidValue() {
//		assertThrows(IllegalArgumentException.class, () -> new QuantityLength(Double.NaN, LengthUnit.FEET));
//	}
//
//	@Test
//	void testRoundTripConversion_RefactoredDesign() {
//		QuantityLength result = new QuantityLength(1.0, LengthUnit.FEET).convertTo(LengthUnit.INCH)
//				.convertTo(LengthUnit.FEET);
//
//		assertEquals(new QuantityLength(1.0, LengthUnit.FEET), result);
//	}
//
//	// Architectural Scalability Test
//
//	@Test
//	void testArchitecturalScalability_MultipleCategories() {
//		assertNotNull(LengthUnit.FEET);
//		assertTrue(LengthUnit.FEET instanceof LengthUnit);
//	}
//
//	// Enum Immutability Test
//
//	@Test
//	void testUnitImmutability() {
//		assertThrows(NoSuchMethodException.class, () -> LengthUnit.class.getDeclaredMethod("setConversionFactor"));
//	}
//
//	// Enum Constant Validation test for weight
//
//	private static final double EPSILON = 1e-6;
//
//	// Equality Tests
//	@Test
//	void testEquality_KilogramToKilogram_SameValue() {
//		assertEquals(new QuantityWeight(1.0, WeightUnit.KILOGRAM), new QuantityWeight(1.0, WeightUnit.KILOGRAM));
//	}
//
//	@Test
//	void testEquality_KilogramToKilogram_DifferentValue() {
//		assertNotEquals(new QuantityWeight(1.0, WeightUnit.KILOGRAM), new QuantityWeight(2.0, WeightUnit.KILOGRAM));
//	}
//
//	@Test
//	void testEquality_KilogramToGram_EquivalentValue() {
//		assertEquals(new QuantityWeight(1.0, WeightUnit.KILOGRAM), new QuantityWeight(1000.0, WeightUnit.GRAM));
//	}
//
//	@Test
//	void testEquality_GramToKilogram_EquivalentValue() {
//		assertEquals(new QuantityWeight(1000.0, WeightUnit.GRAM), new QuantityWeight(1.0, WeightUnit.KILOGRAM));
//	}
//
//	@Test
//	void testEquality_WeightVsLength_Incompatible() {
//		QuantityWeight weight = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
//		QuantityLength length = new QuantityLength(1.0, LengthUnit.FEET);
//		assertNotEquals(weight, length);
//	}
//
//	@Test
//	void testEquality_NullComparison() {
//		assertNotEquals(new QuantityWeight(1.0, WeightUnit.KILOGRAM), null);
//	}
//
//	@Test
//	void testEquality_SameReference() {
//		QuantityWeight weight = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
//		assertEquals(weight, weight);
//	}
//
//	@Test
//	void testEquality_NullUnit() {
//		assertThrows(IllegalArgumentException.class, () -> new QuantityWeight(1.0, null));
//	}
//
//	@Test
//	void testEquality_TransitiveProperty() {
//		QuantityWeight a = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
//		QuantityWeight b = new QuantityWeight(1000.0, WeightUnit.GRAM);
//		QuantityWeight c = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
//
//		assertEquals(a, b);
//		assertEquals(b, c);
//		assertEquals(a, c);
//	}
//
//	@Test
//	void testEquality_ZeroValue() {
//		assertEquals(new QuantityWeight(0.0, WeightUnit.KILOGRAM), new QuantityWeight(0.0, WeightUnit.GRAM));
//	}
//
//	@Test
//	void testEquality_NegativeWeight() {
//		assertEquals(new QuantityWeight(-1.0, WeightUnit.KILOGRAM), new QuantityWeight(-1000.0, WeightUnit.GRAM));
//	}
//
//	@Test
//	void testEquality_LargeWeightValue() {
//		assertEquals(new QuantityWeight(1000000.0, WeightUnit.GRAM), new QuantityWeight(1000.0, WeightUnit.KILOGRAM));
//	}
//
//	@Test
//	void testEquality_SmallWeightValue() {
//		assertEquals(new QuantityWeight(0.001, WeightUnit.KILOGRAM), new QuantityWeight(1.0, WeightUnit.GRAM));
//	}
//
//	// Conversion Tests
//
//	@Test
//	void testConversion_SameUnit() {
//		QuantityWeight result = new QuantityWeight(5.0, WeightUnit.KILOGRAM).convertTo(WeightUnit.KILOGRAM);
//
//		assertEquals(5.0, result.getValue(), EPSILON);
//	}
//
//	@Test
//	void testConversion_ZeroValue() {
//		QuantityWeight result = new QuantityWeight(0.0, WeightUnit.KILOGRAM).convertTo(WeightUnit.GRAM);
//
//		assertEquals(0.0, result.getValue(), EPSILON);
//	}
//
//	@Test
//	void testConversion_NegativeValue() {
//		QuantityWeight result = new QuantityWeight(-1.0, WeightUnit.KILOGRAM).convertTo(WeightUnit.GRAM);
//
//		assertEquals(-1000.0, result.getValue(), EPSILON);
//	}
//
//	@Test
//	void testConversion_RoundTrip() {
//		QuantityWeight result = new QuantityWeight(1.5, WeightUnit.KILOGRAM).convertTo(WeightUnit.GRAM)
//				.convertTo(WeightUnit.KILOGRAM);
//
//		assertEquals(1.5, result.getValue(), EPSILON);
//	}
//
//	// Addition Tests
//
//	@Test
//	void testAddition_SameUnit_KilogramPlusKilogram() {
//		QuantityWeight result = new QuantityWeight(1.0, WeightUnit.KILOGRAM)
//				.add(new QuantityWeight(2.0, WeightUnit.KILOGRAM));
//
//		assertEquals(3.0, result.getValue(), EPSILON);
//	}
//
//	@Test
//	void testAddition_CrossUnit_KilogramPlusGram() {
//		QuantityWeight result = new QuantityWeight(1.0, WeightUnit.KILOGRAM)
//				.add(new QuantityWeight(1000.0, WeightUnit.GRAM));
//
//		assertEquals(2.0, result.getValue(), EPSILON);
//	}
//
//	@Test
//	void testAddition_ExplicitTargetUnit_Kilogram() {
//		QuantityWeight result = new QuantityWeight(1.0, WeightUnit.KILOGRAM)
//				.add(new QuantityWeight(1000.0, WeightUnit.GRAM), WeightUnit.GRAM);
//
//		assertEquals(2000.0, result.getValue(), EPSILON);
//	}
//
//	@Test
//	void testAddition_Commutativity() {
//		QuantityWeight a = new QuantityWeight(1.0, WeightUnit.KILOGRAM)
//				.add(new QuantityWeight(1000.0, WeightUnit.GRAM));
//
//		QuantityWeight b = new QuantityWeight(1000.0, WeightUnit.GRAM)
//				.add(new QuantityWeight(1.0, WeightUnit.KILOGRAM));
//
//		assertEquals(a.convertTo(WeightUnit.KILOGRAM), b.convertTo(WeightUnit.KILOGRAM));
//	}
//
//	@Test
//	void testAddition_WithZero() {
//		QuantityWeight result = new QuantityWeight(5.0, WeightUnit.KILOGRAM)
//				.add(new QuantityWeight(0.0, WeightUnit.GRAM));
//
//		assertEquals(5.0, result.getValue(), EPSILON);
//	}
//
//	@Test
//	void testAddition_NegativeValues() {
//		QuantityWeight result = new QuantityWeight(5.0, WeightUnit.KILOGRAM)
//				.add(new QuantityWeight(-2000.0, WeightUnit.GRAM));
//
//		assertEquals(3.0, result.getValue(), EPSILON);
//	}
//
//	@Test
//	void testAddition_LargeValues() {
//		QuantityWeight result = new QuantityWeight(1e6, WeightUnit.KILOGRAM)
//				.add(new QuantityWeight(1e6, WeightUnit.KILOGRAM));
//
//		assertEquals(2e6, result.getValue(), EPSILON);
//	}

}