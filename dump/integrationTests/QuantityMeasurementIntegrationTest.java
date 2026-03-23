package com.app.quantitymeasurement.integrationTests;

import com.app.quantitymeasurement.controller.QuantityMeasurementController;
import com.app.quantitymeasurement.model.QuantityDTO;
import com.app.quantitymeasurement.model.QuantityMeasurementEntity;
import com.app.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.app.quantitymeasurement.repository.QuantityMeasurementCacheRepository;
import com.app.quantitymeasurement.repository.QuantityMeasurementDatabaseRepository;
import com.app.quantitymeasurement.service.IQuantityMeasurementService;
import com.app.quantitymeasurement.service.QuantityMeasurementServiceImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Integration tests for end-to-end functionality
 * with both Cache and Database repositories.
 */
public class QuantityMeasurementIntegrationTest {

    private IQuantityMeasurementRepository dbRepository;
    private IQuantityMeasurementRepository cacheRepository;
    private QuantityMeasurementController dbController;
    private QuantityMeasurementController cacheController;

    // ── Setup ─────────────────────────────────────────────────────────────────
    @Before
    public void setUp() {
        // Database repository setup
        dbRepository = QuantityMeasurementDatabaseRepository.getInstance();
        dbRepository.deleteAll();
        IQuantityMeasurementService dbService =
                new QuantityMeasurementServiceImpl(dbRepository);
        dbController = new QuantityMeasurementController(dbService);

        // Cache repository setup
        cacheRepository = QuantityMeasurementCacheRepository.getInstance();
        cacheRepository.deleteAll();
        IQuantityMeasurementService cacheService =
                new QuantityMeasurementServiceImpl(cacheRepository);
        cacheController = new QuantityMeasurementController(cacheService);
    }

    // ── Teardown ──────────────────────────────────────────────────────────────
    @After
    public void tearDown() {
        dbRepository.deleteAll();
        cacheRepository.deleteAll();
    }

    // ── Helper ────────────────────────────────────────────────────────────────
    private QuantityDTO lengthDTO(double value,
                                   QuantityDTO.LengthUnit unit) {
        return new QuantityDTO(
                value,
                unit.getUnitName(),
                unit.getMeasurementType()
        );
    }

    private QuantityDTO weightDTO(double value,
                                   QuantityDTO.WeightUnit unit) {
        return new QuantityDTO(
                value,
                unit.getUnitName(),
                unit.getMeasurementType()
        );
    }

    private QuantityDTO volumeDTO(double value,
                                   QuantityDTO.VolumeUnit unit) {
        return new QuantityDTO(
                value,
                unit.getUnitName(),
                unit.getMeasurementType()
        );
    }

    private QuantityDTO tempDTO(double value,
                                 QuantityDTO.TemperatureUnit unit) {
        return new QuantityDTO(
                value,
                unit.getUnitName(),
                unit.getMeasurementType()
        );
    }

    // ── Database Repository Integration Tests ─────────────────────────────────

    @Test
    public void testServiceWithDatabaseRepository_Compare_Persisted() {
        dbController.performComparison(
                lengthDTO(1.0, QuantityDTO.LengthUnit.FEET),
                lengthDTO(12.0, QuantityDTO.LengthUnit.INCH)
        );

        assertEquals(1, dbRepository.getCount());
        List<QuantityMeasurementEntity> results =
                dbRepository.findByOperation("COMPARE");
        assertEquals(1, results.size());
        assertEquals("Equal", results.get(0).resultString);
    }

    @Test
    public void testServiceWithDatabaseRepository_Convert_Persisted() {
        dbController.performConversion(
                lengthDTO(1.0, QuantityDTO.LengthUnit.FEET),
                "INCH"
        );

        assertEquals(1, dbRepository.getCount());
        List<QuantityMeasurementEntity> results =
                dbRepository.findByOperation("CONVERT");
        assertEquals(1, results.size());
        assertEquals(12.0, results.get(0).resultValue, 0.001);
    }

    @Test
    public void testServiceWithDatabaseRepository_Add_Persisted() {
        dbController.performAddition(
                lengthDTO(1.0, QuantityDTO.LengthUnit.FEET),
                lengthDTO(12.0, QuantityDTO.LengthUnit.INCH)
        );

        assertEquals(1, dbRepository.getCount());
        List<QuantityMeasurementEntity> results =
                dbRepository.findByOperation("ADD");
        assertEquals(1, results.size());
        assertEquals(2.0, results.get(0).resultValue, 0.001);
    }

    @Test
    public void testServiceWithDatabaseRepository_AllOperations_Persisted() {
        // LENGTH
        dbController.performComparison(
                lengthDTO(1.0, QuantityDTO.LengthUnit.FEET),
                lengthDTO(12.0, QuantityDTO.LengthUnit.INCH)
        );
        dbController.performConversion(
                lengthDTO(1.0, QuantityDTO.LengthUnit.FEET), "INCH"
        );
        dbController.performAddition(
                lengthDTO(1.0, QuantityDTO.LengthUnit.FEET),
                lengthDTO(12.0, QuantityDTO.LengthUnit.INCH)
        );
        dbController.performSubtraction(
                lengthDTO(1.0, QuantityDTO.LengthUnit.FEET),
                lengthDTO(12.0, QuantityDTO.LengthUnit.INCH)
        );
        dbController.performDivision(
                lengthDTO(1.0, QuantityDTO.LengthUnit.FEET),
                lengthDTO(12.0, QuantityDTO.LengthUnit.INCH)
        );

        assertEquals(5, dbRepository.getCount());
    }

    // ── Cache Repository Integration Tests ────────────────────────────────────

    @Test
    public void testServiceWithCacheRepository_Compare_InCache() {
        cacheController.performComparison(
                lengthDTO(1.0, QuantityDTO.LengthUnit.FEET),
                lengthDTO(12.0, QuantityDTO.LengthUnit.INCH)
        );

        assertEquals(1, cacheRepository.getCount());
        // Verify NOT in database
        assertEquals(0, dbRepository.getCount());
    }

    @Test
    public void testServiceWithCacheRepository_AllOperations_InCache() {
        cacheController.performComparison(
                weightDTO(1.0, QuantityDTO.WeightUnit.KILOGRAM),
                weightDTO(1000.0, QuantityDTO.WeightUnit.GRAM)
        );
        cacheController.performConversion(
                weightDTO(1.0, QuantityDTO.WeightUnit.KILOGRAM), "GRAM"
        );
        cacheController.performAddition(
                weightDTO(1.0, QuantityDTO.WeightUnit.KILOGRAM),
                weightDTO(1000.0, QuantityDTO.WeightUnit.GRAM)
        );

        assertEquals(3, cacheRepository.getCount());
        assertEquals(0, dbRepository.getCount());
    }

    // ── Filter Tests ──────────────────────────────────────────────────────────

    @Test
    public void testFindByOperation_AllOperationTypes() {
        dbController.performComparison(
                lengthDTO(1.0, QuantityDTO.LengthUnit.FEET),
                lengthDTO(12.0, QuantityDTO.LengthUnit.INCH)
        );
        dbController.performConversion(
                lengthDTO(1.0, QuantityDTO.LengthUnit.FEET), "INCH"
        );
        dbController.performAddition(
                lengthDTO(1.0, QuantityDTO.LengthUnit.FEET),
                lengthDTO(12.0, QuantityDTO.LengthUnit.INCH)
        );
        dbController.performSubtraction(
                lengthDTO(1.0, QuantityDTO.LengthUnit.FEET),
                lengthDTO(12.0, QuantityDTO.LengthUnit.INCH)
        );
        dbController.performDivision(
                lengthDTO(1.0, QuantityDTO.LengthUnit.FEET),
                lengthDTO(12.0, QuantityDTO.LengthUnit.INCH)
        );

        assertEquals(1, dbRepository
                .findByOperation("COMPARE").size());
        assertEquals(1, dbRepository
                .findByOperation("CONVERT").size());
        assertEquals(1, dbRepository
                .findByOperation("ADD").size());
        assertEquals(1, dbRepository
                .findByOperation("SUBTRACT").size());
        assertEquals(1, dbRepository
                .findByOperation("DIVIDE").size());
    }

    @Test
    public void testFindByMeasurementType_AllTypes() {
        // Length
        dbController.performComparison(
                lengthDTO(1.0, QuantityDTO.LengthUnit.FEET),
                lengthDTO(12.0, QuantityDTO.LengthUnit.INCH)
        );
        // Weight
        dbController.performComparison(
                weightDTO(1.0, QuantityDTO.WeightUnit.KILOGRAM),
                weightDTO(1000.0, QuantityDTO.WeightUnit.GRAM)
        );
        // Volume
        dbController.performComparison(
                volumeDTO(1.0, QuantityDTO.VolumeUnit.LITRE),
                volumeDTO(1000.0, QuantityDTO.VolumeUnit.MILLILITRE)
        );
        // Temperature
        dbController.performComparison(
                tempDTO(0.0, QuantityDTO.TemperatureUnit.CELSIUS),
                tempDTO(32.0, QuantityDTO.TemperatureUnit.FAHRENHEIT)
        );

        assertEquals(1, dbRepository
                .findByMeasurementType("LengthUnit").size());
        assertEquals(1, dbRepository
                .findByMeasurementType("WeightUnit").size());
        assertEquals(1, dbRepository
                .findByMeasurementType("VolumeUnit").size());
        assertEquals(1, dbRepository
                .findByMeasurementType("TemperatureUnit").size());
    }

    // ── Data Isolation Tests ──────────────────────────────────────────────────

    @Test
    public void testH2Database_IsolationBetweenTests() {
        // This test verifies setUp() cleans DB before each test
        assertEquals(0, dbRepository.getCount());
        assertEquals(0, cacheRepository.getCount());
    }

    // ── Pool Statistics Tests ─────────────────────────────────────────────────

    @Test
    public void testDatabaseRepositoryPoolStatistics() {
        String stats = dbRepository.getPoolStatistics();
        assertNotNull(stats);
        assertFalse(stats.isEmpty());
    }
}