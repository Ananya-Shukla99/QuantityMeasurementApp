package com.app.qma.controller;

import com.app.qma.dto.QuantityInputDTO;
import com.app.qma.dto.QuantityMeasurementDTO;
import com.app.qma.service.QuantityMeasurementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Quantity Measurement Controller - Handles measurement operations
 * Provides REST APIs for comparing, converting, and calculating quantities
 */
@RestController
@RequestMapping("/api/v1/quantities")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Quantity Measurements", description = "APIs for quantity conversions and comparisons")
public class QuantityMeasurementController {

    private final QuantityMeasurementService service;

    /**
     * Compare two quantities for equality
     */
    @PostMapping("/compare")
    @Operation(summary = "Compare two quantities", description = "Check if two quantities are equal")
    public ResponseEntity<QuantityMeasurementDTO> compare(@RequestBody @Valid QuantityInputDTO input) {
        log.info("Comparing quantities");
        return ResponseEntity.ok(service.compare(input.getQuantity1(), input.getQuantity2()));
    }

    /**
     * Convert quantity to target unit
     */
    @PostMapping("/convert")
    @Operation(summary = "Convert quantity", description = "Convert quantity to a different unit")
    public ResponseEntity<QuantityMeasurementDTO> convert(@RequestBody @Valid QuantityInputDTO input) {
        log.info("Converting quantity");
        return ResponseEntity.ok(service.convert(input.getQuantity1(), input.getTargetUnit()));
    }

    /**
     * Add two quantities
     */
    @PostMapping("/add")
    @Operation(summary = "Add quantities", description = "Add two quantities and return result")
    public ResponseEntity<QuantityMeasurementDTO> add(@RequestBody @Valid QuantityInputDTO input) {
        log.info("Adding quantities");
        return ResponseEntity.ok(service.add(input.getQuantity1(), input.getQuantity2(), input.getTargetUnit()));
    }

    /**
     * Subtract two quantities
     */
    @PostMapping("/subtract")
    @Operation(summary = "Subtract quantities", description = "Subtract one quantity from another")
    public ResponseEntity<QuantityMeasurementDTO> subtract(@RequestBody @Valid QuantityInputDTO input) {
        log.info("Subtracting quantities");
        return ResponseEntity.ok(service.subtract(input.getQuantity1(), input.getQuantity2()));
    }

    /**
     * Divide two quantities
     */
    @PostMapping("/divide")
    @Operation(summary = "Divide quantities", description = "Divide one quantity by another")
    public ResponseEntity<QuantityMeasurementDTO> divide(@RequestBody @Valid QuantityInputDTO input) {
        log.info("Dividing quantities");
        return ResponseEntity.ok(service.divide(input.getQuantity1(), input.getQuantity2()));
    }

    /**
     * Get operation history by operation type
     */
    @GetMapping("/history/{operation}")
    @Operation(summary = "Get operation history", description = "Retrieve history of specific operation type")
    public ResponseEntity<List<QuantityMeasurementDTO>> getOperationHistory(
            @Parameter(description = "Operation type (compare, convert, add, subtract, divide)")
            @PathVariable String operation) {
        log.info("Fetching history for operation: {}", operation);
        return ResponseEntity.ok(service.getHistoryByOperation(operation));
    }

    /**
     * Get history by measurement type
     */
    @GetMapping("/history/type/{measurementType}")
    @Operation(summary = "Get history by measurement type", description = "Retrieve history by measurement type")
    public ResponseEntity<List<QuantityMeasurementDTO>> getHistoryByType(
            @Parameter(description = "Measurement type (LENGTH, WEIGHT, VOLUME, TEMPERATURE)")
            @PathVariable String measurementType) {
        log.info("Fetching history for measurement type: {}", measurementType);
        return ResponseEntity.ok(service.getHistoryByMeasurementType(measurementType));
    }

    /**
     * Get count of operations by type
     */
    @GetMapping("/count/{operation}")
    @Operation(summary = "Get operation count", description = "Get total count of specific operation")
    public ResponseEntity<Long> getOperationCount(
            @Parameter(description = "Operation type")
            @PathVariable String operation) {
        log.info("Counting operations: {}", operation);
        return ResponseEntity.ok(service.getOperationCount(operation));
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/")
    @Operation(summary = "Health check", description = "Verify QMA service is running")
    public String home() {
        log.info("Health check requested");
        return "QMA service is running";
    }
}

