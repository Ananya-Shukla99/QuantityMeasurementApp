package com.app.quantitymeasurement.service;

import com.app.quantitymeasurement.entity.QuantityDTO;
import com.app.quantitymeasurement.exception.QuantityMeasurementException;
import com.app.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.app.quantitymeasurement.repository.QuantityMeasurementCacheRepository;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for QuantityMeasurementServiceImpl.
 * Uses in-memory CacheRepository for fast isolated testing.
 */
public class QuantityMeasurementServiceImplTest {

    private IQuantityMeasurementService service;
    private IQuantityMeasurementRepository repository;

    // ── Setup ─────────────────────────────────────────────────────────────────
    @Before
    public void setUp() {
        repository = QuantityMeasurementCacheRepository.getInstance();
        repository.deleteAll();
        service = new QuantityMeasurementServiceImpl(repository);
    }

    // ── Helper ────────────────────────────────────────────────────────────────
    private QuantityDTO lengthDTO(double value, QuantityDTO.LengthUnit unit) {
        return new QuantityDTO(
                value,
                unit.getUnitName(),
                unit.getMeasurementType()
        );
    }

    private QuantityDTO weightDTO(double value, QuantityDTO.WeightUnit unit) {
        return new QuantityDTO(
                value,
                unit.getUnitName(),
                unit.getMeasurementType()
        );
    }

    private QuantityDTO volumeDTO(double value, QuantityDTO.VolumeUnit unit) {
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

    // ── COMPARE Tests ─────────────────────────────────────────────────────────

    @Test
    public void testCompare_FeetAndInch_Equal() {
        assertTrue(service.compare(
                lengthDTO(1.0, QuantityDTO.LengthUnit.FEET),
                lengthDTO(12.0, QuantityDTO.LengthUnit.INCH)
        ));
    }

    @Test
    public void testCompare_KilogramAndGram_Equal() {
        assertTrue(service.compare(
                weightDTO(1.0, QuantityDTO.WeightUnit.KILOGRAM),
                weightDTO(1000.0, QuantityDTO.WeightUnit.GRAM)
        ));
    }

    @Test
    public void testCompare_LitreAndMillilitre_Equal() {
        assertTrue(service.compare(
                volumeDTO(1.0, QuantityDTO.VolumeUnit.LITRE),
                volumeDTO(1000.0, QuantityDTO.VolumeUnit.MILLILITRE)
        ));
    }

    @Test
    public void testCompare_CelsiusAndFahrenheit_Equal() {
        assertTrue(service.compare(
                tempDTO(0.0, QuantityDTO.TemperatureUnit.CELSIUS),
                tempDTO(32.0, QuantityDTO.TemperatureUnit.FAHRENHEIT)
        ));
    }

    @Test
    public void testCompare_DifferentValues_NotEqual() {
        assertFalse(service.compare(
                lengthDTO(1.0, QuantityDTO.LengthUnit.FEET),
                lengthDTO(1.0, QuantityDTO.LengthUnit.INCH)
        ));
    }

    // ── CONVERT Tests ─────────────────────────────────────────────────────────

    @Test
    public void testConvert_FeetToInch() {
        QuantityDTO result = service.convert(
                lengthDTO(1.0, QuantityDTO.LengthUnit.FEET),
                "INCH"
        );
        assertEquals(12.0, result.value, 0.001);
        assertEquals("INCH", result.unitName);
    }

    @Test
    public void testConvert_KilogramToGram() {
        QuantityDTO result = service.convert(
                weightDTO(1.0, QuantityDTO.WeightUnit.KILOGRAM),
                "GRAM"
        );
        assertEquals(1000.0, result.value, 0.001);
        assertEquals("GRAM", result.unitName);
    }

    @Test
    public void testConvert_LitreToMillilitre() {
        QuantityDTO result = service.convert(
                volumeDTO(1.0, QuantityDTO.VolumeUnit.LITRE),
                "MILLILITRE"
        );
        assertEquals(1000.0, result.value, 0.001);
        assertEquals("MILLILITRE", result.unitName);
    }

    @Test
    public void testConvert_CelsiusToFahrenheit() {
        QuantityDTO result = service.convert(
                tempDTO(0.0, QuantityDTO.TemperatureUnit.CELSIUS),
                "FAHRENHEIT"
        );
        assertEquals(32.0, result.value, 0.001);
        assertEquals("FAHRENHEIT", result.unitName);
    }

    // ── ADD Tests ─────────────────────────────────────────────────────────────

    @Test
    public void testAdd_FeetAndInch() {
        QuantityDTO result = service.add(
                lengthDTO(1.0, QuantityDTO.LengthUnit.FEET),
                lengthDTO(12.0, QuantityDTO.LengthUnit.INCH)
        );
        assertEquals(2.0, result.value, 0.001);
        assertEquals("FEET", result.unitName);
    }

    @Test
    public void testAdd_KilogramAndGram() {
        QuantityDTO result = service.add(
                weightDTO(1.0, QuantityDTO.WeightUnit.KILOGRAM),
                weightDTO(1000.0, QuantityDTO.WeightUnit.GRAM)
        );
        assertEquals(2.0, result.value, 0.001);
    }

    @Test
    public void testAdd_WithTargetUnit() {
        QuantityDTO result = service.add(
                lengthDTO(1.0, QuantityDTO.LengthUnit.FEET),
                lengthDTO(12.0, QuantityDTO.LengthUnit.INCH),
                "FEET"
        );
        assertEquals(2.0, result.value, 0.001);
        assertEquals("FEET", result.unitName);
    }

    // ── SUBTRACT Tests ────────────────────────────────────────────────────────

    @Test
    public void testSubtract_FeetAndInch() {
        QuantityDTO result = service.subtract(
                lengthDTO(1.0, QuantityDTO.LengthUnit.FEET),
                lengthDTO(12.0, QuantityDTO.LengthUnit.INCH)
        );
        assertEquals(0.0, result.value, 0.001);
    }

    @Test
    public void testSubtract_KilogramAndGram() {
        QuantityDTO result = service.subtract(
                weightDTO(1.0, QuantityDTO.WeightUnit.KILOGRAM),
                weightDTO(1000.0, QuantityDTO.WeightUnit.GRAM)
        );
        assertEquals(0.0, result.value, 0.001);
    }

    // ── DIVIDE Tests ──────────────────────────────────────────────────────────

    @Test
    public void testDivide_FeetAndInch() {
        double result = service.divide(
                lengthDTO(1.0, QuantityDTO.LengthUnit.FEET),
                lengthDTO(12.0, QuantityDTO.LengthUnit.INCH)
        );
        assertEquals(1.0, result, 0.001);
    }

    @Test
    public void testDivide_KilogramAndGram() {
        double result = service.divide(
                weightDTO(1.0, QuantityDTO.WeightUnit.KILOGRAM),
                weightDTO(1000.0, QuantityDTO.WeightUnit.GRAM)
        );
        assertEquals(1.0, result, 0.001);
    }

    // ── Exception Tests ───────────────────────────────────────────────────────

    @Test(expected = QuantityMeasurementException.class)
    public void testCompare_IncompatibleTypes_ThrowsException() {
        service.compare(
                lengthDTO(1.0, QuantityDTO.LengthUnit.FEET),
                weightDTO(1.0, QuantityDTO.WeightUnit.KILOGRAM)
        );
    }

    @Test(expected = QuantityMeasurementException.class)
    public void testAdd_IncompatibleTypes_ThrowsException() {
        service.add(
                lengthDTO(1.0, QuantityDTO.LengthUnit.FEET),
                weightDTO(1.0, QuantityDTO.WeightUnit.KILOGRAM)
        );
    }

    @Test(expected = QuantityMeasurementException.class)
    public void testAdd_Temperature_ThrowsException() {
        service.add(
                tempDTO(0.0, QuantityDTO.TemperatureUnit.CELSIUS),
                tempDTO(32.0, QuantityDTO.TemperatureUnit.FAHRENHEIT)
        );
    }

    // ── Repository Persistence Tests ──────────────────────────────────────────

    @Test
    public void testService_SavesToRepository_AfterOperation() {
        service.compare(
                lengthDTO(1.0, QuantityDTO.LengthUnit.FEET),
                lengthDTO(12.0, QuantityDTO.LengthUnit.INCH)
        );
        assertEquals(1, repository.getCount());
    }

    @Test
    public void testService_MultipleOperations_AllSaved() {
        service.compare(
                lengthDTO(1.0, QuantityDTO.LengthUnit.FEET),
                lengthDTO(12.0, QuantityDTO.LengthUnit.INCH)
        );
        service.convert(
                lengthDTO(1.0, QuantityDTO.LengthUnit.FEET),
                "INCH"
        );
        service.add(
                lengthDTO(1.0, QuantityDTO.LengthUnit.FEET),
                lengthDTO(12.0, QuantityDTO.LengthUnit.INCH)
        );
        assertEquals(3, repository.getCount());
    }
}