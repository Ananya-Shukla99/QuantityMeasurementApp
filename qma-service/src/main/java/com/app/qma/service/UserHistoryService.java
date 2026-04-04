package com.app.qma.service;

import com.app.qma.dto.UserHistoryDTO;
import com.app.qma.model.UserHistory;
import com.app.qma.repository.UserHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing user activity history
 * Provides business logic for tracking, retrieving, and analyzing user operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserHistoryService {

    // Repository for user history database operations
    private final UserHistoryRepository userHistoryRepository;

    /**
     * Record a user operation in the history
     * @param userHistoryDTO the user history data
     * @return the saved user history record
     */
    public UserHistoryDTO recordUserOperation(UserHistoryDTO userHistoryDTO) {
        log.info("Recording user operation for user: {}, operation: {}",
                userHistoryDTO.getUserId(), userHistoryDTO.getOperationType());

        UserHistory userHistory = convertDtoToEntity(userHistoryDTO);
        UserHistory saved = userHistoryRepository.save(userHistory);

        log.info("User operation recorded successfully with ID: {}", saved.getId());
        return convertEntityToDto(saved);
    }

    /**
     * Retrieve all history records for a specific user
     * @param userId the user ID
     * @return list of user history DTOs
     */
    public List<UserHistoryDTO> getUserHistory(String userId) {
        log.info("Fetching history for user: {}", userId);
        return userHistoryRepository.findByUserId(userId)
                .stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve all history records for a specific operation type
     * @param operationType the type of operation
     * @return list of user history DTOs
     */
    public List<UserHistoryDTO> getHistoryByOperationType(String operationType) {
        log.info("Fetching history for operation type: {}", operationType);
        return userHistoryRepository.findByOperationType(operationType)
                .stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve user operations within a date range
     * @param userId the user ID
     * @param startDate the start date
     * @param endDate the end date
     * @return list of user history DTOs within the date range
     */
    public List<UserHistoryDTO> getUserOperationsByDateRange(String userId, LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Fetching operations for user: {} between {} and {}", userId, startDate, endDate);
        return userHistoryRepository.findUserOperationsByDateRange(userId, startDate, endDate)
                .stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve all failed operations
     * @return list of failed operation DTOs
     */
    public List<UserHistoryDTO> getFailedOperations() {
        log.info("Fetching all failed operations");
        return userHistoryRepository.findByOperationStatus("FAILURE")
                .stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve all operations performed on a specific entity
     * @param affectedEntity the affected entity name
     * @return list of user history DTOs
     */
    public List<UserHistoryDTO> getOperationsByAffectedEntity(String affectedEntity) {
        log.info("Fetching operations for affected entity: {}", affectedEntity);
        return userHistoryRepository.findByAffectedEntity(affectedEntity)
                .stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve all history records ordered by creation date (most recent first)
     * @return list of user history DTOs
     */
    public List<UserHistoryDTO> getAllHistoryOrderedByDate() {
        log.info("Fetching all history records ordered by date");
        return userHistoryRepository.findAllOrderByCreatedAtDesc()
                .stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    /**
     * Get count of operations performed by a user
     * @param userId the user ID
     * @return count of operations
     */
    public long getUserOperationCount(String userId) {
        log.info("Getting operation count for user: {}", userId);
        return userHistoryRepository.countByUserId(userId);
    }

    /**
     * Get history by username
     * @param username the username
     * @return list of user history DTOs
     */
    public List<UserHistoryDTO> getHistoryByUsername(String username) {
        log.info("Fetching history for username: {}", username);
        return userHistoryRepository.findByUsername(username)
                .stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    /**
     * Delete history record by ID (for administration purposes)
     * @param id the history record ID
     */
    public void deleteHistoryRecord(Long id) {
        log.info("Deleting history record with ID: {}", id);
        userHistoryRepository.deleteById(id);
    }

    /**
     * Convert UserHistory entity to UserHistoryDTO
     * @param userHistory the entity
     * @return the DTO
     */
    private UserHistoryDTO convertEntityToDto(UserHistory userHistory) {
        return UserHistoryDTO.builder()
                .id(userHistory.getId())
                .userId(userHistory.getUserId())
                .username(userHistory.getUsername())
                .email(userHistory.getEmail())
                .operationType(userHistory.getOperationType())
                .operationDescription(userHistory.getOperationDescription())
                .affectedEntity(userHistory.getAffectedEntity())
                .affectedEntityId(userHistory.getAffectedEntityId())
                .httpMethod(userHistory.getHttpMethod())
                .apiEndpoint(userHistory.getApiEndpoint())
                .ipAddress(userHistory.getIpAddress())
                .operationStatus(userHistory.getOperationStatus())
                .errorMessage(userHistory.getErrorMessage())
                .metadata(userHistory.getMetadata())
                .createdAt(userHistory.getCreatedAt())
                .build();
    }

    /**
     * Convert UserHistoryDTO to UserHistory entity
     * @param userHistoryDTO the DTO
     * @return the entity
     */
    private UserHistory convertDtoToEntity(UserHistoryDTO userHistoryDTO) {
        return UserHistory.builder()
                .id(userHistoryDTO.getId())
                .userId(userHistoryDTO.getUserId())
                .username(userHistoryDTO.getUsername())
                .email(userHistoryDTO.getEmail())
                .operationType(userHistoryDTO.getOperationType())
                .operationDescription(userHistoryDTO.getOperationDescription())
                .affectedEntity(userHistoryDTO.getAffectedEntity())
                .affectedEntityId(userHistoryDTO.getAffectedEntityId())
                .httpMethod(userHistoryDTO.getHttpMethod())
                .apiEndpoint(userHistoryDTO.getApiEndpoint())
                .ipAddress(userHistoryDTO.getIpAddress())
                .operationStatus(userHistoryDTO.getOperationStatus())
                .errorMessage(userHistoryDTO.getErrorMessage())
                .metadata(userHistoryDTO.getMetadata())
                .build();
    }
}

