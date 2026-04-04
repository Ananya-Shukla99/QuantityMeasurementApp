package com.app.qma.controller;

import com.app.qma.dto.UserHistoryDTO;
import com.app.qma.service.UserHistoryService;
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
 * REST Controller for User History operations
 * Provides endpoints to track, retrieve, and analyze user activity
 */
@RestController
@RequestMapping("/api/v1/user-history")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User History", description = "APIs for tracking and managing user activity history")
public class UserHistoryController {

    // Service for user history business logic
    private final UserHistoryService userHistoryService;

    /**
     * Record a user operation
     * @param userHistoryDTO the user history data
     * @return saved user history record
     */
    @PostMapping("/record")
    @Operation(summary = "Record a user operation",
            description = "Records a new user activity/operation in the history")
    @ApiResponse(responseCode = "201", description = "Operation recorded successfully")
    public ResponseEntity<UserHistoryDTO> recordUserOperation(
            @RequestBody UserHistoryDTO userHistoryDTO) {
        log.info("Recording user operation for user: {}", userHistoryDTO.getUserId());
        UserHistoryDTO saved = userHistoryService.recordUserOperation(userHistoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * Get all history records for a specific user
     * @param userId the user ID
     * @return list of user history records
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user history",
            description = "Retrieves all activity history records for a specific user")
    @ApiResponse(responseCode = "200", description = "History records retrieved successfully")
    public ResponseEntity<List<UserHistoryDTO>> getUserHistory(
            @Parameter(description = "The user ID", required = true)
            @PathVariable String userId) {
        log.info("Fetching history for user: {}", userId);
        List<UserHistoryDTO> history = userHistoryService.getUserHistory(userId);
        return ResponseEntity.ok(history);
    }

    /**
     * Get history by operation type
     * @param operationType the type of operation
     * @return list of history records
     */
    @GetMapping("/operation-type/{operationType}")
    @Operation(summary = "Get history by operation type",
            description = "Retrieves all history records for a specific operation type")
    @ApiResponse(responseCode = "200", description = "History records retrieved successfully")
    public ResponseEntity<List<UserHistoryDTO>> getHistoryByOperationType(
            @Parameter(description = "The operation type", required = true)
            @PathVariable String operationType) {
        log.info("Fetching history for operation type: {}", operationType);
        List<UserHistoryDTO> history = userHistoryService.getHistoryByOperationType(operationType);
        return ResponseEntity.ok(history);
    }

    /**
     * Get operations by date range
     * @param userId the user ID
     * @param startDate the start date
     * @param endDate the end date
     * @return list of history records within the date range
     */
    @GetMapping("/user/{userId}/date-range")
    @Operation(summary = "Get user operations by date range",
            description = "Retrieves user operations within a specified date range")
    @ApiResponse(responseCode = "200", description = "Operations retrieved successfully")
    public ResponseEntity<List<UserHistoryDTO>> getUserOperationsByDateRange(
            @Parameter(description = "The user ID", required = true)
            @PathVariable String userId,
            @Parameter(description = "Start date (yyyy-MM-dd'T'HH:mm:ss)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date (yyyy-MM-dd'T'HH:mm:ss)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        log.info("Fetching operations for user: {} between {} and {}", userId, startDate, endDate);
        List<UserHistoryDTO> history = userHistoryService.getUserOperationsByDateRange(userId, startDate, endDate);
        return ResponseEntity.ok(history);
    }

    /**
     * Get all failed operations
     * @return list of failed operation records
     */
    @GetMapping("/failed")
    @Operation(summary = "Get failed operations",
            description = "Retrieves all failed user operations")
    @ApiResponse(responseCode = "200", description = "Failed operations retrieved successfully")
    public ResponseEntity<List<UserHistoryDTO>> getFailedOperations() {
        log.info("Fetching all failed operations");
        List<UserHistoryDTO> history = userHistoryService.getFailedOperations();
        return ResponseEntity.ok(history);
    }

    /**
     * Get operations by affected entity
     * @param affectedEntity the affected entity name
     * @return list of history records
     */
    @GetMapping("/affected-entity/{affectedEntity}")
    @Operation(summary = "Get operations by affected entity",
            description = "Retrieves all operations performed on a specific entity")
    @ApiResponse(responseCode = "200", description = "Operations retrieved successfully")
    public ResponseEntity<List<UserHistoryDTO>> getOperationsByAffectedEntity(
            @Parameter(description = "The affected entity name", required = true)
            @PathVariable String affectedEntity) {
        log.info("Fetching operations for affected entity: {}", affectedEntity);
        List<UserHistoryDTO> history = userHistoryService.getOperationsByAffectedEntity(affectedEntity);
        return ResponseEntity.ok(history);
    }

    /**
     * Get all history records ordered by date
     * @return list of all history records
     */
    @GetMapping("/all")
    @Operation(summary = "Get all history records",
            description = "Retrieves all user history records ordered by date (most recent first)")
    @ApiResponse(responseCode = "200", description = "History records retrieved successfully")
    public ResponseEntity<List<UserHistoryDTO>> getAllHistory() {
        log.info("Fetching all history records");
        List<UserHistoryDTO> history = userHistoryService.getAllHistoryOrderedByDate();
        return ResponseEntity.ok(history);
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
        long count = userHistoryService.getUserOperationCount(userId);
        return ResponseEntity.ok(count);
    }

    /**
     * Get history by username
     * @param username the username
     * @return list of history records
     */
    @GetMapping("/username/{username}")
    @Operation(summary = "Get history by username",
            description = "Retrieves all history records for a specific username")
    @ApiResponse(responseCode = "200", description = "History records retrieved successfully")
    public ResponseEntity<List<UserHistoryDTO>> getHistoryByUsername(
            @Parameter(description = "The username", required = true)
            @PathVariable String username) {
        log.info("Fetching history for username: {}", username);
        List<UserHistoryDTO> history = userHistoryService.getHistoryByUsername(username);
        return ResponseEntity.ok(history);
    }

    /**
     * Delete a history record (for administration purposes)
     * @param id the history record ID
     * @return response indicating successful deletion
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete history record",
            description = "Deletes a specific user history record (admin operation)")
    @ApiResponse(responseCode = "204", description = "Record deleted successfully")
    public ResponseEntity<Void> deleteHistoryRecord(
            @Parameter(description = "The history record ID", required = true)
            @PathVariable Long id) {
        log.info("Deleting history record with ID: {}", id);
        userHistoryService.deleteHistoryRecord(id);
        return ResponseEntity.noContent().build();
    }
}

