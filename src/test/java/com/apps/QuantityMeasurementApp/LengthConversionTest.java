package com.apps.QuantityMeasurementApp;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.apps.quantitymeasurement.LengthUnit;
import com.apps.quantitymeasurement.QuantityLength;

// Length Conversion Testing Class 
public class LengthConversionTest {

    // Feet to Inches
    @Test
    void testConversion_FeetToInches() {
        double result = QuantityLength.convert(1.0, LengthUnit.FEET, LengthUnit.INCH);
        assertEquals(12.0, result, 1e-6);
    }

    // Inches to Feet
    @Test
    void testConversion_InchesToFeet() {
        double result = QuantityLength.convert(24.0, LengthUnit.INCH, LengthUnit.FEET);
        assertEquals(2.0, result, 1e-6);
    }

    // Yards to Inches
    @Test
    void testConversion_YardsToInches() {
        double result = QuantityLength.convert(1.0, LengthUnit.YARDS, LengthUnit.INCH);
        assertEquals(36.0, result, 1e-6);
    }

    // Centimeters to Inches
    @Test
    void testConversion_CentimetersToInches() {
        double result = QuantityLength.convert(2.54, LengthUnit.CENTIMETERS, LengthUnit.INCH);
        assertEquals(1.0, result, 1e-6);
    }

    // Round-trip test
    @Test
    void testConversion_RoundTrip_PreservesValue() {
        double original = 5.0;
        double converted = QuantityLength.convert(original, LengthUnit.FEET, LengthUnit.INCH);
        double back = QuantityLength.convert(converted, LengthUnit.INCH, LengthUnit.FEET);

        assertEquals(original, back, 1e-6);
    }

    // Zero value
    @Test
    void testConversion_ZeroValue() {
        double result = QuantityLength.convert(0.0, LengthUnit.FEET, LengthUnit.INCH);
        assertEquals(0.0, result, 1e-6);
    }

    // Negative value
    @Test
    void testConversion_NegativeValue() {
        double result = QuantityLength.convert(-1.0, LengthUnit.FEET, LengthUnit.INCH);
        assertEquals(-12.0, result, 1e-6);
    }

    // Invalid unit
    @Test
    void testConversion_InvalidUnit_Throws() {
        assertThrows(IllegalArgumentException.class, () ->
                QuantityLength.convert(1.0, null, LengthUnit.FEET));
    }

    // NaN value
    @Test
    void testConversion_NaNOrInfinite_Throws() {
        assertThrows(IllegalArgumentException.class, () ->
                QuantityLength.convert(Double.NaN, LengthUnit.FEET, LengthUnit.INCH));

        assertThrows(IllegalArgumentException.class, () ->
                QuantityLength.convert(Double.POSITIVE_INFINITY, LengthUnit.FEET, LengthUnit.INCH));
    }
}