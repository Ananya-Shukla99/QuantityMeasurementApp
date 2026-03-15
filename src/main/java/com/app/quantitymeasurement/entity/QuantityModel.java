package com.app.quantitymeasurement.entity;

import com.app.quantitymeasurement.unit.IMeasurable;

/**
 * QuantityModel is an internal working object used inside the service layer.
 * Holds a value and its corresponding IMeasurable unit.
 */
public class QuantityModel<U extends IMeasurable> {

    public double value;
    public U unit;

    /**
     * Constructor to create a QuantityModel with a value and unit.
     */
    public QuantityModel(double value, U unit) {
        this.value = value;
        this.unit  = unit;
    }

    @Override
    public String toString() {
        return value + " " + unit.getUnitName();
    }
}