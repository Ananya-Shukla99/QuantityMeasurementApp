package com.apps.quantitymeasurement;

import java.util.Scanner;

public class QuantityMeasurementApp {
	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		double value = sc.nextDouble();
		double value2 = sc.nextDouble();
		Feet f1 = new Feet(value);
		Feet f2 = new Feet(value2);

		boolean flag = f1.equals(f2);
		if (flag) {
			System.out.println(value + " is equals to" + value2);
		} else {
			System.out.println(value + " is not equals to" + value2);
		}
		sc.close();
	}
}
