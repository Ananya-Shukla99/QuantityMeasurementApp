package com.apps.quantitymeasurement;

public class QuantityMeasurementApp {

	// Demonstrates static conversion
	public static double demonstrateLengthConversion(double value, LengthUnit from, LengthUnit to) {

		double result = QuantityLength.convert(value, from, to);

		System.out.println("convert(" + value + ", " + from + ", " + to + ") → " + result);

		return result;
	}

	// Overloaded method using instance
	public static QuantityLength demonstrateLengthConversion(QuantityLength length, LengthUnit to) {

		QuantityLength result = length.convertTo(to);

		System.out.println(length + " converted to " + to + " : " + result);

		return result;
	}

	public static void main(String[] args) {

		// Conversion methods 
		demonstrateLengthConversion(1.0, LengthUnit.FEET, LengthUnit.INCH);
		demonstrateLengthConversion(3.0, LengthUnit.YARDS, LengthUnit.FEET);
		demonstrateLengthConversion(36.0, LengthUnit.INCH, LengthUnit.YARDS);
		demonstrateLengthConversion(1.0, LengthUnit.CENTIMETERS, LengthUnit.INCH);

		// Length object creation
		QuantityLength length = new QuantityLength(2.0, LengthUnit.YARDS);
		demonstrateLengthConversion(length, LengthUnit.FEET);
	}
}
