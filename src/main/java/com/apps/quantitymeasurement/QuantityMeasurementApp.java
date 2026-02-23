package com.apps.quantitymeasurement;

public class QuantityMeasurementApp {

	// Static equality demonstration
	public static boolean demonstrateLengthEquality(QuantityLength l1, QuantityLength l2) {
		return l1.equals(l2);
	}

	// Private helper for printing equality
	private static void checkEquality(QuantityLength l1, QuantityLength l2) {

		System.out.println(l1 + " and " + l2 + " Equal: " + demonstrateLengthEquality(l1, l2));
	}


	// Static method for length conversion 
	public static void demonstrateLengthConversion(double value, LengthUnit fromUnit, LengthUnit toUnit) {

		double result = LengthUnit.valueOf(fromUnit.name()).convertFromBaseUnit(fromUnit.convertToBaseUnit(value));

		System.out.println(value + " " + fromUnit + " is " + result + " " + toUnit);
	}

	// Object-based conversion
	public static void demonstrateLengthConversion(QuantityLength length, LengthUnit toUnit) {

		System.out.println(length + " converted to " + toUnit + " is " + length.convertTo(toUnit));
	}

	// Static method for length addition 
	public static void demonstrateLengthAddition(double value1, LengthUnit unit1, double value2, LengthUnit unit2) {

		QuantityLength length1 = new QuantityLength(value1, unit1);
		QuantityLength length2 = new QuantityLength(value2, unit2);

		QuantityLength result = length1.add(length2, LengthUnit.FEET);

		System.out.println(length1 + " + " + length2 + " = " + result);
	}

	// Static method for length addition with target type
	public static void demonstrateLengthAddition(double value1, LengthUnit unit1, double value2, LengthUnit unit2,
			LengthUnit targetUnit) {

		QuantityLength length1 = new QuantityLength(value1, unit1);
		QuantityLength length2 = new QuantityLength(value2, unit2);

		QuantityLength result = length1.add(length2, targetUnit);

		System.out.println(length1 + " + " + length2 + " = " + result);
	}

	public static void main(String[] args) {

		// Equality method 
		checkEquality(new QuantityLength(1.0, LengthUnit.FEET), new QuantityLength(12.0, LengthUnit.INCH));

		// Conversion method 
		demonstrateLengthConversion(1.0, LengthUnit.FEET, LengthUnit.INCH);

		demonstrateLengthConversion(new QuantityLength(2.0, LengthUnit.YARDS), LengthUnit.FEET);

		// Addition
		demonstrateLengthAddition(1.0, LengthUnit.FEET, 12.0, LengthUnit.INCH);

		// Addition with target unit
		demonstrateLengthAddition(1.0, LengthUnit.FEET, 12.0, LengthUnit.INCH, LengthUnit.YARDS);
	}
}