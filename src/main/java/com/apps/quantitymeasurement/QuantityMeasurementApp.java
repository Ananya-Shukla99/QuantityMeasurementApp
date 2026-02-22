package com.apps.quantitymeasurement;

public class QuantityMeasurementApp {

	// Static method for comparing feet
	public static boolean areFeetEqual(double value1, double value2) {
		Feet f1 = new Feet(value1);
		Feet f2 = new Feet(value2);
		return f1.equals(f2);
	}

	// Static method for comparing inches
	public static boolean areInchesEqual(double value1, double value2) {
		Inches i1 = new Inches(value1);
		Inches i2 = new Inches(value2);
		return i1.equals(i2);
	}

	public static void main(String[] args) {

		// Comparing two feet value
		boolean feetResult = areFeetEqual(1.0, 1.0);
		System.out.println("Input values : 1.0 ft and 1.0 ft");
		System.out.println("Output: Equal " + feetResult );

		// Comparing two Inches value
		boolean inchResult = areInchesEqual(1.0, 1.0);
		System.out.println("Input values : 1.0 inch and 1.0 inch");
		System.out.println("Output: Equal " + inchResult );

	}
}
