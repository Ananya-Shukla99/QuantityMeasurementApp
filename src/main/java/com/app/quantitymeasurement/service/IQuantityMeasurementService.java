package com.app.quantitymeasurement.service;

import java.util.List;

import com.app.quantitymeasurement.dto.request.QuantityMeasurementDTO;
import com.app.quantitymeasurement.dto.response.QuantityDTO;

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