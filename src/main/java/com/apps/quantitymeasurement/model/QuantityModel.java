package com.apps.quantitymeasurement.model;

import com.apps.quantitymeasurement.IMeasurable;

public class QuantityModel<U extends IMeasurable> {

    public double value;
    public U unit;

    //Constructor to create a QuantityModel with a value and unit.
     
    public QuantityModel(double value, U unit) {
        this.value = value;
        this.unit = unit;
    }

    @Override
    public String toString() {
        return value + " " + unit.getUnitName();
    }
}