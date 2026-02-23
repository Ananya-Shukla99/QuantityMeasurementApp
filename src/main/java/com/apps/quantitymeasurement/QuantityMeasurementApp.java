package com.apps.quantitymeasurement;


public class QuantityMeasurementApp {

   
	// Common display utility method
    public static boolean demonstrateLengthEquality(QuantityLength l1,
                                                    QuantityLength l2) {
        return l1.equals(l2);
    }

    // Helper method to print equality result
    private static void checkEquality(QuantityLength l1,
                                      QuantityLength l2) {

        System.out.println(l1 + " and " + l2 +
                " Equal: " + demonstrateLengthEquality(l1, l2));
    }

  

    // Static conversion using value + units
    public static void demonstrateLengthConversion(double value,
                                                   LengthUnit fromUnit,
                                                   LengthUnit toUnit) {

        double baseValue = fromUnit.convertToBaseUnit(value);
        double result = toUnit.convertFromBaseUnit(baseValue);

        System.out.println(value + " " + fromUnit +
                " is " + result + " " + toUnit);
    }

    // Object-based conversion
    public static void demonstrateLengthConversion(QuantityLength length,
                                                   LengthUnit toUnit) {

        System.out.println(length + " converted to " +
                toUnit + " is " + length.convertTo(toUnit));
    }

   

    public static void demonstrateLengthAddition(double value1,
                                                 LengthUnit unit1,
                                                 double value2,
                                                 LengthUnit unit2) {

        QuantityLength length1 = new QuantityLength(value1, unit1);
        QuantityLength length2 = new QuantityLength(value2, unit2);

        
        QuantityLength result = length1.add(length2);

        System.out.println(length1 + " + " + length2 +
                " = " + result);
    }

    // ===== Main Method =====

    public static void main(String[] args) {

        // UC1–UC4 Equality
        checkEquality(new QuantityLength(1.0, LengthUnit.FEET),
                      new QuantityLength(1.0, LengthUnit.FEET));

        checkEquality(new QuantityLength(1.0, LengthUnit.FEET),
                      new QuantityLength(12.0, LengthUnit.INCH));

        checkEquality(new QuantityLength(1.0, LengthUnit.YARDS),
                      new QuantityLength(3.0, LengthUnit.FEET));

        // UC5 Conversions
        demonstrateLengthConversion(1.0, LengthUnit.FEET, LengthUnit.INCH);
        demonstrateLengthConversion(3.0, LengthUnit.YARDS, LengthUnit.FEET);
        demonstrateLengthConversion(36.0, LengthUnit.INCH, LengthUnit.YARDS);

        demonstrateLengthConversion(
                new QuantityLength(2.0, LengthUnit.YARDS),
                LengthUnit.INCH);

        // UC6 Addition
        demonstrateLengthAddition(1.0, LengthUnit.FEET,
                                  12.0, LengthUnit.INCH);

        demonstrateLengthAddition(1.0, LengthUnit.YARDS,
                                  3.0, LengthUnit.FEET);
    }
}