package com.app.quantitymeasurement.controller;

import com.app.quantitymeasurement.entity.QuantityDTO;
import com.app.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.app.quantitymeasurement.repository.QuantityMeasurementCacheRepository;
import com.app.quantitymeasurement.service.IQuantityMeasurementService;
import com.app.quantitymeasurement.service.QuantityMeasurementServiceImpl;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for QuantityMeasurementController.
 * Uses CacheRepository for fast isolated testing.
 */
public class QuantityMeasurementControllerTest {

    private QuantityMeasurementController controller;
    private IQuantityMeasurementRepository repository;

    // ── Setup ─────────────────────────────────────────────────────────────────
    @Before
    public void setUp() {
        repository = QuantityMeasurementCacheRepository.getInstance();
        repository.deleteAll();
        IQuantityMeasurementService service =
                new QuantityMeasurementServiceImpl(repository);
        controller = new QuantityMeasurementController(service);
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
    public void testPerformComparison_FeetAndInch_ReturnsTrue() {
        boolean result = controller.performComparison(
                lengthDTO(1.0, QuantityDTO.LengthUnit.FEET),
                lengthDTO(12.0, QuantityDTO.LengthUnit.INCH)
        );
        assertTrue(result);
    }

    @Test
    public void testPerformComparison_DifferentValues_ReturnsFalse() {
        boolean result = controller.performComparison(
                lengthDTO(1.0, QuantityDTO.LengthUnit.FEET),
                lengthDTO(1.0, QuantityDTO.LengthUnit.INCH)
        );
        assertFalse(result);
    }

    @Test
    public void testPerformComparison_IncompatibleTypes_ReturnsFalse() {
        // Controller handles exception gracefully — returns false
        boolean result = controller.performComparison(
                lengthDTO(1.0, QuantityDTO.LengthUnit.FEET),
                weightDTO(1.0, QuantityDTO.WeightUnit.KILOGRAM)
        );
        assertFalse(result);
    }

    // ── CONVERT Tests ─────────────────────────────────────────────────────────

    @Test
    public void testPerformConversion_FeetToInch() {
        QuantityDTO result = controller.performConversion(
                lengthDTO(1.0, QuantityDTO.LengthUnit.FEET),
                "INCH"
        );
        assertNotNull(result);
        assertEquals(12.0, result.value, 0.001);
        assertEquals("INCH", result.unitName);
    }

    @Test
    public void testPerformConversion_InvalidUnit_ReturnsNull() {
        QuantityDTO result = controller.performConversion(
                lengthDTO(1.0, QuantityDTO.LengthUnit.FEET),
                "INVALID_UNIT"
        );
        assertNull(result);
    }

    // ── ADD Tests ─────────────────────────────────────────────────────────────

    @Test
    public void testPerformAddition_FeetAndInch() {
        QuantityDTO result = controller.performAddition(
                lengthDTO(1.0, QuantityDTO.LengthUnit.FEET),
                lengthDTO(12.0, QuantityDTO.LengthUnit.INCH)
        );
        assertNotNull(result);
        assertEquals(2.0, result.value, 0.001);
    }

    @Test
    public void testPerformAddition_WithTargetUnit() {
        QuantityDTO result = controller.performAddition(
                lengthDTO(1.0, QuantityDTO.LengthUnit.FEET),
                lengthDTO(12.0, QuantityDTO.LengthUnit.INCH),
                "FEET"
        );
        assertNotNull(result);
        assertEquals(2.0, result.value, 0.001);
        assertEquals("FEET", result.unitName);
    }

    @Test
    public void testPerformAddition_Temperature_ReturnsNull() {
        // Temperature does not support arithmetic
        QuantityDTO result = controller.performAddition(
                tempDTO(0.0, QuantityDTO.TemperatureUnit.CELSIUS),
                tempDTO(32.0, QuantityDTO.TemperatureUnit.FAHRENHEIT)
        );
        assertNull(result);
    }

    // ── SUBTRACT Tests ────────────────────────────────────────────────────────

    @Test
    public void testPerformSubtraction_FeetAndInch() {
        QuantityDTO result = controller.performSubtraction(
                lengthDTO(1.0, QuantityDTO.LengthUnit.FEET),
                lengthDTO(12.0, QuantityDTO.LengthUnit.INCH)
        );
        assertNotNull(result);
        assertEquals(0.0, result.value, 0.001);
    }

    // ── DIVIDE Tests ──────────────────────────────────────────────────────────

    @Test
    public void testPerformDivision_FeetAndInch() {
        double result = controller.performDivision(
                lengthDTO(1.0, QuantityDTO.LengthUnit.FEET),
                lengthDTO(12.0, QuantityDTO.LengthUnit.INCH)
        );
        assertEquals(1.0, result, 0.001);
    }

    @Test
    public void testPerformDivision_Temperature_ReturnsZero() {
        // Temperature does not support arithmetic
        double result = controller.performDivision(
                tempDTO(0.0, QuantityDTO.TemperatureUnit.CELSIUS),
                tempDTO(32.0, QuantityDTO.TemperatureUnit.FAHRENHEIT)
        );
        assertEquals(0.0, result, 0.001);
    }
}