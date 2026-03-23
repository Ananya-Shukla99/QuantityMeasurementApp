package com.app.quantitymeasurement.service;

import com.app.quantitymeasurement.DTO.QuantityDTO;
import com.app.quantitymeasurement.DTO.QuantityMeasurementDTO;

import java.util.List;

public interface IQuantityMeasurementService {

    // ── Core Operations ───────────────────────────────────────────────────────
    QuantityMeasurementDTO compare(QuantityDTO dto1, QuantityDTO dto2);

    QuantityMeasurementDTO convert(QuantityDTO dto, String targetUnit);

    QuantityMeasurementDTO add(QuantityDTO dto1, QuantityDTO dto2);

    QuantityMeasurementDTO add(QuantityDTO dto1, QuantityDTO dto2, String targetUnit);

    QuantityMeasurementDTO subtract(QuantityDTO dto1, QuantityDTO dto2);

    QuantityMeasurementDTO divide(QuantityDTO dto1, QuantityDTO dto2);

    // ── History & Analytics ───────────────────────────────────────────────────
    List<QuantityMeasurementDTO> getHistoryByMeasurementType(String measurementType);

    List<QuantityMeasurementDTO> getHistoryByOperation(String operation);

    long getOperationCount(String operation);

}