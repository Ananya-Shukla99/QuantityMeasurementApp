package com.app.qma.repository;

import com.app.qma.model.OperationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for OperationHistory entity
 * Handles all database operations related to measurement operation tracking
 */
@Repository
public interface OperationHistoryRepository extends JpaRepository<OperationHistory, Long> {

    /**
     * Find all operations performed by a specific user
     * @param userId the user ID
     * @return list of operation history records
     */
    List<OperationHistory> findByUserId(String userId);

    /**
     * Find all operations of a specific type
     * @param operationName the name of the operation
     * @return list of operation history records
     */
    List<OperationHistory> findByOperationName(String operationName);

    /**
     * Find all operations for a specific measurement type
     * @param measurementType the type of measurement
     * @return list of operation history records
     */
    List<OperationHistory> findByMeasurementType(String measurementType);

    /**
     * Find all successful operations
     * @return list of successful operation records
     */
    List<OperationHistory> findByOperationStatus(String operationStatus);

    /**
     * Find operations performed by a user within a date range
     * @param userId the user ID
     * @param startDate the start date
     * @param endDate the end date
     * @return list of operation history records within the date range
     */
    @Query("SELECT oh FROM OperationHistory oh WHERE oh.userId = :userId AND oh.createdAt BETWEEN :startDate AND :endDate ORDER BY oh.createdAt DESC")
    List<OperationHistory> findUserOperationsByDateRange(
            @Param("userId") String userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    /**
     * Find all operations ordered by creation date (most recent first)
     * @return list of operation history records
     */
    @Query("SELECT oh FROM OperationHistory oh ORDER BY oh.createdAt DESC")
    List<OperationHistory> findAllOrderByCreatedAtDesc();

    /**
     * Find operation history by measurement entity ID
     * @param measurementEntityId the measurement entity ID
     * @return list of operation history records
     */
    List<OperationHistory> findByMeasurementEntityId(Long measurementEntityId);

    /**
     * Count operations performed by a user
     * @param userId the user ID
     * @return count of operations
     */
    long countByUserId(String userId);

    /**
     * Count operations by type
     * @param operationName the operation name
     * @return count of operations
     */
    long countByOperationName(String operationName);

    /**
     * Find failed operations
     * @return list of failed operation records
     */
    @Query("SELECT oh FROM OperationHistory oh WHERE oh.operationStatus = 'FAILURE' ORDER BY oh.createdAt DESC")
    List<OperationHistory> findFailedOperations();

    /**
     * Get average execution time for a specific operation type
     * @param operationName the operation name
     * @return average execution time in milliseconds
     */
    @Query("SELECT AVG(oh.executionTimeMs) FROM OperationHistory oh WHERE oh.operationName = :operationName")
    Double getAverageExecutionTime(@Param("operationName") String operationName);
}

