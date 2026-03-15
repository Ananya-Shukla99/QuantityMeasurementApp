package com.apps.quantitymeasurement.entity;

public class QuantityDTO {

	// Fields
	public double value;
	public IMeasurableUnit unit;

	// Constructor
	public QuantityDTO(double value, IMeasurableUnit unit) {
		this.value = value;
		this.unit = unit;
	}

	@Override
	public String toString() {
		return value + " " + unit.getUnitName();
	}

	// ─────────────────────────────────────────────────────────────────────────
	// INNER INTERFACE
	// ─────────────────────────────────────────────────────────────────────────

	public interface IMeasurableUnit {
		public String getUnitName();

		public String getMeasurementType();
	}

	// ─────────────────────────────────────────────────────────────────────────
	// INNER ENUMS — mirror the core unit enums for easy service-layer mapping
	// ─────────────────────────────────────────────────────────────────────────

	/**
	 * Length units: FEET, INCHES, YARDS, CENTIMETERS Maps to core LengthUnit enum
	 * in the service layer.
	 */
	public enum LengthUnit implements IMeasurableUnit {
		FEET, INCH, YARDS, CENTIMETERS;

		@Override
		public String getUnitName() {
			return this.name();
		}

		@Override
		public String getMeasurementType() {
			return this.getClass().getSimpleName();
		}
	}

	/**
	 * Weight units: GRAM, KILOGRAM, POUND Maps to core WeightUnit enum in the
	 * service layer.
	 */
	public enum WeightUnit implements IMeasurableUnit {
		GRAM, KILOGRAM, POUND;

		@Override
		public String getUnitName() {
			return this.name();
		}

		@Override
		public String getMeasurementType() {
			return this.getClass().getSimpleName();
		}
	}

	/**
	 * Volume units: LITRE, MILLILITRE, GALLON Maps to core VolumeUnit enum in the
	 * service layer.
	 */
	public enum VolumeUnit implements IMeasurableUnit {
		LITRE, MILLILITRE, GALLON;

		@Override
		public String getUnitName() {
			return this.name();
		}

		@Override
		public String getMeasurementType() {
			return this.getClass().getSimpleName();
		}
	}

	/**
	 * Temperature units: CELSIUS, FAHRENHEIT, KELVIN Maps to core TemperatureUnit
	 * enum in the service layer.
	 */
	public enum TemperatureUnit implements IMeasurableUnit {
		CELSIUS, FAHRENHEIT, KELVIN;

		@Override
		public String getUnitName() {
			return this.name();
		}

		@Override
		public String getMeasurementType() {
			return this.getClass().getSimpleName();
		}
	}
}