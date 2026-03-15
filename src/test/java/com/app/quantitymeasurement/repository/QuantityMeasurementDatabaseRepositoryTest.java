package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for QuantityMeasurementDatabaseRepository. Uses H2 in-memory
 * database for isolated testing.
 */
public class QuantityMeasurementDatabaseRepositoryTest {

	private IQuantityMeasurementRepository repository;

	// ── Setup ─────────────────────────────────────────────────────────────────
	@Before
	public void setUp() {
		repository = QuantityMeasurementDatabaseRepository.getInstance();
		repository.deleteAll(); // clean slate before each test
	}

	// ── Teardown ──────────────────────────────────────────────────────────────
	@After
	public void tearDown() {
		repository.deleteAll(); // clean up after each test
	}

	// ── Helper ────────────────────────────────────────────────────────────────
	private QuantityMeasurementEntity createEntity(String operation, double thisValue, String thisUnit,
			String thisMeasurementType, double thatValue, String thatUnit, String thatMeasurementType,
			String resultString) {

		QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
		entity.operation = operation;
		entity.thisValue = thisValue;
		entity.thisUnit = thisUnit;
		entity.thisMeasurementType = thisMeasurementType;
		entity.thatValue = thatValue;
		entity.thatUnit = thatUnit;
		entity.thatMeasurementType = thatMeasurementType;
		entity.resultString = resultString;
		return entity;
	}

	private QuantityMeasurementEntity createNumericResultEntity(String operation, double thisValue, String thisUnit,
			String thisMeasurementType, double resultValue, String resultUnit, String resultMeasurementType) {

		QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
		entity.operation = operation;
		entity.thisValue = thisValue;
		entity.thisUnit = thisUnit;
		entity.thisMeasurementType = thisMeasurementType;
		entity.resultValue = resultValue;
		entity.resultUnit = resultUnit;
		entity.resultMeasurementType = resultMeasurementType;
		return entity;
	}

	// ── Tests ─────────────────────────────────────────────────────────────────

	@Test
	public void testDatabaseRepository_SaveEntity() {
		QuantityMeasurementEntity entity = createEntity("COMPARE", 1.0, "FEET", "LengthUnit", 12.0, "INCH",
				"LengthUnit", "Equal");

		repository.save(entity);

		assertEquals(1, repository.getCount());
	}

	@Test
	public void testDatabaseRepository_RetrieveAllMeasurements() {
		repository.save(createEntity("COMPARE", 1.0, "FEET", "LengthUnit", 12.0, "INCH", "LengthUnit", "Equal"));
		repository.save(createNumericResultEntity("CONVERT", 1.0, "FEET", "LengthUnit", 12.0, "INCH", "LengthUnit"));
		repository.save(createEntity("ADD", 1.0, "KILOGRAM", "WeightUnit", 1000.0, "GRAM", "WeightUnit", null));

		List<QuantityMeasurementEntity> all = repository.getAllMeasurements();

		assertEquals(3, all.size());
	}

	@Test
	public void testDatabaseRepository_QueryByOperation() {
		repository.save(createEntity("COMPARE", 1.0, "FEET", "LengthUnit", 12.0, "INCH", "LengthUnit", "Equal"));
		repository.save(createNumericResultEntity("CONVERT", 1.0, "FEET", "LengthUnit", 12.0, "INCH", "LengthUnit"));
		repository.save(createEntity("COMPARE", 1.0, "KILOGRAM", "WeightUnit", 1000.0, "GRAM", "WeightUnit", "Equal"));

		List<QuantityMeasurementEntity> compareResults = repository.findByOperation("COMPARE");

		assertEquals(2, compareResults.size());
		compareResults.forEach(e -> assertEquals("COMPARE", e.operation));
	}

	@Test
	public void testDatabaseRepository_QueryByMeasurementType() {
		repository.save(createEntity("COMPARE", 1.0, "FEET", "LengthUnit", 12.0, "INCH", "LengthUnit", "Equal"));
		repository.save(createEntity("COMPARE", 1.0, "KILOGRAM", "WeightUnit", 1000.0, "GRAM", "WeightUnit", "Equal"));
		repository.save(createNumericResultEntity("CONVERT", 1.0, "FEET", "LengthUnit", 12.0, "INCH", "LengthUnit"));

		List<QuantityMeasurementEntity> lengthResults = repository.findByMeasurementType("LengthUnit");

		assertEquals(2, lengthResults.size());
		lengthResults.forEach(e -> assertEquals("LengthUnit", e.thisMeasurementType));
	}

	@Test
	public void testDatabaseRepository_CountMeasurements() {
		repository.save(createEntity("COMPARE", 1.0, "FEET", "LengthUnit", 12.0, "INCH", "LengthUnit", "Equal"));
		repository.save(createNumericResultEntity("CONVERT", 1.0, "FEET", "LengthUnit", 12.0, "INCH", "LengthUnit"));
		repository.save(createEntity("ADD", 1.0, "KILOGRAM", "WeightUnit", 1000.0, "GRAM", "WeightUnit", null));

		assertEquals(3, repository.getCount());
	}

	@Test
	public void testDatabaseRepository_DeleteAll() {
		repository.save(createEntity("COMPARE", 1.0, "FEET", "LengthUnit", 12.0, "INCH", "LengthUnit", "Equal"));
		repository.save(createNumericResultEntity("CONVERT", 1.0, "FEET", "LengthUnit", 12.0, "INCH", "LengthUnit"));

		repository.deleteAll();

		assertEquals(0, repository.getCount());
	}

	@Test
	public void testSQLInjectionPrevention() {
		// Attempt SQL injection via operation parameter
		List<QuantityMeasurementEntity> results = repository
				.findByOperation("COMPARE'; DROP TABLE quantity_measurement_entity; --");

		// Table should still exist and return empty results
		assertNotNull(results);
		assertTrue(results.isEmpty());
		// Verify table still works
		assertEquals(0, repository.getCount());
	}

	@Test
	public void testDatabaseRepository_SaveAndRetrieve_VerifyData() {
		QuantityMeasurementEntity entity = createEntity("COMPARE", 1.0, "FEET", "LengthUnit", 12.0, "INCH",
				"LengthUnit", "Equal");

		repository.save(entity);

		List<QuantityMeasurementEntity> all = repository.getAllMeasurements();

		assertEquals(1, all.size());
		QuantityMeasurementEntity saved = all.get(0);
		assertEquals("COMPARE", saved.operation);
		assertEquals(1.0, saved.thisValue, 0.001);
		assertEquals("FEET", saved.thisUnit);
		assertEquals("LengthUnit", saved.thisMeasurementType);
		assertEquals(12.0, saved.thatValue, 0.001);
		assertEquals("INCH", saved.thatUnit);
		assertEquals("Equal", saved.resultString);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDatabaseRepository_SaveNull_ThrowsException() {
		repository.save(null);
	}

	@Test
	public void testDatabaseRepository_PoolStatistics() {
		String stats = repository.getPoolStatistics();
		assertNotNull(stats);
		assertTrue(stats.contains("available"));
		assertTrue(stats.contains("used"));
	}

	@Test
	public void testDatabaseRepository_SaveMultiple_LargeDataSet() {
		int count = 100;

		for (int i = 0; i < count; i++) {
			repository.save(createEntity("ADD", i, "FEET", "LengthUnit", i, "INCH", "LengthUnit", null));
		}

		assertEquals(count, repository.getCount());
	}

	@Test
	public void testDatabaseRepository_FindByOperation_EmptyResult() {
		List<QuantityMeasurementEntity> results = repository.findByOperation("DIVIDE");

		assertNotNull(results);
		assertTrue(results.isEmpty());
	}

	@Test
	public void testDatabaseRepository_FindByMeasurementType_EmptyResult() {
		List<QuantityMeasurementEntity> results = repository.findByMeasurementType("TemperatureUnit");

		assertNotNull(results);
		assertTrue(results.isEmpty());
	}

	@Test
	public void testDatabaseSchema_TablesCreated() {
		// If we can save and retrieve, tables exist
		repository.save(createEntity("COMPARE", 0.0, "CELSIUS", "TemperatureUnit", 32.0, "FAHRENHEIT",
				"TemperatureUnit", "Equal"));

		assertNotNull(repository.getAllMeasurements());
		assertTrue(repository.getCount() > 0);
	}
}