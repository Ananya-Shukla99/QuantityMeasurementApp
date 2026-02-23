package com.apps.quantitymeasurement;

public class QuantityMeasurementApp {

	// Common display utility method
	private static void displayResult(String operation, Object result) {
		System.out.println(operation + " : " + result);
	}

	// GENERIC DEMONSTRATION METHODS

	// Equality demonstration
	public static <U extends IMeasurable> boolean demonstrateEquality(Quantity<U> q1, Quantity<U> q2) {

		if (q1 == null || q2 == null) {
			throw new IllegalArgumentException("Quantities cannot be null");
		}

		boolean result = q1.equals(q2);

		displayResult("Equality Check (" + q1 + ", " + q2 + ")", result);

		return result;
	}

	// Conversion demonstration
	public static <U extends IMeasurable> Quantity<U> demonstrateConversion(Quantity<U> quantity, U targetUnit) {

		if (quantity == null) {
			throw new IllegalArgumentException("Quantity cannot be null");
		}

		Quantity<U> result = quantity.convertTo(targetUnit);

		displayResult(quantity + " converted to " + targetUnit, result);

		return result;
	}

	// Addition demonstration (result in first operand unit)
	public static <U extends IMeasurable> Quantity<U> demonstrateAddition(Quantity<U> q1, Quantity<U> q2) {

		if (q1 == null || q2 == null) {
			throw new IllegalArgumentException("Quantities cannot be null");
		}

		Quantity<U> result = q1.add(q2);

		displayResult("Addition (" + q1 + " + " + q2 + ")", result);

		return result;
	}

	// Addition demonstration with explicit target unit
	public static <U extends IMeasurable> Quantity<U> demonstrateAddition(Quantity<U> q1, Quantity<U> q2,
			U targetUnit) {

		if (q1 == null || q2 == null) {
			throw new IllegalArgumentException("Quantities cannot be null");
		}

		Quantity<U> result = q1.add(q2, targetUnit);

		displayResult("Addition (" + q1 + " + " + q2 + ") in " + targetUnit, result);

		return result;
	}

	// Round-trip demonstration
	public static <U extends IMeasurable> void demonstrateRoundTrip(Quantity<U> quantity, U intermediateUnit) {

		if (quantity == null) {
			throw new IllegalArgumentException("Quantity cannot be null");
		}

		Quantity<U> roundTrip = quantity.convertTo(intermediateUnit).convertTo(quantity.getUnit());

		displayResult("Round Trip Test (" + quantity + ")", roundTrip);
	}

	// MAIN METHOD
	public static void main(String[] args) {

		// Length method
		System.out.println("***********************************************");
		Quantity<LengthUnit> length1 = new Quantity<>(1.0, LengthUnit.FEET);

		Quantity<LengthUnit> length2 = new Quantity<>(12.0, LengthUnit.INCH);

		demonstrateEquality(length1, length2);
		demonstrateConversion(length1, LengthUnit.INCH);
		demonstrateAddition(length1, length2);
		demonstrateAddition(length1, length2, LengthUnit.FEET);

		// Weight method
		System.out.println("***********************************************");
		Quantity<WeightUnit> weight1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);

		Quantity<WeightUnit> weight2 = new Quantity<>(1000.0, WeightUnit.GRAM);

		demonstrateEquality(weight1, weight2);
		demonstrateConversion(weight1, WeightUnit.GRAM);

		Quantity<WeightUnit> weight3 = new Quantity<>(2.20462, WeightUnit.POUND);

		demonstrateAddition(weight1, weight3);
		demonstrateAddition(weight1, weight3, WeightUnit.GRAM);
		demonstrateRoundTrip(weight1, WeightUnit.POUND);

		
		// Volume method 
		System.out.println("***********************************************");
		Quantity<VolumeUnit> volume1 = new Quantity<>(1.0, VolumeUnit.LITRE);

		Quantity<VolumeUnit> volume2 = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);

		demonstrateEquality(volume1, volume2);
		demonstrateConversion(volume1, VolumeUnit.MILLILITRE);
		demonstrateAddition(volume1, volume2);
		demonstrateAddition(volume1, volume2, VolumeUnit.LITRE);

		Quantity<VolumeUnit> volume3 = new Quantity<>(1.0, VolumeUnit.GALLON);

		demonstrateConversion(volume3, VolumeUnit.LITRE);
		demonstrateAddition(volume1, volume3);
		demonstrateAddition(volume1, volume3, VolumeUnit.MILLILITRE);
		demonstrateRoundTrip(volume1, VolumeUnit.GALLON);
		System.out.println("***********************************************");

	}
}