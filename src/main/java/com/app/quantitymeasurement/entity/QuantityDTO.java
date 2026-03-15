package com.app.quantitymeasurement.entity;

/**
 * QuantityDTO is the Data Transfer Object used for input and output
 * between the controller and service layers.
 * Contains inner enums for all supported unit types.
 */
public class QuantityDTO {

    // ── Fields ────────────────────────────────────────────────────────────────
    public double value;
    public String unitName;
    public String measurementType;

    // ── Constructor ───────────────────────────────────────────────────────────
    public QuantityDTO(double value, String unitName, String measurementType) {
        this.value           = value;
        this.unitName        = unitName;
        this.measurementType = measurementType;
    }

    @Override
    public String toString() {
        return value + " " + unitName;
    }

    // ── Inner Interface ───────────────────────────────────────────────────────
    public interface IMeasurableUnit {
        String getUnitName();
        String getMeasurementType();
    }

    // ── LENGTH ────────────────────────────────────────────────────────────────
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

    // ── WEIGHT ────────────────────────────────────────────────────────────────
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

    // ── VOLUME ────────────────────────────────────────────────────────────────
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

    // ── TEMPERATURE ───────────────────────────────────────────────────────────
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