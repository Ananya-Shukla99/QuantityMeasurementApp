package com.app.quantitymeasurement.entity;

import com.app.quantitymeasurement.unit.IMeasurable;

/**
 * QuantityMeasurementEntity represents a single database record
 * for any quantity measurement operation (ADD, SUBTRACT, DIVIDE,
 * CONVERT, COMPARE). All fields are flattened for JDBC storage.
 */
public class QuantityMeasurementEntity {

    // ── First Operand ─────────────────────────────────────────────────────────
    public double thisValue;
    public String thisUnit;
    public String thisMeasurementType;

    // ── Second Operand (nullable for CONVERT) ─────────────────────────────────
    public double thatValue;
    public String thatUnit;
    public String thatMeasurementType;

    // ── Operation ─────────────────────────────────────────────────────────────
    // CONVERT, COMPARE, ADD, SUBTRACT, DIVIDE
    public String operation;

    // ── Numeric Result (ADD, SUBTRACT, CONVERT, DIVIDE) ──────────────────────
    public double resultValue;
    public String resultUnit;
    public String resultMeasurementType;

    // ── String Result (COMPARE → "Equal" / "Not Equal") ──────────────────────
    public String resultString;

    // ── Error Info ────────────────────────────────────────────────────────────
    public boolean isError;
    public String errorMessage;

    // ── Constructors ──────────────────────────────────────────────────────────

    /**
     * Default no-arg constructor.
     * Used by repository mapRow() when reading from database.
     */
    public QuantityMeasurementEntity() {
    }

    /**
     * Private base constructor — shared initialization for operands + operation.
     */
    private QuantityMeasurementEntity(
            QuantityModel<IMeasurable> thisQuantity,
            QuantityModel<IMeasurable> thatQuantity,
            String operation) {

        // First operand
        if (thisQuantity != null) {
            this.thisValue           = thisQuantity.value;
            this.thisUnit            = thisQuantity.unit.getUnitName();
            this.thisMeasurementType = thisQuantity.unit.getMeasurementType();
        }

        // Second operand
        if (thatQuantity != null) {
            this.thatValue           = thatQuantity.value;
            this.thatUnit            = thatQuantity.unit.getUnitName();
            this.thatMeasurementType = thatQuantity.unit.getMeasurementType();
        }

        this.operation = operation;
    }

    // ── Public Constructors ───────────────────────────────────────────────────

    /**
     * Constructor for string result operations (COMPARE).
     * e.g. result = "Equal" or "Not Equal"
     */
    public QuantityMeasurementEntity(
            QuantityModel<IMeasurable> thisQuantity,
            QuantityModel<IMeasurable> thatQuantity,
            String operation,
            String result) {
        this(thisQuantity, thatQuantity, operation);
        this.resultString = result;
    }

    /**
     * Constructor for numeric result operations
     * (ADD, SUBTRACT, CONVERT, DIVIDE).
     */
    public QuantityMeasurementEntity(
            QuantityModel<IMeasurable> thisQuantity,
            QuantityModel<IMeasurable> thatQuantity,
            String operation,
            QuantityModel<IMeasurable> result) {
        this(thisQuantity, thatQuantity, operation);
        this.resultValue           = result.value;
        this.resultUnit            = result.unit.getUnitName();
        this.resultMeasurementType = result.unit.getMeasurementType();
    }

    /**
     * Constructor for error state operations.
     */
    public QuantityMeasurementEntity(
            QuantityModel<IMeasurable> thisQuantity,
            QuantityModel<IMeasurable> thatQuantity,
            String operation,
            String errorMessage,
            boolean isError) {
        this(thisQuantity, thatQuantity, operation);
        this.errorMessage = errorMessage;
        this.isError      = isError;
    }

    // ── toString ──────────────────────────────────────────────────────────────

    @Override
    public String toString() {
        if (isError) {
            return "QuantityMeasurementEntity {" +
                    " operation: " + operation +
                    " | ERROR: "   + errorMessage +
                    " }";
        }
        if (resultString != null) {
            return "QuantityMeasurementEntity {" +
                    " operation: " + operation +
                    " | "          + thisValue + " " + thisUnit +
                    " vs "         + thatValue + " " + thatUnit +
                    " → "          + resultString +
                    " }";
        }
        return "QuantityMeasurementEntity {" +
                " operation: "  + operation +
                " | "           + thisValue + " " + thisUnit +
                " op "          + thatValue + " " + thatUnit +
                " → "           + resultValue + " " + resultUnit +
                " }";
    }
}