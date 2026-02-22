package com.apps.quantitymeasurement;

public class QuantityMeasurementApp {

	public static void main(String[] args) {

        // Cross-unit comparison
		QuantityLength l1 = new QuantityLength(1.0, LengthUnit.FEET);
		QuantityLength l2 = new QuantityLength(12.0, LengthUnit.INCH);

		// Results of comparison
        System.out.println("Input values : " + l1 + " and " + l2);
        System.out.println("Output: Equal " + l1.equals(l2) );

        // Same-unit comparison
        QuantityLength l3 = new QuantityLength(1.0, LengthUnit.INCH);
        QuantityLength l4 = new QuantityLength(1.0, LengthUnit.INCH);

        // Results of comparison
        System.out.println("Input values: " + l3 + " and " + l4);
        System.out.println("Output: Equal " + l3.equals(l4) );
    }
}
