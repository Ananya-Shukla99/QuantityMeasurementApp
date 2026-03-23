package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * QuantityMeasurementCacheRepository is an in-memory implementation of
 * IQuantityMeasurementRepository using a List as cache storage.
 */
public class QuantityMeasurementCacheRepository implements IQuantityMeasurementRepository {

	private static final Logger logger = Logger.getLogger(QuantityMeasurementCacheRepository.class.getName());

	// ── Singleton ─────────────────────────────────────────────────────────────
	private static QuantityMeasurementCacheRepository instance;

	// ── Cache Storage ─────────────────────────────────────────────────────────
	private final List<QuantityMeasurementEntity> cache;

	// ── Private Constructor ───────────────────────────────────────────────────
	private QuantityMeasurementCacheRepository() {
		this.cache = new ArrayList<>();
		logger.info("QuantityMeasurementCacheRepository initialized.");
	}

	// ── getInstance ───────────────────────────────────────────────────────────
	public static QuantityMeasurementCacheRepository getInstance() {
		if (instance == null) {
			instance = new QuantityMeasurementCacheRepository();
		}
		return instance;
	}

	// ── SAVE ──────────────────────────────────────────────────────────────────
	@Override
	public void save(QuantityMeasurementEntity entity) {
		if (entity == null)
			throw new IllegalArgumentException("Entity cannot be null");

		cache.add(entity);
		logger.info("[Cache] Saved | operation: " + entity.operation);
	}

	// ── GET ALL ───────────────────────────────────────────────────────────────
	@Override
	public List<QuantityMeasurementEntity> getAllMeasurements() {
		logger.info("[Cache] Fetched all | count: " + cache.size());
		return new ArrayList<>(cache);
	}

	// ── FIND BY OPERATION ─────────────────────────────────────────────────────
	@Override
	public List<QuantityMeasurementEntity> findByOperation(String operation) {
		if (operation == null || operation.isEmpty())
			throw new IllegalArgumentException("Operation cannot be null");

		List<QuantityMeasurementEntity> results = cache.stream().filter(e -> operation.equalsIgnoreCase(e.operation))
				.collect(Collectors.toList());

		logger.info("[Cache] Fetched by operation: " + operation + " | count: " + results.size());

		return results;
	}

	// ── FIND BY MEASUREMENT TYPE ──────────────────────────────────────────────
	@Override
	public List<QuantityMeasurementEntity> findByMeasurementType(String measurementType) {

		if (measurementType == null || measurementType.isEmpty())
			throw new IllegalArgumentException("Measurement type cannot be null");

		List<QuantityMeasurementEntity> results = cache.stream()
				.filter(e -> measurementType.equalsIgnoreCase(e.thisMeasurementType)).collect(Collectors.toList());

		logger.info("[Cache] Fetched by type: " + measurementType + " | count: " + results.size());

		return results;
	}

	// ── COUNT ─────────────────────────────────────────────────────────────────
	@Override
	public int getCount() {
		logger.info("[Cache] Total count: " + cache.size());
		return cache.size();
	}

	// ── DELETE ALL ────────────────────────────────────────────────────────────
	@Override
	public void deleteAll() {
		int count = cache.size();
		cache.clear();
		logger.info("[Cache] Deleted all | count: " + count);
	}

	// ── POOL STATISTICS ───────────────────────────────────────────────────────
	@Override
	public String getPoolStatistics() {
		return "CacheRepository Stats { size: " + cache.size() + " }";
	}

	// ── RELEASE RESOURCES ─────────────────────────────────────────────────────
	@Override
	public void releaseResources() {
		cache.clear();
		logger.info("[Cache] Resources released. Cache cleared.");
	}
}