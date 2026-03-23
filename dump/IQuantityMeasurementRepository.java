package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;

import java.util.List;

/**
 * IQuantityMeasurementRepository defines the contract for all repository
 * implementations (Cache and Database). Supports CRUD operations, filtering,
 * and resource management.
 */
public interface IQuantityMeasurementRepository {

	// ── SAVE ──────────────────────────────────────────────────────────────────

	/**
	 * Saves a QuantityMeasurementEntity to the repository.
	 */
	void save(QuantityMeasurementEntity entity);

	// ── GET ALL ───────────────────────────────────────────────────────────────

	/**
	 * Retrieves all measurements from the repository.
	 */
	List<QuantityMeasurementEntity> getAllMeasurements();

	// ── FIND BY OPERATION ─────────────────────────────────────────────────────

	/**
	 * Retrieves measurements filtered by operation type. e.g.
	 * findByOperation("ADD") → all ADD records
	 */
	List<QuantityMeasurementEntity> findByOperation(String operation);

	// ── FIND BY MEASUREMENT TYPE ──────────────────────────────────────────────

	/**
	 * Retrieves measurements filtered by measurement type. e.g.
	 * findByMeasurementType("LengthUnit") → all length records
	 */
	List<QuantityMeasurementEntity> findByMeasurementType(String measurementType);

	// ── COUNT ─────────────────────────────────────────────────────────────────

	/**
	 * Returns total count of measurements in the repository.
	 */
	int getCount();

	// ── DELETE ALL ────────────────────────────────────────────────────────────

	/**
	 * Deletes all measurements from the repository. Useful for testing and
	 * resetting application state.
	 */
	void deleteAll();

	// ── DEFAULT METHODS ───────────────────────────────────────────────────────

	/**
	 * Returns pool statistics for database-backed repositories. Default
	 * implementation returns a simple message.
	 */
	default String getPoolStatistics() {
		return "Pool statistics not available for this repository.";
	}

	/**
	 * Releases all resources held by the repository. e.g. closes database
	 * connections or clears caches.
	 */
	default void releaseResources() {
		// Default: no-op for cache repository
	}
}