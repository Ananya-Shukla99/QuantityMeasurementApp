package com.apps.quantitymeasurement.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Manages a single persistent H2 embedded database connection. Uses file-based
 * storage so data survives restarts.
 */
public class H2ConnectionManager {

	// File-based H2 DB — stored at ./data/quantity_measurement
	private static final String JDBC_URL = "jdbc:h2:./data/quantity_measurement;AUTO_SERVER=TRUE";
	private static final String USER = "Ananya";
	private static final String PASSWORD = "1234";

	private static Connection connection;

	private H2ConnectionManager() {
	}

	/** Returns the shared connection, creating it on first call. */
	public static Connection getConnection() throws SQLException {
		if (connection == null || connection.isClosed()) {
			connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
			initSchema(connection);
		}
		return connection;
	}

	/** Reads and executes schema.sql to create tables if they don't exist. */
	private static void initSchema(Connection conn) throws SQLException {
		String sql = "CREATE TABLE IF NOT EXISTS quantity_measurement ("
				+ "  id                      BIGINT AUTO_INCREMENT PRIMARY KEY,"
				+ "  operation               VARCHAR(20)  NOT NULL," + "  this_value              DOUBLE,"
				+ "  this_unit               VARCHAR(50)," + "  this_measurement_type   VARCHAR(50),"
				+ "  that_value              DOUBLE," + "  that_unit               VARCHAR(50),"
				+ "  that_measurement_type   VARCHAR(50)," + "  result_value            DOUBLE,"
				+ "  result_unit             VARCHAR(50)," + "  result_measurement_type VARCHAR(50),"
				+ "  result_string           VARCHAR(100)," + "  is_error                BOOLEAN DEFAULT FALSE,"
				+ "  error_message           VARCHAR(255),"
				+ "  created_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP" + ")";

		try (Statement stmt = conn.createStatement()) {
			stmt.execute(sql);
			System.out.println("[H2] Schema initialized.");
		}
	}

	/** Closes the connection gracefully (call on app shutdown). */
	public static void close() {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
				System.out.println("[H2] Connection closed.");
			}
		} catch (SQLException e) {
			System.err.println("[H2] Error closing connection: " + e.getMessage());
		}
	}
}