package com.app.quantitymeasurement.entity;

import org.junit.Test;

import com.app.quantitymeasurement.model.QuantityDTO;
import com.app.quantitymeasurement.model.QuantityMeasurementEntity;

import static org.junit.Assert.*;

/**
 * Unit tests for QuantityMeasurementEntity.
 * Tests constructors, fields and toString.
 */
public class QuantityMeasurementEntityTest {

    // ── Tests ─────────────────────────────────────────────────────────────────

    @Test
    public void testEntity_DefaultConstructor() {
        QuantityMeasurementEntity entity =
                new QuantityMeasurementEntity();

        assertNotNull(entity);
        assertFalse(entity.isError);
        assertNull(entity.operation);
        assertNull(entity.thisUnit);
    }

    @Test
    public void testEntity_SetFields() {
        QuantityMeasurementEntity entity =
                new QuantityMeasurementEntity();

        entity.operation           = "COMPARE";
        entity.thisValue           = 1.0;
        entity.thisUnit            = "FEET";
        entity.thisMeasurementType = "LengthUnit";
        entity.thatValue           = 12.0;
        entity.thatUnit            = "INCH";
        entity.thatMeasurementType = "LengthUnit";
        entity.resultString        = "Equal";

        assertEquals("COMPARE",   entity.operation);
        assertEquals(1.0,          entity.thisValue,  0.001);
        assertEquals("FEET",       entity.thisUnit);
        assertEquals("LengthUnit", entity.thisMeasurementType);
        assertEquals(12.0,         entity.thatValue,  0.001);
        assertEquals("INCH",       entity.thatUnit);
        assertEquals("Equal",      entity.resultString);
    }

    @Test
    public void testEntity_ErrorFields() {
        QuantityMeasurementEntity entity =
                new QuantityMeasurementEntity();

        entity.isError      = true;
        entity.errorMessage = "Test error";
        entity.operation    = "ADD";

        assertTrue(entity.isError);
        assertEquals("Test error", entity.errorMessage);
    }

    @Test
    public void testEntity_NumericResultFields() {
        QuantityMeasurementEntity entity =
                new QuantityMeasurementEntity();

        entity.operation              = "CONVERT";
        entity.thisValue              = 1.0;
        entity.thisUnit               = "FEET";
        entity.thisMeasurementType    = "LengthUnit";
        entity.resultValue            = 12.0;
        entity.resultUnit             = "INCH";
        entity.resultMeasurementType  = "LengthUnit";

        assertEquals("CONVERT",   entity.operation);
        assertEquals(12.0,         entity.resultValue, 0.001);
        assertEquals("INCH",       entity.resultUnit);
        assertEquals("LengthUnit", entity.resultMeasurementType);
    }

    @Test
    public void testEntity_ToString_CompareOperation() {
        QuantityMeasurementEntity entity =
                new QuantityMeasurementEntity();

        entity.operation    = "COMPARE";
        entity.thisValue    = 1.0;
        entity.thisUnit     = "FEET";
        entity.thatValue    = 12.0;
        entity.thatUnit     = "INCH";
        entity.resultString = "Equal";

        String str = entity.toString();

        assertNotNull(str);
        assertTrue(str.contains("COMPARE"));
        assertTrue(str.contains("Equal"));
    }

    @Test
    public void testEntity_ToString_ErrorOperation() {
        QuantityMeasurementEntity entity =
                new QuantityMeasurementEntity();

        entity.operation    = "ADD";
        entity.isError      = true;
        entity.errorMessage = "Temperature not supported";

        String str = entity.toString();

        assertNotNull(str);
        assertTrue(str.contains("ERROR"));
        assertTrue(str.contains("Temperature not supported"));
    }

    @Test
    public void testEntity_ToString_NumericOperation() {
        QuantityMeasurementEntity entity =
                new QuantityMeasurementEntity();

        entity.operation    = "CONVERT";
        entity.thisValue    = 1.0;
        entity.thisUnit     = "FEET";
        entity.resultValue  = 12.0;
        entity.resultUnit   = "INCH";

        String str = entity.toString();

        assertNotNull(str);
        assertTrue(str.contains("CONVERT"));
    }

    @Test
    public void testQuantityDTO_Constructor() {
        QuantityDTO dto = new QuantityDTO(
                1.0,
                QuantityDTO.LengthUnit.FEET.getUnitName(),
                QuantityDTO.LengthUnit.FEET.getMeasurementType()
        );

        assertEquals(1.0,          dto.value,           0.001);
        assertEquals("FEET",       dto.unitName);
        assertEquals("LengthUnit", dto.measurementType);
    }

    @Test
    public void testQuantityDTO_AllUnitEnums() {
        // Length
        assertEquals("FEET",
                QuantityDTO.LengthUnit.FEET.getUnitName());
        assertEquals("LengthUnit",
                QuantityDTO.LengthUnit.FEET.getMeasurementType());

        // Weight
        assertEquals("KILOGRAM",
                QuantityDTO.WeightUnit.KILOGRAM.getUnitName());
        assertEquals("WeightUnit",
                QuantityDTO.WeightUnit.KILOGRAM.getMeasurementType());

        // Volume
        assertEquals("LITRE",
                QuantityDTO.VolumeUnit.LITRE.getUnitName());
        assertEquals("VolumeUnit",
                QuantityDTO.VolumeUnit.LITRE.getMeasurementType());

        // Temperature
        assertEquals("CELSIUS",
                QuantityDTO.TemperatureUnit.CELSIUS.getUnitName());
        assertEquals("TemperatureUnit",
                QuantityDTO.TemperatureUnit.CELSIUS.getMeasurementType());
    }

    @Test
    public void testQuantityModel_Constructor() {
        // We can't test directly without IMeasurable
        // but we verify QuantityDTO works correctly
        QuantityDTO dto = new QuantityDTO(
                100.0,
                QuantityDTO.WeightUnit.GRAM.getUnitName(),
                QuantityDTO.WeightUnit.GRAM.getMeasurementType()
        );

        assertEquals(100.0, dto.value, 0.001);
        assertEquals("GRAM", dto.unitName);
    }
}