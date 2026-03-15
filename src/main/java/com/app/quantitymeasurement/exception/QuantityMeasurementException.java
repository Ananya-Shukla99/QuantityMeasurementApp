package com.app.quantitymeasurement.exception;

import java.util.logging.Logger;

/**
 * QuantityMeasurementException handles all business logic exceptions in the
 * Quantity Measurement application.
 */
public class QuantityMeasurementException extends RuntimeException {

	private static final Logger logger = Logger.getLogger(QuantityMeasurementException.class.getName());

	private static final long serialVersionUID = 1L;

	// ── Constructors ──────────────────────────────────────────────────────────

	/**
	 * Constructor with message only.
	 */
	public QuantityMeasurementException(String message) {
		super(message);
		logger.severe("QuantityMeasurementException: " + message);
	}

	/**
	 * Constructor with message and cause.
	 */
	public QuantityMeasurementException(String message, Throwable cause) {
		super(message, cause);
		logger.severe("QuantityMeasurementException: " + message + " | cause: " + cause.getMessage());
	}

	// ── toString ──────────────────────────────────────────────────────────────
	@Override
	public String toString() {
		return "QuantityMeasurementException { message: " + getMessage() + " }";
	}
}