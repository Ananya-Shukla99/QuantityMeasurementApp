package com.app.qma.service;

import com.app.qma.dto.QuantityDTO;
import com.app.qma.dto.QuantityMeasurementDTO;
import com.app.qma.model.QuantityMeasurementEntity;
import com.app.qma.repository.QuantityMeasurementRepository;
import com.app.qma.unit.IMeasurable;
import com.app.qma.unit.LengthUnit;
import com.app.qma.unit.TemperatureUnit;
import com.app.qma.unit.VolumeUnit;
import com.app.qma.unit.WeightUnit;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuantityMeasurementService {

    private final QuantityMeasurementRepository repository;

    public QuantityMeasurementService(QuantityMeasurementRepository repository) {
        this.repository = repository;
    }

    public QuantityMeasurementDTO compare(QuantityDTO q1, QuantityDTO q2) {
        return saveSafely("COMPARE", q1, q2, entity -> {
            QuantityValue<IMeasurable> first = toQuantity(q1);
            QuantityValue<IMeasurable> second = toQuantity(q2);
            entity.setResultString(first.equivalentTo(second) ? "Equal" : "Not Equal");
        });
    }

    public QuantityMeasurementDTO convert(QuantityDTO q1, String targetUnit) {
        return saveSafely("CONVERT", q1, null, entity -> {
            QuantityValue<IMeasurable> first = toQuantity(q1);
            IMeasurable target = first.getUnit().getUnitInstance(targetUnit);
            QuantityValue<IMeasurable> result = first.convertTo(target);
            entity.setResultValue(result.getValue());
            entity.setResultUnit(result.getUnit().getUnitName());
            entity.setResultMeasurementType(result.getUnit().getMeasurementType());
        });
    }

    public QuantityMeasurementDTO add(QuantityDTO q1, QuantityDTO q2, String targetUnit) {
        return saveSafely("ADD", q1, q2, entity -> {
            QuantityValue<IMeasurable> first = toQuantity(q1);
            QuantityValue<IMeasurable> second = toQuantity(q2);
            IMeasurable target = targetUnit == null ? first.getUnit() : first.getUnit().getUnitInstance(targetUnit);
            QuantityValue<IMeasurable> result = first.add(second, target);
            entity.setResultValue(result.getValue());
            entity.setResultUnit(result.getUnit().getUnitName());
            entity.setResultMeasurementType(result.getUnit().getMeasurementType());
        });
    }

    public QuantityMeasurementDTO subtract(QuantityDTO q1, QuantityDTO q2) {
        return saveSafely("SUBTRACT", q1, q2, entity -> {
            QuantityValue<IMeasurable> first = toQuantity(q1);
            QuantityValue<IMeasurable> second = toQuantity(q2);
            QuantityValue<IMeasurable> result = first.subtract(second, first.getUnit());
            entity.setResultValue(result.getValue());
            entity.setResultUnit(result.getUnit().getUnitName());
            entity.setResultMeasurementType(result.getUnit().getMeasurementType());
        });
    }

    public QuantityMeasurementDTO divide(QuantityDTO q1, QuantityDTO q2) {
        return saveSafely("DIVIDE", q1, q2, entity -> {
            QuantityValue<IMeasurable> first = toQuantity(q1);
            QuantityValue<IMeasurable> second = toQuantity(q2);
            entity.setResultValue(first.divide(second));
        });
    }

    public List<QuantityMeasurementDTO> getHistoryByOperation(String operation) {
        return QuantityMeasurementDTO.fromEntities(repository.findByOperation(operation.toUpperCase()));
    }

    public List<QuantityMeasurementDTO> getHistoryByMeasurementType(String measurementType) {
        return QuantityMeasurementDTO.fromEntities(repository.findByThisMeasurementType(measurementType));
    }

    public long getOperationCount(String operation) {
        return repository.countByOperationIgnoreCaseAndErrorFalse(operation);
    }

    private QuantityMeasurementDTO saveSafely(String operation, QuantityDTO q1, QuantityDTO q2, EntityLogic logic) {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.setOperation(operation);
        copyOperand1(entity, q1);
        copyOperand2(entity, q2);

        try {
            logic.apply(entity);
            entity.setError(false);
        } catch (Exception ex) {
            entity.setError(true);
            entity.setErrorMessage(ex.getMessage());
        }

        return QuantityMeasurementDTO.fromEntity(repository.save(entity));
    }

    private void copyOperand1(QuantityMeasurementEntity entity, QuantityDTO dto) {
        entity.setThisValue(dto.getValue());
        entity.setThisUnit(dto.getUnit());
        entity.setThisMeasurementType(dto.getMeasurementType());
    }

    private void copyOperand2(QuantityMeasurementEntity entity, QuantityDTO dto) {
        if (dto == null) {
            return;
        }
        entity.setThatValue(dto.getValue());
        entity.setThatUnit(dto.getUnit());
        entity.setThatMeasurementType(dto.getMeasurementType());
    }

    private QuantityValue<IMeasurable> toQuantity(QuantityDTO dto) {
        IMeasurable unit = switch (dto.getMeasurementType()) {
            case "LengthUnit" -> LengthUnit.INCH.getUnitInstance(dto.getUnit());
            case "WeightUnit" -> WeightUnit.GRAM.getUnitInstance(dto.getUnit());
            case "VolumeUnit" -> VolumeUnit.LITRE.getUnitInstance(dto.getUnit());
            case "TemperatureUnit" -> TemperatureUnit.CELSIUS.getUnitInstance(dto.getUnit());
            default -> throw new IllegalArgumentException("Unknown measurement type: " + dto.getMeasurementType());
        };
        return new QuantityValue<>(dto.getValue(), unit);
    }

    @FunctionalInterface
    private interface EntityLogic {
        void apply(QuantityMeasurementEntity entity);
    }
}

