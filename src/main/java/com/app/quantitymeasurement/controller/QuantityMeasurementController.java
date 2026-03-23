package com.app.quantitymeasurement.controller;

import com.app.quantitymeasurement.DTO.QuantityInputDTO;
import com.app.quantitymeasurement.DTO.QuantityMeasurementDTO;
import com.app.quantitymeasurement.service.IQuantityMeasurementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/quantities")
@Tag(name = "Quantity Measurements",
     description = "REST API for quantity measurement operations")
public class QuantityMeasurementController {

    private static final Logger logger = Logger.getLogger(
            QuantityMeasurementController.class.getName()
    );

    // ── Service injected by Spring ────────────────────────────────────────────
    @Autowired
    private IQuantityMeasurementService service;

    // ── COMPARE ───────────────────────────────────────────────────────────────
    @PostMapping("/compare")
    @Operation(summary = "Compare two quantities")
    public ResponseEntity<QuantityMeasurementDTO> performComparison(
            @RequestBody QuantityInputDTO input) {
        try {
            QuantityMeasurementDTO result = service.compare(
                    input.getQuantity1(), input.getQuantity2());
            logger.info("COMPARE: " + input.getQuantity1()
                    + " vs " + input.getQuantity2());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.severe("Comparison Error: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    // ── CONVERT ───────────────────────────────────────────────────────────────
    @PostMapping("/convert")
    @Operation(summary = "Convert a quantity to a target unit")
    public ResponseEntity<QuantityMeasurementDTO> performConversion(
            @RequestBody QuantityInputDTO input) {
        try {
            QuantityMeasurementDTO result = service.convert(
                    input.getQuantity1(), input.getTargetUnit());
            logger.info("CONVERT: " + input.getQuantity1()
                    + " → " + input.getTargetUnit());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.severe("Conversion Error: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    // ── ADD ───────────────────────────────────────────────────────────────────
    @PostMapping("/add")
    @Operation(summary = "Add two quantities")
    public ResponseEntity<QuantityMeasurementDTO> performAddition(
            @RequestBody QuantityInputDTO input) {
        try {
            QuantityMeasurementDTO result;
            if (input.getTargetUnit() != null) {
                result = service.add(
                        input.getQuantity1(),
                        input.getQuantity2(),
                        input.getTargetUnit());
            } else {
                result = service.add(
                        input.getQuantity1(),
                        input.getQuantity2());
            }
            logger.info("ADD: " + input.getQuantity1()
                    + " + " + input.getQuantity2());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.severe("Addition Error: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    // ── SUBTRACT ──────────────────────────────────────────────────────────────
    @PostMapping("/subtract")
    @Operation(summary = "Subtract two quantities")
    public ResponseEntity<QuantityMeasurementDTO> performSubtraction(
            @RequestBody QuantityInputDTO input) {
        try {
            QuantityMeasurementDTO result = service.subtract(
                    input.getQuantity1(), input.getQuantity2());
            logger.info("SUBTRACT: " + input.getQuantity1()
                    + " - " + input.getQuantity2());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.severe("Subtraction Error: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    // ── DIVIDE ────────────────────────────────────────────────────────────────
    @PostMapping("/divide")
    @Operation(summary = "Divide two quantities")
    public ResponseEntity<QuantityMeasurementDTO> performDivision(
            @RequestBody QuantityInputDTO input) {
        try {
            QuantityMeasurementDTO result = service.divide(
                    input.getQuantity1(), input.getQuantity2());
            logger.info("DIVIDE: " + input.getQuantity1()
                    + " / " + input.getQuantity2());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.severe("Division Error: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    // ── HISTORY ───────────────────────────────────────────────────────────────
    @GetMapping("/history/{operation}")
    @Operation(summary = "Get operation history by operation type")
    public ResponseEntity<List<QuantityMeasurementDTO>> getOperationHistory(
            @PathVariable String operation) {
        try {
            List<QuantityMeasurementDTO> history =
                    service.getHistoryByOperation(operation);
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            logger.severe("History Error: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/history/type/{measurementType}")
    @Operation(summary = "Get history by measurement type")
    public ResponseEntity<List<QuantityMeasurementDTO>> getHistoryByType(
            @PathVariable String measurementType) {
        try {
            List<QuantityMeasurementDTO> history =
                    service.getHistoryByMeasurementType(measurementType);
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            logger.severe("History Error: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/count/{operation}")
    @Operation(summary = "Get count of successful operations")
    public ResponseEntity<Long> getOperationCount(
            @PathVariable String operation) {
        try {
            long count = service.getOperationCount(operation);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            logger.severe("Count Error: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}