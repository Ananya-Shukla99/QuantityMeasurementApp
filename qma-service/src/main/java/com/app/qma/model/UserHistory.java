package com.app.qma.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Entity to track user activities and operations history
 * Maintains a complete audit trail of all user interactions
 */
@Entity
@Table(name = "user_history", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_operation_type", columnList = "operation_type"),
        @Index(name = "idx_created_date", columnList = "created_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserHistory {

    /**
     * Unique identifier for the user history record
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
     * Username of the user who performed the operation
     */
    @Column(name = "username")
    private String username;

    /**
     * Email address of the user
     */
    @Column(name = "email")
    private String email;

    /**
     * Type of operation performed (CREATE, READ, UPDATE, DELETE, LOGIN, LOGOUT, etc.)
     */
    @Column(name = "operation_type", nullable = false)
    private String operationType;

    /**
     * Detailed description of the operation
     */
    @Column(name = "operation_description", columnDefinition = "TEXT")
    private String operationDescription;

    /**
     * Entity/Resource that was affected by the operation
     */
    @Column(name = "affected_entity")
    private String affectedEntity;

    /**
     * ID of the affected resource
     */
    @Column(name = "affected_entity_id")
    private String affectedEntityId;

    /**
     * HTTP method used (GET, POST, PUT, DELETE, PATCH)
     */
    @Column(name = "http_method")
    private String httpMethod;

    /**
     * API endpoint that was called
     */
    @Column(name = "api_endpoint")
    private String apiEndpoint;

    /**
     * IP address of the request
     */
    @Column(name = "ip_address")
    private String ipAddress;

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
     * Additional metadata about the operation
     */
    @Column(name = "metadata", columnDefinition = "JSON")
    private String metadata;

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

