package com.app.qma.dto;

import com.app.qma.model.QuantityMeasurementEntity;

import java.time.LocalDateTime;
import java.util.List;

public class QuantityMeasurementDTO {

    private Long id;
    private double thisValue;
    private String thisUnit;
    private String thisMeasurementType;
    private double thatValue;
    private String thatUnit;
    private String thatMeasurementType;
    private String operation;
    private double resultValue;
    private String resultUnit;
    private String resultMeasurementType;
    private String resultString;
    private boolean error;
    private String errorMessage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static QuantityMeasurementDTO fromEntity(QuantityMeasurementEntity entity) {
        QuantityMeasurementDTO dto = new QuantityMeasurementDTO();
        dto.id = entity.getId();
        dto.thisValue = entity.getThisValue();
        dto.thisUnit = entity.getThisUnit();
        dto.thisMeasurementType = entity.getThisMeasurementType();
        dto.thatValue = entity.getThatValue();
        dto.thatUnit = entity.getThatUnit();
        dto.thatMeasurementType = entity.getThatMeasurementType();
        dto.operation = entity.getOperation();
        dto.resultValue = entity.getResultValue();
        dto.resultUnit = entity.getResultUnit();
        dto.resultMeasurementType = entity.getResultMeasurementType();
        dto.resultString = entity.getResultString();
        dto.error = entity.isError();
        dto.errorMessage = entity.getErrorMessage();
        dto.createdAt = entity.getCreatedAt();
        dto.updatedAt = entity.getUpdatedAt();
        return dto;
    }

    public static List<QuantityMeasurementDTO> fromEntities(List<QuantityMeasurementEntity> entities) {
        return entities.stream().map(QuantityMeasurementDTO::fromEntity).toList();
    }

    public Long getId() {
        return id;
    }

    public double getThisValue() {
        return thisValue;
    }

    public String getThisUnit() {
        return thisUnit;
    }

    public String getThisMeasurementType() {
        return thisMeasurementType;
    }

    public double getThatValue() {
        return thatValue;
    }

    public String getThatUnit() {
        return thatUnit;
    }

    public String getThatMeasurementType() {
        return thatMeasurementType;
    }

    public String getOperation() {
        return operation;
    }

    public double getResultValue() {
        return resultValue;
    }

    public String getResultUnit() {
        return resultUnit;
    }

    public String getResultMeasurementType() {
        return resultMeasurementType;
    }

    public String getResultString() {
        return resultString;
    }

    public boolean isError() {
        return error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}

