package com.apps.quantitymeasurement;

public class QuantityMeasurementApp {

	// Common display utility method
	private static void displayResult(String operation, Object result) {
		System.out.println(operation + " : " + result);
	}

	// Length implementation

	// Static conversion demonstration
	public static double demonstrateLengthConversion(double value, LengthUnit from, LengthUnit to) {

		double result = QuantityLength.convert(value, from, to);

		displayResult("convert(" + value + ", " + from + ", " + to + ")", result);

		return result;
	}

	// Instance conversion demonstration (Overloaded method)
	public static QuantityLength demonstrateLengthConversion(QuantityLength length, LengthUnit to) {

		if (length == null) {
			throw new IllegalArgumentException("Length object cannot be null");
		}

		QuantityLength result = length.convertTo(to);

		displayResult(length + " converted to " + to, result);

		return result;
	}

	// Weight implementation

	// Static equality demonstration
	public static boolean demonstrateWeightEquality(double value1, WeightUnit unit1, double value2, WeightUnit unit2) {

		QuantityWeight w1 = new QuantityWeight(value1, unit1);
		QuantityWeight w2 = new QuantityWeight(value2, unit2);

		boolean result = w1.equals(w2);

		displayResult("equals(" + value1 + " " + unit1 + ", " + value2 + " " + unit2 + ")", result);

		return result;
	}

	// Instance equality demonstration (Overloaded)
	public static boolean demonstrateWeightEquality(QuantityWeight w1, QuantityWeight w2) {

		if (w1 == null || w2 == null) {
			throw new IllegalArgumentException("Weight objects cannot be null");
		}

		boolean result = w1.equals(w2);

		displayResult("Equality Check (" + w1 + " , " + w2 + ")", result);

		return result;
	}

	// Static addition demonstration
	public static QuantityWeight demonstrateWeightAddition(double value1, WeightUnit unit1, double value2,
			WeightUnit unit2) {

		QuantityWeight w1 = new QuantityWeight(value1, unit1);
		QuantityWeight w2 = new QuantityWeight(value2, unit2);

		QuantityWeight result = w1.add(w2);

		displayResult("add(" + value1 + " " + unit1 + ", " + value2 + " " + unit2 + ")", result);

		return result;
	}

	// Instance addition demonstration (Overloaded)
	public static QuantityWeight demonstrateWeightAddition(QuantityWeight w1, QuantityWeight w2) {

		if (w1 == null || w2 == null) {
			throw new IllegalArgumentException("Weight objects cannot be null");
		}

		QuantityWeight result = w1.add(w2);

		displayResult("Addition (" + w1 + " + " + w2 + ")", result);

		return result;
	}

	// Static addition with explicit target unit
	public static QuantityWeight demonstrateWeightAddition(double value1, WeightUnit unit1, double value2,
			WeightUnit unit2, WeightUnit targetUnit) {

		QuantityWeight w1 = new QuantityWeight(value1, unit1);
		QuantityWeight w2 = new QuantityWeight(value2, unit2);

		QuantityWeight result = w1.add(w2, targetUnit);

		displayResult("add(" + value1 + " " + unit1 + ", " + value2 + " " + unit2 + ") in " + targetUnit, result);

		return result;
	}

	// Static round-trip demonstration
	public static void demonstrateRoundTrip(double value, WeightUnit unit, WeightUnit intermediateUnit) {

		QuantityWeight weight = new QuantityWeight(value, unit);

		QuantityWeight roundTrip = weight.convertTo(intermediateUnit).convertTo(unit);

		displayResult("Round Trip Test (" + value + " " + unit + ")", roundTrip);
	}

	public static void main(String[] args) {

		// Length demonstration
		demonstrateLengthConversion(1.0, LengthUnit.FEET, LengthUnit.INCH);
		demonstrateLengthConversion(3.0, LengthUnit.YARDS, LengthUnit.FEET);
		demonstrateLengthConversion(36.0, LengthUnit.INCH, LengthUnit.YARDS);
		demonstrateLengthConversion(1.0, LengthUnit.CENTIMETERS, LengthUnit.INCH);

		QuantityLength length = new QuantityLength(2.0, LengthUnit.YARDS);
		demonstrateLengthConversion(length, LengthUnit.FEET);

		// Weight demonstrator
		
		QuantityWeight w1 = new QuantityWeight(2.0, WeightUnit.KILOGRAM);
		QuantityWeight w2 = new QuantityWeight(2000.0, WeightUnit.GRAM);
		
		demonstrateWeightEquality(1.0, WeightUnit.KILOGRAM, 1000.0, WeightUnit.GRAM);
		demonstrateWeightEquality(w1, w2);
		demonstrateWeightAddition(1.0, WeightUnit.KILOGRAM, 500.0, WeightUnit.GRAM);
		demonstrateWeightAddition(w1, w2);
		demonstrateWeightAddition(1.0, WeightUnit.KILOGRAM, 500.0, WeightUnit.GRAM, WeightUnit.GRAM);
		demonstrateRoundTrip(1.0, WeightUnit.KILOGRAM, WeightUnit.POUND);
	}
}