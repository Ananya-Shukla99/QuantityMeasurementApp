package com.apps.QuantityMeasurementApp;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.apps.quantitymeasurement.Inches;
import com.apps.quantitymeasurement.QuantityMeasurementApp;

// Test Class
public class InchesMeasurementTesting {

    // Checking same value
    @Test
    public void testEquality_SameValue() {
    	
        boolean result = QuantityMeasurementApp.areInchesEqual(0.1, 0.1);
        assertEquals(true, result);
    }

    // Checking different values
    @Test
    public void testEquality_DifferentValue() {
       
        boolean result = QuantityMeasurementApp.areInchesEqual(0.1, 0.2);
        assertEquals(false, result);
    }

    // Checking the null value comparison
    @Test
    public void testEquality_NullComparison() {
        Inches i1 = new Inches(0.1);

        assertEquals(false, i1.equals(null));
    }

    // Checking non numeric value
  
	@Test
    public void testEquality_NonNumericInput() {
        Inches i1 = new Inches(0.1);

        assertEquals(false, i1.equals("Hello"));
    }

    // Checking same reference
    @Test
    public void testEquality_SameReference() {
        Inches i = new Inches(0.1);

        assertEquals(true, i.equals(i));
    }
    
}