package com.apps.quantitymeasurement.repository;

import com.apps.quantitymeasurement.entity.QuantityMeasurementEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * H2-backed implementation of IQuantityMeasurementRepository. Persists every
 * operation (CONVERT, COMPARE, ADD, SUBTRACT, DIVIDE) to H2.
 */
public class QuantityMeasurementH2Repository implements IQuantityMeasurementRepository {

	private static final String INSERT_SQL = "INSERT INTO quantity_measurement "
			+ "(operation, this_value, this_unit, this_measurement_type, "
			+ " that_value, that_unit, that_measurement_type, "
			+ " result_value, result_unit, result_measurement_type, " + " result_string, is_error, error_message) "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private static final String SELECT_ALL_SQL = "SELECT * FROM quantity_measurement ORDER BY created_at DESC";

	// ── Singleton ────────────────────────────────────────────────────────────

	private static QuantityMeasurementH2Repository instance;

	private QuantityMeasurementH2Repository() {
	}

	public static QuantityMeasurementH2Repository getInstance() {
		if (instance == null) {
			instance = new QuantityMeasurementH2Repository();
		}
		return instance;
	}

	// ── save ─────────────────────────────────────────────────────────────────

	@Override
	public void save(QuantityMeasurementEntity entity) {
		if (entity == null)
			throw new IllegalArgumentException("Entity cannot be null");

		try (PreparedStatement ps = H2ConnectionManager.getConnection().prepareStatement(INSERT_SQL)) {

			ps.setString(1, entity.operation);

			// First operand
			ps.setDouble(2, entity.thisValue);
			ps.setString(3, entity.thisUnit);
			ps.setString(4, entity.thisMeasurementType);

			// Second operand (may be empty for CONVERT)
			ps.setDouble(5, entity.thatValue);
			ps.setString(6, entity.thatUnit);
			ps.setString(7, entity.thatMeasurementType);

			// Numeric result (ADD, SUBTRACT, CONVERT, DIVIDE)
			ps.setDouble(8, entity.resultValue);
			ps.setString(9, entity.resultUnit);
			ps.setString(10, entity.resultMeasurementType);

			// String result (COMPARE → "Equal"/"Not Equal")
			ps.setString(11, entity.resultString);

			// Error info
			ps.setBoolean(12, entity.isError);
			ps.setString(13, entity.errorMessage);

			ps.executeUpdate();
			System.out.println("[H2 Repository] Saved | operation: " + entity.operation);

		} catch (SQLException e) {
			System.err.println("[H2 Repository] Save failed: " + e.getMessage());
			throw new RuntimeException("Failed to save entity to H2", e);
		}
	}

	// ── getAllMeasurements ────────────────────────────────────────────────────

	@Override
	public List<QuantityMeasurementEntity> getAllMeasurements() {
		List<QuantityMeasurementEntity> results = new ArrayList<>();

		try (Statement stmt = H2ConnectionManager.getConnection().createStatement();
				ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {

			while (rs.next()) {
				QuantityMeasurementEntity e = mapRow(rs);
				results.add(e);
			}

		} catch (SQLException ex) {
			System.err.println("[H2 Repository] Fetch failed: " + ex.getMessage());
		}

		return results;
	}

	// ── Row mapper ───────────────────────────────────────────────────────────

	private QuantityMeasurementEntity mapRow(ResultSet rs) throws SQLException {
		// Use the error-state constructor as the base, then fill all fields manually
		QuantityMeasurementEntity e = new QuantityMeasurementEntity(null, null, rs.getString("operation"),
				rs.getString("error_message"), rs.getBoolean("is_error"));

		e.thisValue = rs.getDouble("this_value");
		e.thisUnit = rs.getString("this_unit");
		e.thisMeasurementType = rs.getString("this_measurement_type");

		e.thatValue = rs.getDouble("that_value");
		e.thatUnit = rs.getString("that_unit");
		e.thatMeasurementType = rs.getString("that_measurement_type");

		e.resultValue = rs.getDouble("result_value");
		e.resultUnit = rs.getString("result_unit");
		e.resultMeasurementType = rs.getString("result_measurement_type");

		e.resultString = rs.getString("result_string");

		return e;
	}
}