package com.apps.quantitymeasurement.repository;

import java.util.ArrayList;
import java.util.List;

import com.apps.quantitymeasurement.model.QuantityMeasurementEntity;

public class QuantityMeasurementCacheRepository implements IQuantityMeasurementRepository {

	// Holds the cached QuantityMeasurementEntity objects in memory for quick access
	List<QuantityMeasurementEntity> quantityMeasurementEntityCache;

	// Singleton instance of the repository
	private static QuantityMeasurementCacheRepository instance;

	// Private constructor to prevent instantiation from outside the class
	private QuantityMeasurementCacheRepository() {
		// Initialize the in-memory cache
		quantityMeasurementEntityCache = new ArrayList<>();
	}

	/**
	 * Get the singleton instance of the QuantityMeasurementCacheRepository.
	 */
	public static QuantityMeasurementCacheRepository getInstance() {
		if (instance == null) {
			instance = new QuantityMeasurementCacheRepository();
		}
		return instance;
	}

	/**
	 * Saves a QuantityMeasurementEntity to the in-memory cache.
	 */
	@Override
	public void save(QuantityMeasurementEntity entity) {
		if (entity == null)
			throw new IllegalArgumentException("Entity cannot be null");

		quantityMeasurementEntityCache.add(entity);
		System.out.println("[Repository] Saved entity | operation: " + entity.operation);
	}

	/**
	 * Retrieves all QuantityMeasurementEntity instances from the in-memory cache.
	 */
	@Override
	public List<QuantityMeasurementEntity> getAllMeasurements() {
		return new ArrayList<>(quantityMeasurementEntityCache);
	}
}