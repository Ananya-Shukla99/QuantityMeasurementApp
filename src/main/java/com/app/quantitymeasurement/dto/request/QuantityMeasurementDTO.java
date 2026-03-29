package com.app.quantitymeasurement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.app.quantitymeasurement.model.QuantityMeasurementEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuantityMeasurementDTO {

    private Long id;

    // ── First Operand ─────────────────────────────────────────────────────────
    private double thisValue;
    private String thisUnit;
    private String thisMeasurementType;

    // ── Second Operand ────────────────────────────────────────────────────────
    private double thatValue;
    private String thatUnit;
    private String thatMeasurementType;

    // ── Operation ─────────────────────────────────────────────────────────────
    private String operation;

    // ── Numeric Result ────────────────────────────────────────────────────────
    private double resultValue;
    private String resultUnit;
    private String resultMeasurementType;

    // ── String Result (COMPARE) ───────────────────────────────────────────────
    private String resultString;

    // ── Error Info ────────────────────────────────────────────────────────────
    private boolean isError;
    private String errorMessage;

    // ── Timestamps ────────────────────────────────────────────────────────────
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ── Static Factory Methods ────────────────────────────────────────────────

    // Convert Entity → DTO
    public static QuantityMeasurementDTO fromEntity(QuantityMeasurementEntity entity) {
        QuantityMeasurementDTO dto = new QuantityMeasurementDTO();
        dto.setId(entity.getId());
        dto.setThisValue(entity.getThisValue());
        dto.setThisUnit(entity.getThisUnit());
        dto.setThisMeasurementType(entity.getThisMeasurementType());
        dto.setThatValue(entity.getThatValue());
        dto.setThatUnit(entity.getThatUnit());
        dto.setThatMeasurementType(entity.getThatMeasurementType());
        dto.setOperation(entity.getOperation());
        dto.setResultValue(entity.getResultValue());
        dto.setResultUnit(entity.getResultUnit());
        dto.setResultMeasurementType(entity.getResultMeasurementType());
        dto.setResultString(entity.getResultString());
        dto.setError(entity.isError());
        dto.setErrorMessage(entity.getErrorMessage());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    // Convert DTO → Entity
    public QuantityMeasurementEntity toEntity() {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.setThisValue(this.thisValue);
        entity.setThisUnit(this.thisUnit);
        entity.setThisMeasurementType(this.thisMeasurementType);
        entity.setThatValue(this.thatValue);
        entity.setThatUnit(this.thatUnit);
        entity.setThatMeasurementType(this.thatMeasurementType);
        entity.setOperation(this.operation);
        entity.setResultValue(this.resultValue);
        entity.setResultUnit(this.resultUnit);
        entity.setResultMeasurementType(this.resultMeasurementType);
        entity.setResultString(this.resultString);
        entity.setError(this.isError);
        entity.setErrorMessage(this.errorMessage);
        return entity;
    }

    // Convert List<Entity> → List<DTO>
    public static List<QuantityMeasurementDTO> fromEntityList(
            List<QuantityMeasurementEntity> entities) {
        return entities.stream()
                .map(QuantityMeasurementDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // Convert List<DTO> → List<Entity>
    public static List<QuantityMeasurementEntity> toEntityList(
            List<QuantityMeasurementDTO> dtos) {
        return dtos.stream()
                .map(QuantityMeasurementDTO::toEntity)
                .collect(Collectors.toList());
    }
}