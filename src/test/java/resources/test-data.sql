-- =============================================================================
-- Quantity Measurement App — Test Data Fixtures
-- UC16 | Pre-loaded data for unit and integration tests
-- =============================================================================

-- LENGTH
INSERT INTO quantity_measurement_entity
(this_value, this_unit, this_measurement_type,
 that_value, that_unit, that_measurement_type,
 operation, result_string)
VALUES
(1.0, 'FEET', 'LengthUnit', 12.0, 'INCH', 'LengthUnit', 'COMPARE', 'Equal');

INSERT INTO quantity_measurement_entity
(this_value, this_unit, this_measurement_type,
 operation, result_value, result_unit, result_measurement_type)
VALUES
(1.0, 'FEET', 'LengthUnit', 'CONVERT', 12.0, 'INCH', 'LengthUnit');

INSERT INTO quantity_measurement_entity
(this_value, this_unit, this_measurement_type,
 that_value, that_unit, that_measurement_type,
 operation, result_value, result_unit, result_measurement_type)
VALUES
(1.0, 'FEET', 'LengthUnit', 12.0, 'INCH', 'LengthUnit',
 'ADD', 2.0, 'FEET', 'LengthUnit');

INSERT INTO quantity_measurement_entity
(this_value, this_unit, this_measurement_type,
 that_value, that_unit, that_measurement_type,
 operation, result_value, result_unit, result_measurement_type)
VALUES
(1.0, 'FEET', 'LengthUnit', 12.0, 'INCH', 'LengthUnit',
 'SUBTRACT', 0.0, 'FEET', 'LengthUnit');

INSERT INTO quantity_measurement_entity
(this_value, this_unit, this_measurement_type,
 that_value, that_unit, that_measurement_type,
 operation, result_string)
VALUES
(1.0, 'FEET', 'LengthUnit', 12.0, 'INCH', 'LengthUnit',
 'DIVIDE', '1.0');

-- WEIGHT
INSERT INTO quantity_measurement_entity
(this_value, this_unit, this_measurement_type,
 that_value, that_unit, that_measurement_type,
 operation, result_string)
VALUES
(1.0, 'KILOGRAM', 'WeightUnit', 1000.0, 'GRAM', 'WeightUnit',
 'COMPARE', 'Equal');

INSERT INTO quantity_measurement_entity
(this_value, this_unit, this_measurement_type,
 operation, result_value, result_unit, result_measurement_type)
VALUES
(1.0, 'KILOGRAM', 'WeightUnit', 'CONVERT', 1000.0, 'GRAM', 'WeightUnit');

INSERT INTO quantity_measurement_entity
(this_value, this_unit, this_measurement_type,
 that_value, that_unit, that_measurement_type,
 operation, result_value, result_unit, result_measurement_type)
VALUES
(1.0, 'KILOGRAM', 'WeightUnit', 1000.0, 'GRAM', 'WeightUnit',
 'ADD', 2.0, 'KILOGRAM', 'WeightUnit');

INSERT INTO quantity_measurement_entity
(this_value, this_unit, this_measurement_type,
 that_value, that_unit, that_measurement_type,
 operation, result_value, result_unit, result_measurement_type)
VALUES
(1.0, 'KILOGRAM', 'WeightUnit', 1000.0, 'GRAM', 'WeightUnit',
 'SUBTRACT', 0.0, 'KILOGRAM', 'WeightUnit');

INSERT INTO quantity_measurement_entity
(this_value, this_unit, this_measurement_type,
 that_value, that_unit, that_measurement_type,
 operation, result_string)
VALUES
(1.0, 'KILOGRAM', 'WeightUnit', 1000.0, 'GRAM', 'WeightUnit',
 'DIVIDE', '1.0');

-- VOLUME
INSERT INTO quantity_measurement_entity
(this_value, this_unit, this_measurement_type,
 that_value, that_unit, that_measurement_type,
 operation, result_string)
VALUES
(1.0, 'LITRE', 'VolumeUnit', 1000.0, 'MILLILITRE', 'VolumeUnit',
 'COMPARE', 'Equal');

INSERT INTO quantity_measurement_entity
(this_value, this_unit, this_measurement_type,
 operation, result_value, result_unit, result_measurement_type)
VALUES
(1.0, 'LITRE', 'VolumeUnit', 'CONVERT', 1000.0, 'MILLILITRE', 'VolumeUnit');

INSERT INTO quantity_measurement_entity
(this_value, this_unit, this_measurement_type,
 that_value, that_unit, that_measurement_type,
 operation, result_value, result_unit, result_measurement_type)
VALUES
(1.0, 'LITRE', 'VolumeUnit', 1000.0, 'MILLILITRE', 'VolumeUnit',
 'ADD', 2.0, 'LITRE', 'VolumeUnit');

INSERT INTO quantity_measurement_entity
(this_value, this_unit, this_measurement_type,
 that_value, that_unit, that_measurement_type,
 operation, result_value, result_unit, result_measurement_type)
VALUES
(1.0, 'LITRE', 'VolumeUnit', 1000.0, 'MILLILITRE', 'VolumeUnit',
 'SUBTRACT', 0.0, 'LITRE', 'VolumeUnit');

INSERT INTO quantity_measurement_entity
(this_value, this_unit, this_measurement_type,
 that_value, that_unit, that_measurement_type,
 operation, result_string)
VALUES
(1.0, 'LITRE', 'VolumeUnit', 1000.0, 'MILLILITRE', 'VolumeUnit',
 'DIVIDE', '1.0');

-- TEMPERATURE (COMPARE and CONVERT only)
INSERT INTO quantity_measurement_entity
(this_value, this_unit, this_measurement_type,
 that_value, that_unit, that_measurement_type,
 operation, result_string)
VALUES
(0.0, 'CELSIUS', 'TemperatureUnit', 32.0, 'FAHRENHEIT', 'TemperatureUnit',
 'COMPARE', 'Equal');

INSERT INTO quantity_measurement_entity
(this_value, this_unit, this_measurement_type,
 operation, result_value, result_unit, result_measurement_type)
VALUES
(0.0, 'CELSIUS', 'TemperatureUnit',
 'CONVERT', 32.0, 'FAHRENHEIT', 'TemperatureUnit');