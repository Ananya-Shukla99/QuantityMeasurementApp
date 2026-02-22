package com.apps.QuantityMeasurementApp;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.apps.quantitymeasurement.LengthUnit;
import com.apps.quantitymeasurement.QuantityLength;

// Length Testing Class
public class LengthMeasurementTesting  {

	// Checking feet to feet conversion
	@Test
    void testEquality_FeetToFeet_SameValue() {

        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength q2 = new QuantityLength(1.0, LengthUnit.FEET);

        assertTrue(q1.equals(q2));
    }

	// Checking inch to inch conversion
    @Test
    void testEquality_InchToInch_SameValue() {

        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.INCH);
        QuantityLength q2 = new QuantityLength(1.0, LengthUnit.INCH);

        assertTrue(q1.equals(q2));
    }

    // Checking feet to inch conversion 
    @Test
    void testEquality_FeetToInch_EquivalentValue() {

        QuantityLength feet = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength inches = new QuantityLength(12.0, LengthUnit.INCH);

        assertTrue(feet.equals(inches));
    }

    // Checking inch to feet conversion
    @Test
    void testEquality_InchToFeet_EquivalentValue() {

        QuantityLength inches = new QuantityLength(12.0, LengthUnit.INCH);
        QuantityLength feet = new QuantityLength(1.0, LengthUnit.FEET);

        assertTrue(inches.equals(feet));
    }

    // Checking feet to feet with different value
    @Test
    void testEquality_FeetToFeet_DifferentValue() {

        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength q2 = new QuantityLength(2.0, LengthUnit.FEET);

        assertFalse(q1.equals(q2));
    }

    // Checking inch to inch different value
    @Test
    void testEquality_InchToInch_DifferentValue() {

        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.INCH);
        QuantityLength q2 = new QuantityLength(2.0, LengthUnit.INCH);

        assertFalse(q1.equals(q2));
    }

    // Checking Invalid unit
    @Test
    void testEquality_InvalidUnit() {

        assertThrows(IllegalArgumentException.class, () ->
                new QuantityLength(1.0, null)
        );
    }
    
    // Checking same reference
    @Test
    void testEquality_SameReference() {

        QuantityLength quantity = new QuantityLength(1.0, LengthUnit.FEET);

        assertTrue(quantity.equals(quantity));
    }

    // Checking null comparison
    @Test
    void testEquality_NullComparison() {

        QuantityLength quantity = new QuantityLength(1.0, LengthUnit.FEET);

        assertFalse(quantity.equals(null));
    }
}