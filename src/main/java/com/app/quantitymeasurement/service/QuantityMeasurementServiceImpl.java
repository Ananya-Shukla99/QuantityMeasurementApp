package com.app.quantitymeasurement.service;

import com.app.quantitymeasurement.DTO.Quantity;
import com.app.quantitymeasurement.DTO.QuantityDTO;
import com.app.quantitymeasurement.DTO.QuantityMeasurementDTO;
import com.app.quantitymeasurement.model.*;
import com.app.quantitymeasurement.repository.QuantityMeasurementRepository;
import com.app.quantitymeasurement.unit.*;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.logging.Logger;

@Service
@Transactional
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    private static final Logger logger = Logger.getLogger(
        QuantityMeasurementServiceImpl.class.getName()
    );

    // ── Repository injected by Spring ─────────────────────────────────────────
    @Autowired
    private QuantityMeasurementRepository repository;

    // ── Compare ───────────────────────────────────────────────────────────────
    @Override
    public QuantityMeasurementDTO compare(QuantityDTO dto1, QuantityDTO dto2) {
        QuantityMeasurementEntity entity;
        try {
            QuantityModel<IMeasurable> model1 = convertDtoToModel(dto1);
            QuantityModel<IMeasurable> model2 = convertDtoToModel(dto2);
            boolean result = new Quantity<>(model1.value, model1.unit)
                    .equals(new Quantity<>(model2.value, model2.unit));
            String resultString = result ? "Equal" : "Not Equal";
            entity = new QuantityMeasurementEntity();
            entity.setThisValue(model1.value);
            entity.setThisUnit(model1.unit.getUnitName());
            entity.setThisMeasurementType(model1.unit.getMeasurementType());
            entity.setThatValue(model2.value);
            entity.setThatUnit(model2.unit.getUnitName());
            entity.setThatMeasurementType(model2.unit.getMeasurementType());
            entity.setOperation("COMPARE");
            entity.setResultString(resultString);
            entity.setError(false);
            logger.info("COMPARE: " + dto1 + " vs " + dto2 + " → " + resultString);
        } catch (Exception e) {
            entity = buildErrorEntity(dto1, dto2, "COMPARE", e.getMessage());
        }
      
        repository.save(entity);
        logger.info("Saved entity to DB with ID: " + entity.getId());
        return QuantityMeasurementDTO.fromEntity(entity);
    }

    // ── Convert ───────────────────────────────────────────────────────────────
    @Override
    public QuantityMeasurementDTO convert(QuantityDTO dto, String targetUnit) {
        QuantityMeasurementEntity entity;
        try {
            QuantityModel<IMeasurable> model = convertDtoToModel(dto);
            IMeasurable target = model.unit.getUnitInstance(targetUnit);

            Quantity<IMeasurable> result = new Quantity<>(model.value, model.unit)
                    .convertTo(target);

            entity = new QuantityMeasurementEntity();
            entity.setThisValue(model.value);
            entity.setThisUnit(model.unit.getUnitName());
            entity.setThisMeasurementType(model.unit.getMeasurementType());
            entity.setOperation("CONVERT");
            entity.setResultValue(result.getValue());
            entity.setResultUnit(result.getUnit().getUnitName());
            entity.setResultMeasurementType(result.getUnit().getMeasurementType());
            entity.setError(false);

            logger.info("CONVERT: " + dto + " → " + result);

        } catch (Exception e) {
            entity = buildErrorEntity(dto, null, "CONVERT", e.getMessage());
        }
        repository.save(entity);
        logger.info("Saved entity to DB with ID: " + entity.getId());
        return QuantityMeasurementDTO.fromEntity(entity);
    }


    // ── Add ───────────────────────────────────────────────────────────────────
    @Override
    public QuantityMeasurementDTO add(QuantityDTO dto1, QuantityDTO dto2) {
        QuantityMeasurementEntity entity;
        try {
            QuantityModel<IMeasurable> model1 = convertDtoToModel(dto1);
            QuantityModel<IMeasurable> model2 = convertDtoToModel(dto2);
            Quantity<IMeasurable> result = new Quantity<>(model1.value, model1.unit)
                    .add(new Quantity<>(model2.value, model2.unit));
            entity = buildResultEntity(model1, model2, "ADD", result);
            logger.info("ADD: " + dto1 + " + " + dto2 + " → " + result);
        } catch (Exception e) {
            entity = buildErrorEntity(dto1, dto2, "ADD", e.getMessage());
        }
        repository.save(entity);
        logger.info("Saved entity to DB with ID: " + entity.getId());
        return QuantityMeasurementDTO.fromEntity(entity);
    }

    // ── Add with target unit ──────────────────────────────────────────────────
    @Override
    public QuantityMeasurementDTO add(QuantityDTO dto1, QuantityDTO dto2,
                                      String targetUnit) {
        QuantityMeasurementEntity entity;
        try {
            QuantityModel<IMeasurable> model1 = convertDtoToModel(dto1);
            QuantityModel<IMeasurable> model2 = convertDtoToModel(dto2);
            IMeasurable target = model1.unit.getUnitInstance(targetUnit);
            Quantity<IMeasurable> result = new Quantity<>(model1.value, model1.unit)
                    .add(new Quantity<>(model2.value, model2.unit), target);
            entity = buildResultEntity(model1, model2, "ADD", result);
            logger.info("ADD: " + dto1 + " + " + dto2 + " → " + result
                    + " " + targetUnit);
        } catch (Exception e) {
            entity = buildErrorEntity(dto1, dto2, "ADD", e.getMessage());
        }
        repository.save(entity);
        logger.info("Saved entity to DB with ID: " + entity.getId());
        return QuantityMeasurementDTO.fromEntity(entity);
    }

    // ── Subtract ──────────────────────────────────────────────────────────────
    @Override
    public QuantityMeasurementDTO subtract(QuantityDTO dto1, QuantityDTO dto2) {
        QuantityMeasurementEntity entity;
        try {
            QuantityModel<IMeasurable> model1 = convertDtoToModel(dto1);
            QuantityModel<IMeasurable> model2 = convertDtoToModel(dto2);
            Quantity<IMeasurable> result = new Quantity<>(model1.value, model1.unit)
                    .subtract(new Quantity<>(model2.value, model2.unit));
            entity = buildResultEntity(model1, model2, "SUBTRACT", result);
            logger.info("SUBTRACT: " + dto1 + " - " + dto2 + " → " + result);
        } catch (Exception e) {
            entity = buildErrorEntity(dto1, dto2, "SUBTRACT", e.getMessage());
        }
        repository.save(entity);
        logger.info("Saved entity to DB with ID: " + entity.getId());
        return QuantityMeasurementDTO.fromEntity(entity);
    }

    // ── Divide ────────────────────────────────────────────────────────────────
    @Override
    public QuantityMeasurementDTO divide(QuantityDTO dto1, QuantityDTO dto2) {
        QuantityMeasurementEntity entity;
        try {
            QuantityModel<IMeasurable> model1 = convertDtoToModel(dto1);
            QuantityModel<IMeasurable> model2 = convertDtoToModel(dto2);
            double result = new Quantity<>(model1.value, model1.unit)
                    .divide(new Quantity<>(model2.value, model2.unit));
            entity = new QuantityMeasurementEntity();
            entity.setThisValue(model1.value);
            entity.setThisUnit(model1.unit.getUnitName());
            entity.setThisMeasurementType(model1.unit.getMeasurementType());
            entity.setThatValue(model2.value);
            entity.setThatUnit(model2.unit.getUnitName());
            entity.setThatMeasurementType(model2.unit.getMeasurementType());
            entity.setOperation("DIVIDE");
            entity.setResultValue(result);
            entity.setError(false);
            logger.info("DIVIDE: " + dto1 + " / " + dto2 + " → " + result);
        } catch (Exception e) {
            entity = buildErrorEntity(dto1, dto2, "DIVIDE", e.getMessage());
        }
        repository.save(entity);
        logger.info("Saved entity to DB with ID: " + entity.getId());
        return QuantityMeasurementDTO.fromEntity(entity);
    }

    // ── History & Analytics ───────────────────────────────────────────────────
    @Override
    public List<QuantityMeasurementDTO> getHistoryByMeasurementType(
            String measurementType) {
        return QuantityMeasurementDTO.fromEntityList(
                repository.findByThisMeasurementType(measurementType));
    }

    @Override
    public List<QuantityMeasurementDTO> getHistoryByOperation(String operation) {
        return QuantityMeasurementDTO.fromEntityList(
                repository.findByOperation(operation));
    }

    @Override
    public long getOperationCount(String operation) {
        return repository.countByOperationAndIsErrorFalse(operation);
    }

    // ── Helper: Convert DTO → Model ───────────────────────────────────────────
    private QuantityModel<IMeasurable> convertDtoToModel(QuantityDTO dto) {
        IMeasurable unit = resolveUnit(dto.getMeasurementType(), dto.getUnit());
        return new QuantityModel<>(dto.getValue(), unit);
    }

    // ── Helper: Resolve unit from type + name ─────────────────────────────────
    private IMeasurable resolveUnit(String measurementType, String unitName) {
        switch (measurementType) {
            case "LengthUnit":      return LengthUnit.INCH.getUnitInstance(unitName);
            case "WeightUnit":      return WeightUnit.GRAM.getUnitInstance(unitName);
            case "VolumeUnit":      return VolumeUnit.LITRE.getUnitInstance(unitName);
            case "TemperatureUnit": return TemperatureUnit.CELSIUS.getUnitInstance(unitName);
            default: throw new IllegalArgumentException(
                "Unknown measurement type: " + measurementType);
        }
    }

    // ── Helper: Build result entity ───────────────────────────────────────────
    private QuantityMeasurementEntity buildResultEntity(
            QuantityModel<IMeasurable> model1,
            QuantityModel<IMeasurable> model2,
            String operation,
            Quantity<IMeasurable> result) {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.setThisValue(model1.value);
        entity.setThisUnit(model1.unit.getUnitName());
        entity.setThisMeasurementType(model1.unit.getMeasurementType());
        entity.setThatValue(model2.value);
        entity.setThatUnit(model2.unit.getUnitName());
        entity.setThatMeasurementType(model2.unit.getMeasurementType());
        entity.setOperation(operation);
        entity.setResultValue(result.getValue());
        entity.setResultUnit(result.getUnit().getUnitName());
        entity.setResultMeasurementType(result.getUnit().getMeasurementType());
        entity.setError(false);
        return entity;
    }

    // ── Helper: Build error entity ────────────────────────────────────────────
    private QuantityMeasurementEntity buildErrorEntity(
            QuantityDTO dto1, QuantityDTO dto2,
            String operation, String errorMessage) {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        if (dto1 != null) {
            entity.setThisValue(dto1.getValue());
            entity.setThisUnit(dto1.getUnit());
            entity.setThisMeasurementType(dto1.getMeasurementType());
        }
        if (dto2 != null) {
            entity.setThatValue(dto2.getValue());
            entity.setThatUnit(dto2.getUnit());
            entity.setThatMeasurementType(dto2.getMeasurementType());
        }
        entity.setOperation(operation);
        entity.setError(true);
        entity.setErrorMessage(errorMessage);
        logger.severe(operation + " ERROR: " + errorMessage);
        return entity;
    }
}