package com.app.qma.unit;

public interface IMeasurable {

    double convertToBaseUnit(double value);

    double convertFromBaseUnit(double baseValue);

    String getUnitName();

    String getMeasurementType();

    IMeasurable getUnitInstance(String unitName);

    default void validateOperationSupport(String operation) {
    }
}

