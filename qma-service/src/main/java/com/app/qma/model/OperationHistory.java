package com.app.qma.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Entity to track all measurement operations and their results
 * Maintains a complete audit trail of all quantity measurement operations
 */
@Entity
@Table(name = "operation_history", indexes = {
        @Index(name = "idx_operation_name", columnList = "operation_name"),
        @Index(name = "idx_measurement_type", columnList = "measurement_type"),
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_op_created_date", columnList = "created_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperationHistory {

    /**
     * Unique identifier for the operation record
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * User ID who performed the operation
     */
    @Column(name = "user_id", nullable = false)
    private String userId;

    /**
     * Name of the measurement operation (ADD, SUBTRACT, MULTIPLY, DIVIDE, COMPARE, etc.)
     */
    @Column(name = "operation_name", nullable = false)
    private String operationName;

    /**
     * Type of measurement (LENGTH, WEIGHT, VOLUME, TEMPERATURE, etc.)
     */
    @Column(name = "measurement_type", nullable = false)
    private String measurementType;

    /**
     * First input value
     */
    @Column(name = "input_value_1")
    private Double inputValue1;

    /**
     * Unit of the first input value
     */
    @Column(name = "input_unit_1")
    private String inputUnit1;

    /**
     * Second input value
     */
    @Column(name = "input_value_2")
    private Double inputValue2;

    /**
     * Unit of the second input value
     */
    @Column(name = "input_unit_2")
    private String inputUnit2;

    /**
     * Result value of the operation
     */
    @Column(name = "result_value")
    private Double resultValue;

    /**
     * Unit of the result value
     */
    @Column(name = "result_unit")
    private String resultUnit;

    /**
     * Complete result as string representation
     */
    @Column(name = "result_string", columnDefinition = "TEXT")
    private String resultString;

    /**
     * Status of the operation (SUCCESS, FAILURE)
     */
    @Column(name = "operation_status")
    private String operationStatus;

    /**
     * Error message if operation failed
     */
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    /**
     * Execution time in milliseconds
     */
    @Column(name = "execution_time_ms")
    private Long executionTimeMs;

    /**
     * Reference to the QuantityMeasurementEntity if applicable
     */
    @Column(name = "measurement_entity_id")
    private Long measurementEntityId;

    /**
     * Additional notes or metadata about the operation
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    /**
     * Timestamp when the operation was performed
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Automatically set the creation timestamp
     */
    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}

