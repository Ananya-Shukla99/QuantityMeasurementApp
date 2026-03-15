package com.app.quantitymeasurement.exception;

/**
 * DatabaseException handles all database-related exceptions. Wraps JDBC
 * SQLExceptions with meaningful error messages and propagates them to the upper
 * layers of the application.
 */
public class DatabaseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	// ── Error Codes ───────────────────────────────────────────────────────────
	public static final String CONNECTION_ERROR = "DB_CONNECTION_ERROR";
	public static final String QUERY_ERROR = "DB_QUERY_ERROR";
	public static final String SAVE_ERROR = "DB_SAVE_ERROR";
	public static final String FETCH_ERROR = "DB_FETCH_ERROR";
	public static final String DELETE_ERROR = "DB_DELETE_ERROR";
	public static final String SCHEMA_ERROR = "DB_SCHEMA_ERROR";
	public static final String POOL_EXHAUSTED = "DB_POOL_EXHAUSTED";
	public static final String DRIVER_NOT_FOUND = "DB_DRIVER_NOT_FOUND";

	// ── Error Code Field ──────────────────────────────────────────────────────
	private final String errorCode;

	// ── Constructors ──────────────────────────────────────────────────────────

	/**
	 * Basic constructor with message only.
	 */
	public DatabaseException(String message) {
		super(message);
		this.errorCode = QUERY_ERROR;
	}

	/**
	 * Constructor with message and cause.
	 */
	public DatabaseException(String message, Throwable cause) {
		super(message, cause);
		this.errorCode = QUERY_ERROR;
	}

	/**
	 * Constructor with message, cause and specific error code.
	 */
	public DatabaseException(String message, Throwable cause, String errorCode) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	/**
	 * Constructor with message and specific error code.
	 */
	public DatabaseException(String message, String errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	// ── Getter ────────────────────────────────────────────────────────────────

	public String getErrorCode() {
		return errorCode;
	}

	// ── toString ──────────────────────────────────────────────────────────────

	@Override
	public String toString() {
		return "DatabaseException {" + " errorCode: " + errorCode + " | message: " + getMessage() + " }";
	}
}
