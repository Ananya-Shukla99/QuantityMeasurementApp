package com.app.qma.repository;

import com.app.qma.model.UserHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for UserHistory entity
 * Handles all database operations related to user activity tracking
 */
@Repository
public interface UserHistoryRepository extends JpaRepository<UserHistory, Long> {

    /**
     * Find all history records for a specific user
     * @param userId the user ID
     * @return list of user history records
     */
    List<UserHistory> findByUserId(String userId);

    /**
     * Find all history records for a specific operation type
     * @param operationType the type of operation
     * @return list of history records
     */
    List<UserHistory> findByOperationType(String operationType);

    /**
     * Find all operations performed by a user within a date range
     * @param userId the user ID
     * @param startDate the start date
     * @param endDate the end date
     * @return list of history records within the date range
     */
    @Query("SELECT uh FROM UserHistory uh WHERE uh.userId = :userId AND uh.createdAt BETWEEN :startDate AND :endDate ORDER BY uh.createdAt DESC")
    List<UserHistory> findUserOperationsByDateRange(
            @Param("userId") String userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    /**
     * Find all failed operations
     * @return list of failed history records
     */
    List<UserHistory> findByOperationStatus(String operationStatus);

    /**
     * Find all operations performed on a specific entity
     * @param affectedEntity the affected entity name
     * @return list of history records
     */
    List<UserHistory> findByAffectedEntity(String affectedEntity);

    /**
     * Find all history records ordered by creation date (most recent first)
     * @return list of history records
     */
    @Query("SELECT uh FROM UserHistory uh ORDER BY uh.createdAt DESC")
    List<UserHistory> findAllOrderByCreatedAtDesc();

    /**
     * Count operations performed by a user
     * @param userId the user ID
     * @return count of operations
     */
    long countByUserId(String userId);

    /**
     * Find history records by username
     * @param username the username
     * @return list of history records
     */
    List<UserHistory> findByUsername(String username);
}

