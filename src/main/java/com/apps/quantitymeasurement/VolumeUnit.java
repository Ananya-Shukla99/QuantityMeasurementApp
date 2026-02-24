package com.apps.quantitymeasurement;

public enum VolumeUnit implements IMeasurable {

    // Enum constants with conversion factors relative to base unit (LITRE)
    LITRE(1.0),           // Base unit
    MILLILITRE(0.001),    // 1 mL = 0.001 L
    GALLON(3.78541);      // 1 gallon ≈ 3.78541 L

    // Attribute: conversion factor to base unit
    private final double conversionFactor;

    // Constructor
    VolumeUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    // Converts given value to base unit 
    @Override
    public double convertToBaseUnit(double value) {
        return value * conversionFactor;
    }

    // Converts base unit value litre to this unit
    @Override
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / conversionFactor;
    }

    // Returns readable unit name
    @Override
    public String getUnitName() {
        return name();
    }


}