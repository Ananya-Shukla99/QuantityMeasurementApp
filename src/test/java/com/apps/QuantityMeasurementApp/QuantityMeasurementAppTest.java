package com.apps.QuantityMeasurementApp;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.apps.quantitymeasurement.LengthUnit;
import com.apps.quantitymeasurement.QuantityLength;

// Testing Class
public class QuantityMeasurementAppTest {

    // Feet target unit
    @Test
    void testAddition_ExplicitTargetUnit_Feet() {

        QuantityLength result = new QuantityLength(1.0, LengthUnit.FEET)
                .add(new QuantityLength(12.0, LengthUnit.INCH), LengthUnit.FEET);

        assertEquals(new QuantityLength(2.0, LengthUnit.FEET), result);
    }

    // Inches target unit
    @Test
    void testAddition_ExplicitTargetUnit_Inches() {

        QuantityLength result = new QuantityLength(1.0, LengthUnit.FEET)
                .add(new QuantityLength(12.0, LengthUnit.INCH), LengthUnit.INCH);

        assertEquals(new QuantityLength(24.0, LengthUnit.INCH), result);
    }

    // Yards precision test
    @Test
    void testAddition_ExplicitTargetUnit_Yards() {

        QuantityLength result = new QuantityLength(1.0, LengthUnit.FEET)
                .add(new QuantityLength(12.0, LengthUnit.INCH), LengthUnit.YARDS);

        QuantityLength expected = new QuantityLength(
                (1.0 * LengthUnit.FEET.getConversionFactor()
                        + 12.0 * LengthUnit.INCH.getConversionFactor())
                        / LengthUnit.YARDS.getConversionFactor(),
                LengthUnit.YARDS);

        assertEquals(expected, result);
    }

    // Centimeter precision test
    @Test
    void testAddition_ExplicitTargetUnit_Centimeters() {

        QuantityLength result = new QuantityLength(1.0, LengthUnit.INCH)
                .add(new QuantityLength(1.0, LengthUnit.INCH), LengthUnit.CENTIMETERS);

        QuantityLength expected = new QuantityLength(
                (2.0 * LengthUnit.INCH.getConversionFactor())
                        / LengthUnit.CENTIMETERS.getConversionFactor(),
                LengthUnit.CENTIMETERS);

        assertEquals(expected, result);
    }

    // Same as first operand unit
    @Test
    void testAddition_ExplicitTargetUnit_SameAsFirstOperand() {

        QuantityLength result = new QuantityLength(2.0, LengthUnit.YARDS)
                .add(new QuantityLength(3.0, LengthUnit.FEET), LengthUnit.YARDS);

        assertEquals(new QuantityLength(3.0, LengthUnit.YARDS), result);
    }

    // Same as second operand unit
    @Test
    void testAddition_ExplicitTargetUnit_SameAsSecondOperand() {

        QuantityLength result = new QuantityLength(2.0, LengthUnit.YARDS)
                .add(new QuantityLength(3.0, LengthUnit.FEET), LengthUnit.FEET);

        assertEquals(new QuantityLength(9.0, LengthUnit.FEET), result);
    }

    // Commutativity test
    @Test
    void testAddition_ExplicitTargetUnit_Commutativity() {

        QuantityLength result1 = new QuantityLength(1.0, LengthUnit.FEET)
                .add(new QuantityLength(12.0, LengthUnit.INCH), LengthUnit.YARDS);

        QuantityLength result2 = new QuantityLength(12.0, LengthUnit.INCH)
                .add(new QuantityLength(1.0, LengthUnit.FEET), LengthUnit.YARDS);

        assertEquals(result1, result2);
    }

    // Zero operand test
    @Test
    void testAddition_ExplicitTargetUnit_WithZero() {

        QuantityLength result = new QuantityLength(5.0, LengthUnit.FEET)
                .add(new QuantityLength(0.0, LengthUnit.INCH), LengthUnit.YARDS);

        QuantityLength expected = new QuantityLength(
                (5.0 * LengthUnit.FEET.getConversionFactor())
                        / LengthUnit.YARDS.getConversionFactor(),
                LengthUnit.YARDS);

        assertEquals(expected, result);
    }

    // Negative values test
    @Test
    void testAddition_ExplicitTargetUnit_NegativeValues() {

        QuantityLength result = new QuantityLength(5.0, LengthUnit.FEET)
                .add(new QuantityLength(-2.0, LengthUnit.FEET), LengthUnit.INCH);

        QuantityLength expected = new QuantityLength(
                (5.0 * LengthUnit.FEET.getConversionFactor()
                        - 2.0 * LengthUnit.FEET.getConversionFactor())
                        / LengthUnit.INCH.getConversionFactor(),
                LengthUnit.INCH);

        assertEquals(expected, result);
    }

    // Null target unit test
    @Test
    void testAddition_ExplicitTargetUnit_NullTargetUnit() {

        assertThrows(IllegalArgumentException.class, () ->
                new QuantityLength(1.0, LengthUnit.FEET)
                        .add(new QuantityLength(12.0, LengthUnit.INCH), null));
    }

}