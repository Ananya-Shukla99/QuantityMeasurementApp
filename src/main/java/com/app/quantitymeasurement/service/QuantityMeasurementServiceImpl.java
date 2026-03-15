package com.app.quantitymeasurement.service;

import com.app.quantitymeasurement.entity.QuantityDTO;
import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.entity.QuantityModel;
import com.app.quantitymeasurement.exception.QuantityMeasurementException;
import com.app.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.app.quantitymeasurement.unit.IMeasurable;
import com.app.quantitymeasurement.unit.LengthUnit;
import com.app.quantitymeasurement.unit.WeightUnit;
import com.app.quantitymeasurement.unit.VolumeUnit;
import com.app.quantitymeasurement.unit.TemperatureUnit;
import com.app.quantitymeasurement.quantity.Quantity;

import java.util.logging.Logger;

/**
 * QuantityMeasurementServiceImpl implements IQuantityMeasurementService.
 * Contains all business logic for quantity measurement operations.
 * Delegates persistence to the injected repository.
 */
public class QuantityMeasurementServiceImpl
        implements IQuantityMeasurementService {

    private static final Logger logger = Logger.getLogger(
            QuantityMeasurementServiceImpl.class.getName()
    );

    // ── Repository Dependency (injected via constructor) ──────────────────────
    private final IQuantityMeasurementRepository repository;

    // ── Constructor ───────────────────────────────────────────────────────────
    public QuantityMeasurementServiceImpl(
            IQuantityMeasurementRepository repository) {
        this.repository = repository;
        logger.info("QuantityMeasurementServiceImpl initialized.");
    }

    // ── CONVERT ───────────────────────────────────────────────────────────────
    @Override
    public QuantityDTO convert(QuantityDTO quantityDTO, String targetUnit) {
        try {
            QuantityModel<IMeasurable> model = toQuantityModel(quantityDTO);
            IMeasurable targetUnitInstance   = model.unit
                    .getUnitInstance(targetUnit);

            validateSameType(model.unit, targetUnitInstance, "CONVERT");

            Quantity<IMeasurable> quantity = new Quantity<>(
                    model.value, model.unit
            );
            Quantity<IMeasurable> result   = quantity
                    .convertTo(targetUnitInstance);

            QuantityModel<IMeasurable> resultModel = new QuantityModel<>(
                    result.getValue(), result.getUnit()
            );

            QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                    model, null, "CONVERT", resultModel
            );
            repository.save(entity);

            logger.info("CONVERT: " + model + " → " + resultModel);
            return toQuantityDTO(resultModel);

        } catch (QuantityMeasurementException e) {
            throw e;
        } catch (Exception e) {
            throw new QuantityMeasurementException(
                    "Conversion failed: " + e.getMessage(), e
            );
        }
    }

    // ── COMPARE ───────────────────────────────────────────────────────────────
    @Override
    public boolean compare(QuantityDTO quantityDTO1, QuantityDTO quantityDTO2) {
        try {
            QuantityModel<IMeasurable> model1 = toQuantityModel(quantityDTO1);
            QuantityModel<IMeasurable> model2 = toQuantityModel(quantityDTO2);

            validateSameType(model1.unit, model2.unit, "COMPARE");

            Quantity<IMeasurable> q1 = new Quantity<>(
                    model1.value, model1.unit
            );
            Quantity<IMeasurable> q2 = new Quantity<>(
                    model2.value, model2.unit
            );
            boolean result = q1.equals(q2);

            QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                    model1, model2, "COMPARE",
                    result ? "Equal" : "Not Equal"
            );
            repository.save(entity);

            logger.info("COMPARE: " + model1 + " vs " + model2
                    + " → " + result);
            return result;

        } catch (QuantityMeasurementException e) {
            throw e;
        } catch (Exception e) {
            throw new QuantityMeasurementException(
                    "Comparison failed: " + e.getMessage(), e
            );
        }
    }

    // ── ADD ───────────────────────────────────────────────────────────────────
    @Override
    public QuantityDTO add(QuantityDTO quantityDTO1, QuantityDTO quantityDTO2) {
        try {
            QuantityModel<IMeasurable> model1 = toQuantityModel(quantityDTO1);
            QuantityModel<IMeasurable> model2 = toQuantityModel(quantityDTO2);

            validateSameType(model1.unit, model2.unit, "ADD");

            Quantity<IMeasurable> q1 = new Quantity<>(
                    model1.value, model1.unit
            );
            Quantity<IMeasurable> q2 = new Quantity<>(
                    model2.value, model2.unit
            );
            Quantity<IMeasurable> result = q1.add(q2);

            QuantityModel<IMeasurable> resultModel = new QuantityModel<>(
                    result.getValue(), result.getUnit()
            );

            QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                    model1, model2, "ADD", resultModel
            );
            repository.save(entity);

            logger.info("ADD: " + model1 + " + " + model2
                    + " → " + resultModel);
            return toQuantityDTO(resultModel);

        } catch (QuantityMeasurementException e) {
            throw e;
        } catch (Exception e) {
            throw new QuantityMeasurementException(
                    "Addition failed: " + e.getMessage(), e
            );
        }
    }

    @Override
    public QuantityDTO add(QuantityDTO quantityDTO1, QuantityDTO quantityDTO2,
                           String targetUnit) {
        try {
            QuantityModel<IMeasurable> model1 = toQuantityModel(quantityDTO1);
            QuantityModel<IMeasurable> model2 = toQuantityModel(quantityDTO2);

            validateSameType(model1.unit, model2.unit, "ADD");

            IMeasurable targetUnitInstance = model1.unit
                    .getUnitInstance(targetUnit);

            Quantity<IMeasurable> q1 = new Quantity<>(
                    model1.value, model1.unit
            );
            Quantity<IMeasurable> q2 = new Quantity<>(
                    model2.value, model2.unit
            );
            Quantity<IMeasurable> result = q1.add(q2, targetUnitInstance);

            QuantityModel<IMeasurable> resultModel = new QuantityModel<>(
                    result.getValue(), result.getUnit()
            );

            QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                    model1, model2, "ADD", resultModel
            );
            repository.save(entity);

            logger.info("ADD (target: " + targetUnit + "): "
                    + model1 + " + " + model2 + " → " + resultModel);
            return toQuantityDTO(resultModel);

        } catch (QuantityMeasurementException e) {
            throw e;
        } catch (Exception e) {
            throw new QuantityMeasurementException(
                    "Addition failed: " + e.getMessage(), e
            );
        }
    }

    // ── SUBTRACT ──────────────────────────────────────────────────────────────
    @Override
    public QuantityDTO subtract(QuantityDTO quantityDTO1,
                                QuantityDTO quantityDTO2) {
        try {
            QuantityModel<IMeasurable> model1 = toQuantityModel(quantityDTO1);
            QuantityModel<IMeasurable> model2 = toQuantityModel(quantityDTO2);

            validateSameType(model1.unit, model2.unit, "SUBTRACT");

            Quantity<IMeasurable> q1 = new Quantity<>(
                    model1.value, model1.unit
            );
            Quantity<IMeasurable> q2 = new Quantity<>(
                    model2.value, model2.unit
            );
            Quantity<IMeasurable> result = q1.subtract(q2);

            QuantityModel<IMeasurable> resultModel = new QuantityModel<>(
                    result.getValue(), result.getUnit()
            );

            QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                    model1, model2, "SUBTRACT", resultModel
            );
            repository.save(entity);

            logger.info("SUBTRACT: " + model1 + " - " + model2
                    + " → " + resultModel);
            return toQuantityDTO(resultModel);

        } catch (QuantityMeasurementException e) {
            throw e;
        } catch (Exception e) {
            throw new QuantityMeasurementException(
                    "Subtraction failed: " + e.getMessage(), e
            );
        }
    }

    // ── DIVIDE ────────────────────────────────────────────────────────────────
    @Override
    public double divide(QuantityDTO quantityDTO1, QuantityDTO quantityDTO2) {
        try {
            QuantityModel<IMeasurable> model1 = toQuantityModel(quantityDTO1);
            QuantityModel<IMeasurable> model2 = toQuantityModel(quantityDTO2);

            validateSameType(model1.unit, model2.unit, "DIVIDE");

            Quantity<IMeasurable> q1 = new Quantity<>(
                    model1.value, model1.unit
            );
            Quantity<IMeasurable> q2 = new Quantity<>(
                    model2.value, model2.unit
            );
            double result = q1.divide(q2);

            QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                    model1, model2, "DIVIDE",
                    String.valueOf(result)
            );
            repository.save(entity);

            logger.info("DIVIDE: " + model1 + " / " + model2
                    + " → " + result);
            return result;

        } catch (QuantityMeasurementException e) {
            throw e;
        } catch (Exception e) {
            throw new QuantityMeasurementException(
                    "Division failed: " + e.getMessage(), e
            );
        }
    }

    // ── PRIVATE HELPERS ───────────────────────────────────────────────────────

    /**
     * Converts QuantityDTO to internal QuantityModel.
     */
    private QuantityModel<IMeasurable> toQuantityModel(QuantityDTO dto) {
        if (dto == null)
            throw new QuantityMeasurementException(
                    "QuantityDTO cannot be null"
            );

        IMeasurable resolvedUnit = resolveUnit(
                dto.measurementType, dto.unitName
        );
        return new QuantityModel<>(dto.value, resolvedUnit);
    }

    /**
     * Resolves unit name to its IMeasurable enum instance.
     */
    private IMeasurable resolveUnit(String measurementType, String unitName) {
        switch (measurementType) {
            case "LengthUnit":
                return LengthUnit.INCH.getUnitInstance(unitName);
            case "WeightUnit":
                return WeightUnit.GRAM.getUnitInstance(unitName);
            case "VolumeUnit":
                return VolumeUnit.LITRE.getUnitInstance(unitName);
            case "TemperatureUnit":
                return TemperatureUnit.CELSIUS.getUnitInstance(unitName);
            default:
                throw new QuantityMeasurementException(
                        "Unknown measurement type: " + measurementType
                );
        }
    }

    /**
     * Validates that two units belong to the same measurement type.
     */
    private void validateSameType(IMeasurable unit1, IMeasurable unit2,
                                  String operation) {
        if (!unit1.getMeasurementType().equals(unit2.getMeasurementType())) {
            throw new QuantityMeasurementException(
                    "Cannot perform " + operation
                    + " on incompatible types: "
                    + unit1.getMeasurementType()
                    + " and " + unit2.getMeasurementType()
            );
        }
    }

    /**
     * Converts internal QuantityModel back to QuantityDTO.
     */
    private QuantityDTO toQuantityDTO(QuantityModel<IMeasurable> model) {
        return new QuantityDTO(
                model.value,
                model.unit.getUnitName(),
                model.unit.getMeasurementType()
        );
    }
}