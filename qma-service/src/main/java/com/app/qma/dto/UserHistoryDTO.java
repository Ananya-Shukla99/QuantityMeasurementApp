package com.app.qma.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for User History
 * Used for API requests and responses related to user activity tracking
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserHistoryDTO {

    /**
     * Unique identifier for the user history record
     */
    private Long id;

    /**
     * User ID who performed the operation
     */
    private String userId;

    /**
     * Username of the user who performed the operation
     */
    private String username;

    /**
     * Email address of the user
     */
    private String email;

    /**
     * Type of operation performed (CREATE, READ, UPDATE, DELETE, LOGIN, LOGOUT, etc.)
     */
    private String operationType;

    /**
     * Detailed description of the operation
     */
    private String operationDescription;

    /**
     * Entity/Resource that was affected by the operation
     */
    private String affectedEntity;

    /**
     * ID of the affected resource
     */
    private String affectedEntityId;

    /**
     * HTTP method used (GET, POST, PUT, DELETE, PATCH)
     */
    private String httpMethod;

    /**
     * API endpoint that was called
     */
    private String apiEndpoint;

    /**
     * IP address of the request
     */
    private String ipAddress;

    /**
     * Status of the operation (SUCCESS, FAILURE)
     */
    private String operationStatus;

    /**
     * Error message if operation failed
     */
    private String errorMessage;

    /**
     * Additional metadata about the operation
     */
    private String metadata;

    /**
     * Timestamp when the operation was performed
     */
    private LocalDateTime createdAt;
}

