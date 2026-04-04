package com.app.qma.controller;

import com.app.qma.dto.OperationHistoryDTO;
import com.app.qma.service.OperationHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST Controller for Operation History operations
 * Provides endpoints to track, retrieve, and analyze measurement operations
 */
@RestController
@RequestMapping("/api/v1/operation-history")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Operation History", description = "APIs for tracking and managing measurement operation history")
public class OperationHistoryController {

    // Service for operation history business logic
    private final OperationHistoryService operationHistoryService;

    /**
     * Record a measurement operation
     * @param operationHistoryDTO the operation history data
     * @return saved operation history record
     */
    @PostMapping("/record")
    @Operation(summary = "Record a measurement operation",
            description = "Records a new measurement operation in the history")
    @ApiResponse(responseCode = "201", description = "Operation recorded successfully")
    public ResponseEntity<OperationHistoryDTO> recordOperation(
            @RequestBody OperationHistoryDTO operationHistoryDTO) {
        log.info("Recording operation: {} for user: {}",
                operationHistoryDTO.getOperationName(), operationHistoryDTO.getUserId());
        OperationHistoryDTO saved = operationHistoryService.recordOperation(operationHistoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * Get all operations performed by a user
     * @param userId the user ID
     * @return list of operation history records
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user operations",
            description = "Retrieves all measurement operations performed by a specific user")
    @ApiResponse(responseCode = "200", description = "Operations retrieved successfully")
    public ResponseEntity<List<OperationHistoryDTO>> getUserOperations(
            @Parameter(description = "The user ID", required = true)
            @PathVariable String userId) {
        log.info("Fetching operations for user: {}", userId);
        List<OperationHistoryDTO> operations = operationHistoryService.getUserOperations(userId);
        return ResponseEntity.ok(operations);
    }

    /**
     * Get operations by operation name
     * @param operationName the name of the operation
     * @return list of operation history records
     */
    @GetMapping("/name/{operationName}")
    @Operation(summary = "Get operations by name",
            description = "Retrieves all operations of a specific type")
    @ApiResponse(responseCode = "200", description = "Operations retrieved successfully")
    public ResponseEntity<List<OperationHistoryDTO>> getOperationsByName(
            @Parameter(description = "The operation name", required = true)
            @PathVariable String operationName) {
        log.info("Fetching operations of type: {}", operationName);
        List<OperationHistoryDTO> operations = operationHistoryService.getOperationsByName(operationName);
        return ResponseEntity.ok(operations);
    }

    /**
     * Get operations by measurement type
     * @param measurementType the type of measurement
     * @return list of operation history records
     */
    @GetMapping("/measurement-type/{measurementType}")
    @Operation(summary = "Get operations by measurement type",
            description = "Retrieves all operations for a specific measurement type")
    @ApiResponse(responseCode = "200", description = "Operations retrieved successfully")
    public ResponseEntity<List<OperationHistoryDTO>> getOperationsByMeasurementType(
            @Parameter(description = "The measurement type", required = true)
            @PathVariable String measurementType) {
        log.info("Fetching operations for measurement type: {}", measurementType);
        List<OperationHistoryDTO> operations = operationHistoryService.getOperationsByMeasurementType(measurementType);
        return ResponseEntity.ok(operations);
    }

    /**
     * Get all successful operations
     * @return list of successful operation records
     */
    @GetMapping("/successful")
    @Operation(summary = "Get successful operations",
            description = "Retrieves all successful measurement operations")
    @ApiResponse(responseCode = "200", description = "Operations retrieved successfully")
    public ResponseEntity<List<OperationHistoryDTO>> getSuccessfulOperations() {
        log.info("Fetching all successful operations");
        List<OperationHistoryDTO> operations = operationHistoryService.getSuccessfulOperations();
        return ResponseEntity.ok(operations);
    }

    /**
     * Get all failed operations
     * @return list of failed operation records
     */
    @GetMapping("/failed")
    @Operation(summary = "Get failed operations",
            description = "Retrieves all failed measurement operations")
    @ApiResponse(responseCode = "200", description = "Operations retrieved successfully")
    public ResponseEntity<List<OperationHistoryDTO>> getFailedOperations() {
        log.info("Fetching all failed operations");
        List<OperationHistoryDTO> operations = operationHistoryService.getFailedOperations();
        return ResponseEntity.ok(operations);
    }

    /**
     * Get operations by date range
     * @param userId the user ID
     * @param startDate the start date
     * @param endDate the end date
     * @return list of operation records within the date range
     */
    @GetMapping("/user/{userId}/date-range")
    @Operation(summary = "Get user operations by date range",
            description = "Retrieves user operations within a specified date range")
    @ApiResponse(responseCode = "200", description = "Operations retrieved successfully")
    public ResponseEntity<List<OperationHistoryDTO>> getUserOperationsByDateRange(
            @Parameter(description = "The user ID", required = true)
            @PathVariable String userId,
            @Parameter(description = "Start date (yyyy-MM-dd'T'HH:mm:ss)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date (yyyy-MM-dd'T'HH:mm:ss)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        log.info("Fetching operations for user: {} between {} and {}", userId, startDate, endDate);
        List<OperationHistoryDTO> operations = operationHistoryService.getUserOperationsByDateRange(userId, startDate, endDate);
        return ResponseEntity.ok(operations);
    }

    /**
     * Get all operations ordered by date
     * @return list of all operation records
     */
    @GetMapping("/all")
    @Operation(summary = "Get all operations",
            description = "Retrieves all measurement operations ordered by date (most recent first)")
    @ApiResponse(responseCode = "200", description = "Operations retrieved successfully")
    public ResponseEntity<List<OperationHistoryDTO>> getAllOperations() {
        log.info("Fetching all operations");
        List<OperationHistoryDTO> operations = operationHistoryService.getAllOperationsOrderedByDate();
        return ResponseEntity.ok(operations);
    }

    /**
     * Get operation history by measurement entity
     * @param measurementEntityId the measurement entity ID
     * @return list of operation history records
     */
    @GetMapping("/measurement-entity/{measurementEntityId}")
    @Operation(summary = "Get operations by measurement entity",
            description = "Retrieves all operations related to a specific measurement entity")
    @ApiResponse(responseCode = "200", description = "Operations retrieved successfully")
    public ResponseEntity<List<OperationHistoryDTO>> getOperationsByMeasurementEntity(
            @Parameter(description = "The measurement entity ID", required = true)
            @PathVariable Long measurementEntityId) {
        log.info("Fetching operations for measurement entity: {}", measurementEntityId);
        List<OperationHistoryDTO> operations = operationHistoryService.getOperationsByMeasurementEntity(measurementEntityId);
        return ResponseEntity.ok(operations);
    }

    /**
     * Get user operation count
     * @param userId the user ID
     * @return count of operations
     */
    @GetMapping("/user/{userId}/count")
    @Operation(summary = "Get user operation count",
            description = "Gets the total number of operations performed by a user")
    @ApiResponse(responseCode = "200", description = "Operation count retrieved successfully")
    public ResponseEntity<Long> getUserOperationCount(
            @Parameter(description = "The user ID", required = true)
            @PathVariable String userId) {
        log.info("Getting operation count for user: {}", userId);
        long count = operationHistoryService.getUserOperationCount(userId);
        return ResponseEntity.ok(count);
    }

    /**
     * Get operation type count
     * @param operationName the operation name
     * @return count of operations
     */
    @GetMapping("/name/{operationName}/count")
    @Operation(summary = "Get operation type count",
            description = "Gets the total number of operations of a specific type")
    @ApiResponse(responseCode = "200", description = "Operation count retrieved successfully")
    public ResponseEntity<Long> getOperationTypeCount(
            @Parameter(description = "The operation name", required = true)
            @PathVariable String operationName) {
        log.info("Getting operation count for operation type: {}", operationName);
        long count = operationHistoryService.getOperationTypeCount(operationName);
        return ResponseEntity.ok(count);
    }

    /**
     * Get average execution time
     * @param operationName the operation name
     * @return average execution time in milliseconds
     */
    @GetMapping("/name/{operationName}/avg-execution-time")
    @Operation(summary = "Get average execution time",
            description = "Gets the average execution time for a specific operation type")
    @ApiResponse(responseCode = "200", description = "Average execution time retrieved successfully")
    public ResponseEntity<Double> getAverageExecutionTime(
            @Parameter(description = "The operation name", required = true)
            @PathVariable String operationName) {
        log.info("Getting average execution time for operation: {}", operationName);
        Double avgTime = operationHistoryService.getAverageExecutionTime(operationName);
        return ResponseEntity.ok(avgTime);
    }

    /**
     * Delete an operation history record (for administration purposes)
     * @param id the operation history record ID
     * @return response indicating successful deletion
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete operation history record",
            description = "Deletes a specific operation history record (admin operation)")
    @ApiResponse(responseCode = "204", description = "Record deleted successfully")
    public ResponseEntity<Void> deleteOperationRecord(
            @Parameter(description = "The operation history record ID", required = true)
            @PathVariable Long id) {
        log.info("Deleting operation history record with ID: {}", id);
        operationHistoryService.deleteOperationRecord(id);
        return ResponseEntity.noContent().build();
    }
}

