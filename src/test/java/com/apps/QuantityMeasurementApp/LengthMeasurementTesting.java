package com.apps.QuantityMeasurementApp;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.apps.quantitymeasurement.LengthUnit;
import com.apps.quantitymeasurement.QuantityLength;

// Length Testing Class
public class LengthMeasurementTesting {

	// Checking yard to yard conversion (same value)
	@Test
	void testEquality_YardToYard_SameValue() {

		QuantityLength q1 = new QuantityLength(1.0, LengthUnit.YARDS);
		QuantityLength q2 = new QuantityLength(1.0, LengthUnit.YARDS);

		assertTrue(q1.equals(q2));
	}

	// Checking yard to yard conversion (different value)
	@Test
	void testEquality_YardToYard_DifferentValue() {

		QuantityLength q1 = new QuantityLength(1.0, LengthUnit.YARDS);
		QuantityLength q2 = new QuantityLength(2.0, LengthUnit.YARDS);

		assertFalse(q1.equals(q2));
	}

	// Checking yard to feet conversion
	@Test
	void testEquality_YardToFeet_EquivalentValue() {

		QuantityLength yard = new QuantityLength(1.0, LengthUnit.YARDS);
		QuantityLength feet = new QuantityLength(3.0, LengthUnit.FEET);

		assertTrue(yard.equals(feet));
	}

	// Checking feet to yard conversion
	@Test
	void testEquality_FeetToYard_EquivalentValue() {

		QuantityLength feet = new QuantityLength(3.0, LengthUnit.FEET);
		QuantityLength yard = new QuantityLength(1.0, LengthUnit.YARDS);

		assertTrue(feet.equals(yard));
	}

	// Checking yard to inches conversion
	@Test
	void testEquality_YardToInches_EquivalentValue() {

		QuantityLength yard = new QuantityLength(1.0, LengthUnit.YARDS);
		QuantityLength inches = new QuantityLength(36.0, LengthUnit.INCH);

		assertTrue(yard.equals(inches));
	}

	// Checking inches to yard conversion
	@Test
	void testEquality_InchesToYard_EquivalentValue() {

		QuantityLength inches = new QuantityLength(36.0, LengthUnit.INCH);
		QuantityLength yard = new QuantityLength(1.0, LengthUnit.YARDS);

		assertTrue(inches.equals(yard));
	}

	// Checking yard to feet non equivalent conversion
	@Test
	void testEquality_YardToFeet_NonEquivalentValue() {

		QuantityLength yard = new QuantityLength(1.0, LengthUnit.YARDS);
		QuantityLength feet = new QuantityLength(2.0, LengthUnit.FEET);

		assertFalse(yard.equals(feet));
	}

	// Checking centimeters to inches conversion
	@Test
	void testEquality_CentimetersToInches_EquivalentValue() {

		QuantityLength cm = new QuantityLength(1.0, LengthUnit.CENTIMETERS);
		QuantityLength inches = new QuantityLength(0.393701, LengthUnit.INCH);

		assertTrue(cm.equals(inches));
	}

	// Checking centimeters to feet non equivalent conversion
	@Test
	void testEquality_CentimetersToFeet_NonEquivalentValue() {

		QuantityLength cm = new QuantityLength(1.0, LengthUnit.CENTIMETERS);
		QuantityLength feet = new QuantityLength(1.0, LengthUnit.FEET);

		assertFalse(cm.equals(feet));
	}

	// Checking multi unit transitive property
	@Test
	void testEquality_MultiUnit_TransitiveProperty() {

		QuantityLength yard = new QuantityLength(1.0, LengthUnit.YARDS);
		QuantityLength feet = new QuantityLength(3.0, LengthUnit.FEET);
		QuantityLength inches = new QuantityLength(36.0, LengthUnit.INCH);

		assertTrue(yard.equals(feet));
		assertTrue(feet.equals(inches));
		assertTrue(yard.equals(inches));
	}

	// Checking invalid unit for yard
	@Test
	void testEquality_YardWithNullUnit() {

		assertThrows(IllegalArgumentException.class, () -> new QuantityLength(1.0, null));
	}

	// Checking invalid unit for centimeters
	@Test
	void testEquality_CentimetersWithNullUnit() {

		assertThrows(IllegalArgumentException.class, () -> new QuantityLength(1.0, null));
	}

	// Checking same reference for yard
	@Test
	void testEquality_YardSameReference() {

		QuantityLength yard = new QuantityLength(1.0, LengthUnit.YARDS);

		assertTrue(yard.equals(yard));
	}

	// Checking same reference for centimeters
	@Test
	void testEquality_CentimetersSameReference() {

		QuantityLength cm = new QuantityLength(2.0, LengthUnit.CENTIMETERS);

		assertTrue(cm.equals(cm));
	}

	// Checking null comparison for yard
	@Test
	void testEquality_YardNullComparison() {

		QuantityLength yard = new QuantityLength(1.0, LengthUnit.YARDS);

		assertFalse(yard.equals(null));
	}

	// Checking null comparison for centimeters
	@Test
	void testEquality_CentimetersNullComparison() {

		QuantityLength cm = new QuantityLength(2.0, LengthUnit.CENTIMETERS);

		assertFalse(cm.equals(null));
	}

	// Checking complex scenario with all units
	@Test
	void testEquality_AllUnits_ComplexScenario() {

		QuantityLength yards = new QuantityLength(2.0, LengthUnit.YARDS);
		QuantityLength feet = new QuantityLength(6.0, LengthUnit.FEET);
		QuantityLength inches = new QuantityLength(72.0, LengthUnit.INCH);

		assertTrue(yards.equals(feet));
		assertTrue(feet.equals(inches));
		assertTrue(yards.equals(inches));
	}
}