package com.app.quantitymeasurement.controller;

import com.app.quantitymeasurement.entity.QuantityDTO;
import com.app.quantitymeasurement.exception.QuantityMeasurementException;
import com.app.quantitymeasurement.service.IQuantityMeasurementService;

import java.util.logging.Logger;

/**
 * QuantityMeasurementController handles all incoming requests
 * and delegates to the service layer for business logic.
 * Catches exceptions and logs results.
 */
public class QuantityMeasurementController {

    private static final Logger logger = Logger.getLogger(
            QuantityMeasurementController.class.getName()
    );

    // ── Service Dependency (injected via constructor) ─────────────────────────
    private final IQuantityMeasurementService service;

    // ── Display Separator ─────────────────────────────────────────────────────
    private static final String SEPARATOR = " : ";

    // ── Constructor ───────────────────────────────────────────────────────────
    public QuantityMeasurementController(
            IQuantityMeasurementService service) {
        this.service = service;
        logger.info("QuantityMeasurementController initialized.");
    }

    // ── COMPARE ───────────────────────────────────────────────────────────────

    /**
     * Performs equality comparison between two quantities.
     */
    public boolean performComparison(QuantityDTO q1, QuantityDTO q2) {
        try {
            boolean result = service.compare(q1, q2);
            displayResult(
                    "Equality Check (" + q1 + ", " + q2 + ")",
                    result
            );
            logger.info("COMPARE: " + q1 + " vs " + q2
                    + " → " + result);
            return result;

        } catch (QuantityMeasurementException e) {
            logger.severe("Comparison Error: " + e.getMessage());
            System.out.println("Comparison Error: " + e.getMessage());
            return false;
        }
    }

    // ── CONVERT ───────────────────────────────────────────────────────────────

    /**
     * Performs unit conversion on a given quantity.
     */
    public QuantityDTO performConversion(QuantityDTO quantity,
                                         String targetUnit) {
        try {
            QuantityDTO result = service.convert(quantity, targetUnit);
            displayResult(
                    quantity + " converted to " + targetUnit,
                    result
            );
            logger.info("CONVERT: " + quantity
                    + " → " + targetUnit + " = " + result);
            return result;

        } catch (QuantityMeasurementException e) {
            logger.severe("Conversion Error: " + e.getMessage());
            System.out.println("Conversion Error: " + e.getMessage());
            return null;
        }
    }

    // ── ADD ───────────────────────────────────────────────────────────────────

    /**
     * Performs addition — result in unit of first operand.
     */
    public QuantityDTO performAddition(QuantityDTO q1, QuantityDTO q2) {
        try {
            QuantityDTO result = service.add(q1, q2);
            displayResult(
                    "Addition (" + q1 + " + " + q2 + ")",
                    result
            );
            logger.info("ADD: " + q1 + " + " + q2 + " → " + result);
            return result;

        } catch (QuantityMeasurementException e) {
            logger.severe("Addition Error: " + e.getMessage());
            System.out.println("Addition Error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Performs addition — result in specified target unit.
     */
    public QuantityDTO performAddition(QuantityDTO q1, QuantityDTO q2,
                                        String targetUnit) {
        try {
            QuantityDTO result = service.add(q1, q2, targetUnit);
            displayResult(
                    "Addition (" + q1 + " + " + q2
                    + ") in " + targetUnit,
                    result
            );
            logger.info("ADD (target: " + targetUnit + "): "
                    + q1 + " + " + q2 + " → " + result);
            return result;

        } catch (QuantityMeasurementException e) {
            logger.severe("Addition Error: " + e.getMessage());
            System.out.println("Addition Error: " + e.getMessage());
            return null;
        }
    }

    // ── SUBTRACT ──────────────────────────────────────────────────────────────

    /**
     * Performs subtraction of second quantity from first.
     */
    public QuantityDTO performSubtraction(QuantityDTO q1, QuantityDTO q2) {
        try {
            QuantityDTO result = service.subtract(q1, q2);
            displayResult(
                    "Subtraction (" + q1 + " - " + q2 + ")",
                    result
            );
            logger.info("SUBTRACT: " + q1 + " - " + q2
                    + " → " + result);
            return result;

        } catch (QuantityMeasurementException e) {
            logger.severe("Subtraction Error: " + e.getMessage());
            System.out.println("Subtraction Error: " + e.getMessage());
            return null;
        }
    }

    // ── DIVIDE ────────────────────────────────────────────────────────────────

    /**
     * Performs division of first quantity by second.
     */
    public double performDivision(QuantityDTO q1, QuantityDTO q2) {
        try {
            double result = service.divide(q1, q2);
            displayResult(
                    "Division (" + q1 + " / " + q2 + ")",
                    result
            );
            logger.info("DIVIDE: " + q1 + " / " + q2
                    + " → " + result);
            return result;

        } catch (QuantityMeasurementException e) {
            logger.severe("Division Error: " + e.getMessage());
            System.out.println("Division Error: " + e.getMessage());
            return 0.0;
        }
    }

    // ── DISPLAY UTILITY ───────────────────────────────────────────────────────

    /**
     * Prints operation result to console.
     */
    private void displayResult(String operation, Object result) {
        System.out.println(operation + SEPARATOR + result);
    }
}