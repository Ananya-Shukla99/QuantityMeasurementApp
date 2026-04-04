package com.app.qma.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Operation History
 * Used for API requests and responses related to measurement operation tracking
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OperationHistoryDTO {

    /**
     * Unique identifier for the operation record
     */
    private Long id;

    /**
     * User ID who performed the operation
     */
    private String userId;

    /**
     * Name of the measurement operation (ADD, SUBTRACT, MULTIPLY, DIVIDE, COMPARE, etc.)
     */
    private String operationName;

    /**
     * Type of measurement (LENGTH, WEIGHT, VOLUME, TEMPERATURE, etc.)
     */
    private String measurementType;

    /**
     * First input value
     */
    private Double inputValue1;

    /**
     * Unit of the first input value
     */
    private String inputUnit1;

    /**
     * Second input value
     */
    private Double inputValue2;

    /**
     * Unit of the second input value
     */
    private String inputUnit2;

    /**
     * Result value of the operation
     */
    private Double resultValue;

    /**
     * Unit of the result value
     */
    private String resultUnit;

    /**
     * Complete result as string representation
     */
    private String resultString;

    /**
     * Status of the operation (SUCCESS, FAILURE)
     */
    private String operationStatus;

    /**
     * Error message if operation failed
     */
    private String errorMessage;

    /**
     * Execution time in milliseconds
     */
    private Long executionTimeMs;

    /**
     * Reference to the QuantityMeasurementEntity if applicable
     */
    private Long measurementEntityId;

    /**
     * Additional notes or metadata about the operation
     */
    private String notes;

    /**
     * Timestamp when the operation was performed
     */
    private LocalDateTime createdAt;
}

