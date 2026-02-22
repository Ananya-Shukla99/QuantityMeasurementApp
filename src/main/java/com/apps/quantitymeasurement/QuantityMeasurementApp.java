package com.apps.quantitymeasurement;

public class QuantityMeasurementApp {

	// static method for comparing different units
	public static void demonstrateLengthComparison(double value1, LengthUnit unit1, double value2, LengthUnit unit2) {

		QuantityLength length1 = new QuantityLength(value1, unit1);
		QuantityLength length2 = new QuantityLength(value2, unit2);

		boolean result = length1.equals(length2);

		System.out.println("Length 1: " + length1);
		System.out.println("Length 2: " + length2);
		System.out.println("Are lengths equal? " + result);
	}

	public static void main(String[] args) {

		// Feet and Inches
		demonstrateLengthComparison(1.0, LengthUnit.FEET, 12.0, LengthUnit.INCH);

		// Yards and Inches
		demonstrateLengthComparison(1.0, LengthUnit.YARDS, 36.0, LengthUnit.INCH);

		// Centimeters and Inches
		demonstrateLengthComparison(100.0, LengthUnit.CENTIMETERS, 39.3701, LengthUnit.INCH);

		// Feet and Yards
		demonstrateLengthComparison(3.0, LengthUnit.FEET, 1.0, LengthUnit.YARDS);

		// Centimeters and Feet
		demonstrateLengthComparison(30.48, LengthUnit.CENTIMETERS, 1.0, LengthUnit.FEET);
	}
}
