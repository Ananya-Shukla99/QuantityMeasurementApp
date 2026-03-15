package com.app.quantitymeasurement.unit;

/**
 * IMeasurable defines the contract for all unit enums.
 * Every unit must support conversion to/from its base unit.
 */
public interface IMeasurable {

    // ── Core Conversion ───────────────────────────────────────────────────────
    double convertToBaseUnit(double value);
    double convertFromBaseUnit(double baseValue);

    // ── Unit Info ─────────────────────────────────────────────────────────────
    String getUnitName();
    String getMeasurementType();

    // ── Unit Resolution ───────────────────────────────────────────────────────
    IMeasurable getUnitInstance(String unitName);

    // ── Arithmetic Support ────────────────────────────────────────────────────
    default boolean supportsArithmetic() {
        return true;
    }

    default void validateOperationSupport(String operation) {
        // default: supported for all units except Temperature
    }
}