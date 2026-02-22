package com.apps.quantitymeasurement;

public class Feet {

	// Attributes
	private final double value;

	// Constructor
	public Feet(double value) {
		this.value = value;
	}

	// Getter
	public double getValue() {
		return value;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}

		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		Feet feet = (Feet) obj;

		return Double.compare(this.value, feet.value) == 0;
	}

	@Override
	public int hashCode() {
		return Double.hashCode(value);
	}

}
