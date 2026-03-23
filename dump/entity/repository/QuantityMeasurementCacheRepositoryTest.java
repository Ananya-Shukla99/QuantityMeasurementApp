package com.app.quantitymeasurement.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.app.quantitymeasurement.model.QuantityMeasurementEntity;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for QuantityMeasurementCacheRepository.
 * Tests in-memory cache operations.
 */
public class QuantityMeasurementCacheRepositoryTest {

    private IQuantityMeasurementRepository repository;

    // ── Setup ─────────────────────────────────────────────────────────────────
    @Before
    public void setUp() {
        repository = QuantityMeasurementCacheRepository.getInstance();
        repository.deleteAll();
    }

    // ── Teardown ──────────────────────────────────────────────────────────────
    @After
    public void tearDown() {
        repository.deleteAll();
    }

    // ── Helper ────────────────────────────────────────────────────────────────
    private QuantityMeasurementEntity createEntity(
            String operation,
            double thisValue, String thisUnit,
            String thisMeasurementType,
            String resultString) {

        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.operation           = operation;
        entity.thisValue           = thisValue;
        entity.thisUnit            = thisUnit;
        entity.thisMeasurementType = thisMeasurementType;
        entity.resultString        = resultString;
        return entity;
    }

    // ── Tests ─────────────────────────────────────────────────────────────────

    @Test
    public void testCacheRepository_SaveEntity() {
        QuantityMeasurementEntity entity = createEntity(
                "COMPARE",
                1.0, "FEET", "LengthUnit",
                "Equal"
        );

        repository.save(entity);

        assertEquals(1, repository.getCount());
    }

    @Test
    public void testCacheRepository_GetAllMeasurements() {
        repository.save(createEntity(
                "COMPARE",
                1.0, "FEET", "LengthUnit", "Equal"
        ));
        repository.save(createEntity(
                "CONVERT",
                1.0, "FEET", "LengthUnit", null
        ));
        repository.save(createEntity(
                "ADD",
                1.0, "KILOGRAM", "WeightUnit", null
        ));

        List<QuantityMeasurementEntity> all =
                repository.getAllMeasurements();

        assertEquals(3, all.size());
    }

    @Test
    public void testCacheRepository_FindByOperation() {
        repository.save(createEntity(
                "COMPARE",
                1.0, "FEET", "LengthUnit", "Equal"
        ));
        repository.save(createEntity(
                "COMPARE",
                1.0, "KILOGRAM", "WeightUnit", "Equal"
        ));
        repository.save(createEntity(
                "CONVERT",
                1.0, "FEET", "LengthUnit", null
        ));

        List<QuantityMeasurementEntity> results =
                repository.findByOperation("COMPARE");

        assertEquals(2, results.size());
        results.forEach(e ->
                assertEquals("COMPARE", e.operation)
        );
    }

    @Test
    public void testCacheRepository_FindByMeasurementType() {
        repository.save(createEntity(
                "COMPARE",
                1.0, "FEET", "LengthUnit", "Equal"
        ));
        repository.save(createEntity(
                "COMPARE",
                1.0, "KILOGRAM", "WeightUnit", "Equal"
        ));
        repository.save(createEntity(
                "CONVERT",
                1.0, "FEET", "LengthUnit", null
        ));

        List<QuantityMeasurementEntity> results =
                repository.findByMeasurementType("LengthUnit");

        assertEquals(2, results.size());
        results.forEach(e ->
                assertEquals("LengthUnit", e.thisMeasurementType)
        );
    }

    @Test
    public void testCacheRepository_Count() {
        repository.save(createEntity(
                "COMPARE",
                1.0, "FEET", "LengthUnit", "Equal"
        ));
        repository.save(createEntity(
                "CONVERT",
                1.0, "FEET", "LengthUnit", null
        ));

        assertEquals(2, repository.getCount());
    }

    @Test
    public void testCacheRepository_DeleteAll() {
        repository.save(createEntity(
                "COMPARE",
                1.0, "FEET", "LengthUnit", "Equal"
        ));
        repository.save(createEntity(
                "CONVERT",
                1.0, "FEET", "LengthUnit", null
        ));

        repository.deleteAll();

        assertEquals(0, repository.getCount());
    }

    @Test
    public void testCacheRepository_FindByOperation_EmptyResult() {
        List<QuantityMeasurementEntity> results =
                repository.findByOperation("DIVIDE");

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testCacheRepository_FindByMeasurementType_EmptyResult() {
        List<QuantityMeasurementEntity> results =
                repository.findByMeasurementType("TemperatureUnit");

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCacheRepository_SaveNull_ThrowsException() {
        repository.save(null);
    }

    @Test
    public void testCacheRepository_PoolStatistics() {
        String stats = repository.getPoolStatistics();
        assertNotNull(stats);
        assertTrue(stats.contains("size"));
    }

    @Test
    public void testCacheRepository_ReleaseResources() {
        repository.save(createEntity(
                "COMPARE",
                1.0, "FEET", "LengthUnit", "Equal"
        ));

        repository.releaseResources();

        assertEquals(0, repository.getCount());
    }

    @Test
    public void testCacheRepository_GetAllMeasurements_ReturnsCopy() {
        repository.save(createEntity(
                "COMPARE",
                1.0, "FEET", "LengthUnit", "Equal"
        ));

        List<QuantityMeasurementEntity> list1 =
                repository.getAllMeasurements();
        List<QuantityMeasurementEntity> list2 =
                repository.getAllMeasurements();

        // Should return independent copies
        assertNotSame(list1, list2);
        assertEquals(list1.size(), list2.size());
    }
}