package com.app.qma.service;

import com.app.qma.dto.OperationHistoryDTO;
import com.app.qma.model.OperationHistory;
import com.app.qma.repository.OperationHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing measurement operation history
 * Provides business logic for tracking, retrieving, and analyzing measurement operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OperationHistoryService {

    // Repository for operation history database operations
    private final OperationHistoryRepository operationHistoryRepository;

    /**
     * Record a measurement operation in the history
     * @param operationHistoryDTO the operation history data
     * @return the saved operation history record
     */
    public OperationHistoryDTO recordOperation(OperationHistoryDTO operationHistoryDTO) {
        log.info("Recording operation: {} for user: {}", 
                operationHistoryDTO.getOperationName(), operationHistoryDTO.getUserId());
        
        OperationHistory operationHistory = convertDtoToEntity(operationHistoryDTO);
        OperationHistory saved = operationHistoryRepository.save(operationHistory);
        
        log.info("Operation recorded successfully with ID: {}", saved.getId());
        return convertEntityToDto(saved);
    }

    /**
     * Retrieve all operations performed by a specific user
     * @param userId the user ID
     * @return list of operation history DTOs
     */
    public List<OperationHistoryDTO> getUserOperations(String userId) {
        log.info("Fetching operations for user: {}", userId);
        return operationHistoryRepository.findByUserId(userId)
                .stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve all operations of a specific type
     * @param operationName the name of the operation
     * @return list of operation history DTOs
     */
    public List<OperationHistoryDTO> getOperationsByName(String operationName) {
        log.info("Fetching operations of type: {}", operationName);
        return operationHistoryRepository.findByOperationName(operationName)
                .stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve all operations for a specific measurement type
     * @param measurementType the type of measurement
     * @return list of operation history DTOs
     */
    public List<OperationHistoryDTO> getOperationsByMeasurementType(String measurementType) {
        log.info("Fetching operations for measurement type: {}", measurementType);
        return operationHistoryRepository.findByMeasurementType(measurementType)
                .stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve all successful operations
     * @return list of successful operation DTOs
     */
    public List<OperationHistoryDTO> getSuccessfulOperations() {
        log.info("Fetching all successful operations");
        return operationHistoryRepository.findByOperationStatus("SUCCESS")
                .stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve all failed operations
     * @return list of failed operation DTOs
     */
    public List<OperationHistoryDTO> getFailedOperations() {
        log.info("Fetching all failed operations");
        return operationHistoryRepository.findFailedOperations()
                .stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve user operations within a date range
     * @param userId the user ID
     * @param startDate the start date
     * @param endDate the end date
     * @return list of operation history DTOs within the date range
     */
    public List<OperationHistoryDTO> getUserOperationsByDateRange(String userId, LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Fetching operations for user: {} between {} and {}", userId, startDate, endDate);
        return operationHistoryRepository.findUserOperationsByDateRange(userId, startDate, endDate)
                .stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve all operations ordered by creation date (most recent first)
     * @return list of operation history DTOs
     */
    public List<OperationHistoryDTO> getAllOperationsOrderedByDate() {
        log.info("Fetching all operations ordered by date");
        return operationHistoryRepository.findAllOrderByCreatedAtDesc()
                .stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve operation history for a specific measurement entity
     * @param measurementEntityId the measurement entity ID
     * @return list of operation history DTOs
     */
    public List<OperationHistoryDTO> getOperationsByMeasurementEntity(Long measurementEntityId) {
        log.info("Fetching operations for measurement entity: {}", measurementEntityId);
        return operationHistoryRepository.findByMeasurementEntityId(measurementEntityId)
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
        return operationHistoryRepository.countByUserId(userId);
    }

    /**
     * Get count of operations by type
     * @param operationName the operation name
     * @return count of operations
     */
    public long getOperationTypeCount(String operationName) {
        log.info("Getting operation count for operation type: {}", operationName);
        return operationHistoryRepository.countByOperationName(operationName);
    }

    /**
     * Get average execution time for a specific operation type
     * @param operationName the operation name
     * @return average execution time in milliseconds
     */
    public Double getAverageExecutionTime(String operationName) {
        log.info("Getting average execution time for operation: {}", operationName);
        return operationHistoryRepository.getAverageExecutionTime(operationName);
    }

    /**
     * Delete operation history record by ID (for administration purposes)
     * @param id the operation history record ID
     */
    public void deleteOperationRecord(Long id) {
        log.info("Deleting operation history record with ID: {}", id);
        operationHistoryRepository.deleteById(id);
    }

    /**
     * Convert OperationHistory entity to OperationHistoryDTO
     * @param operationHistory the entity
     * @return the DTO
     */
    private OperationHistoryDTO convertEntityToDto(OperationHistory operationHistory) {
        return OperationHistoryDTO.builder()
                .id(operationHistory.getId())
                .userId(operationHistory.getUserId())
                .operationName(operationHistory.getOperationName())
                .measurementType(operationHistory.getMeasurementType())
                .inputValue1(operationHistory.getInputValue1())
                .inputUnit1(operationHistory.getInputUnit1())
                .inputValue2(operationHistory.getInputValue2())
                .inputUnit2(operationHistory.getInputUnit2())
                .resultValue(operationHistory.getResultValue())
                .resultUnit(operationHistory.getResultUnit())
                .resultString(operationHistory.getResultString())
                .operationStatus(operationHistory.getOperationStatus())
                .errorMessage(operationHistory.getErrorMessage())
                .executionTimeMs(operationHistory.getExecutionTimeMs())
                .measurementEntityId(operationHistory.getMeasurementEntityId())
                .notes(operationHistory.getNotes())
                .createdAt(operationHistory.getCreatedAt())
                .build();
    }

    /**
     * Convert OperationHistoryDTO to OperationHistory entity
     * @param operationHistoryDTO the DTO
     * @return the entity
     */
    private OperationHistory convertDtoToEntity(OperationHistoryDTO operationHistoryDTO) {
        return OperationHistory.builder()
                .id(operationHistoryDTO.getId())
                .userId(operationHistoryDTO.getUserId())
                .operationName(operationHistoryDTO.getOperationName())
                .measurementType(operationHistoryDTO.getMeasurementType())
                .inputValue1(operationHistoryDTO.getInputValue1())
                .inputUnit1(operationHistoryDTO.getInputUnit1())
                .inputValue2(operationHistoryDTO.getInputValue2())
                .inputUnit2(operationHistoryDTO.getInputUnit2())
                .resultValue(operationHistoryDTO.getResultValue())
                .resultUnit(operationHistoryDTO.getResultUnit())
                .resultString(operationHistoryDTO.getResultString())
                .operationStatus(operationHistoryDTO.getOperationStatus())
                .errorMessage(operationHistoryDTO.getErrorMessage())
                .executionTimeMs(operationHistoryDTO.getExecutionTimeMs())
                .measurementEntityId(operationHistoryDTO.getMeasurementEntityId())
                .notes(operationHistoryDTO.getNotes())
                .build();
    }
}

