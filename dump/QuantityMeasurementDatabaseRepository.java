package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.exception.DatabaseException;
import com.app.quantitymeasurement.util.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * QuantityMeasurementDatabaseRepository implements IQuantityMeasurementRepository
 * using JDBC and H2 database for persistent storage of quantity measurement operations.
 */
public class QuantityMeasurementDatabaseRepository
        implements IQuantityMeasurementRepository {

    private static final Logger logger = Logger.getLogger(
            QuantityMeasurementDatabaseRepository.class.getName()
    );

    // ── Singleton ─────────────────────────────────────────────────────────────
    private static QuantityMeasurementDatabaseRepository instance;

    // ── Connection Pool ───────────────────────────────────────────────────────
    private final ConnectionPool connectionPool;

    // ── SQL Queries ───────────────────────────────────────────────────────────
    private static final String INSERT_SQL =
            "INSERT INTO quantity_measurement_entity " +
            "(this_value, this_unit, this_measurement_type, " +
            " that_value, that_unit, that_measurement_type, " +
            " operation, " +
            " result_value, result_unit, result_measurement_type, " +
            " result_string, is_error, error_message) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SELECT_ALL_SQL =
            "SELECT * FROM quantity_measurement_entity " +
            "ORDER BY created_at DESC";

    private static final String SELECT_BY_OPERATION_SQL =
            "SELECT * FROM quantity_measurement_entity " +
            "WHERE operation = ? " +
            "ORDER BY created_at DESC";

    private static final String SELECT_BY_MEASUREMENT_TYPE_SQL =
            "SELECT * FROM quantity_measurement_entity " +
            "WHERE this_measurement_type = ? " +
            "ORDER BY created_at DESC";

    private static final String COUNT_SQL =
            "SELECT COUNT(*) FROM quantity_measurement_entity";

    private static final String DELETE_ALL_SQL =
            "DELETE FROM quantity_measurement_entity";

    // ── Private Constructor ───────────────────────────────────────────────────
    private QuantityMeasurementDatabaseRepository() {
        this.connectionPool = ConnectionPool.getInstance();
        initializeSchema();
        logger.info("QuantityMeasurementDatabaseRepository initialized.");
    }

    // ── getInstance ───────────────────────────────────────────────────────────
    public static QuantityMeasurementDatabaseRepository getInstance() {
        if (instance == null) {
            instance = new QuantityMeasurementDatabaseRepository();
        }
        return instance;
    }

    // ── Initialize Schema ─────────────────────────────────────────────────────

    /**
     * Creates tables automatically if they don't exist.
     */
    private void initializeSchema() {
        String createEntityTable =
            "CREATE TABLE IF NOT EXISTS quantity_measurement_entity (" +
            "  id                      BIGINT AUTO_INCREMENT PRIMARY KEY," +
            "  this_value              DOUBLE       NOT NULL," +
            "  this_unit               VARCHAR(50)  NOT NULL," +
            "  this_measurement_type   VARCHAR(50)  NOT NULL," +
            "  that_value              DOUBLE," +
            "  that_unit               VARCHAR(50)," +
            "  that_measurement_type   VARCHAR(50)," +
            "  operation               VARCHAR(20)  NOT NULL," +
            "  result_value            DOUBLE," +
            "  result_unit             VARCHAR(50)," +
            "  result_measurement_type VARCHAR(50)," +
            "  result_string           VARCHAR(255)," +
            "  is_error                BOOLEAN      DEFAULT FALSE," +
            "  error_message           VARCHAR(500)," +
            "  created_at              TIMESTAMP    DEFAULT CURRENT_TIMESTAMP" +
            ")";

        String createHistoryTable =
            "CREATE TABLE IF NOT EXISTS quantity_measurement_history (" +
            "  id              BIGINT AUTO_INCREMENT PRIMARY KEY," +
            "  entity_id       BIGINT NOT NULL," +
            "  operation_count INT    DEFAULT 1," +
            "  created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "  FOREIGN KEY (entity_id) " +
            "      REFERENCES quantity_measurement_entity(id) " +
            "      ON DELETE CASCADE" +
            ")";

        Connection conn = null;
        try {
            conn = connectionPool.getConnection();

            try (PreparedStatement ps1 =
                         conn.prepareStatement(createEntityTable)) {
                ps1.execute();
            }

            try (PreparedStatement ps2 =
                         conn.prepareStatement(createHistoryTable)) {
                ps2.execute();
            }

            logger.info("[DB] Schema initialized successfully.");

        } catch (SQLException e) {
            logger.severe("[DB] Schema initialization failed: "
                    + e.getMessage());
            throw new DatabaseException(
                    "Schema initialization failed",
                    e,
                    DatabaseException.SCHEMA_ERROR
            );
        } finally {
            connectionPool.releaseConnection(conn);
        }
    }

    // ── SAVE ──────────────────────────────────────────────────────────────────

    /**
     * Saves a QuantityMeasurementEntity to the database.
     */
    @Override
    public void save(QuantityMeasurementEntity entity) {

        if (entity == null)
            throw new IllegalArgumentException("Entity cannot be null");

        Connection conn = null;

        try {
            conn = connectionPool.getConnection();
            conn.setAutoCommit(false); // ← begin transaction

            try (PreparedStatement ps =
                         conn.prepareStatement(INSERT_SQL)) {

                // First Operand
                ps.setDouble(1, entity.thisValue);
                ps.setString(2, entity.thisUnit);
                ps.setString(3, entity.thisMeasurementType);

                // Second Operand
                ps.setObject(4, entity.thatValue);
                ps.setString(5, entity.thatUnit);
                ps.setString(6, entity.thatMeasurementType);

                // Operation
                ps.setString(7, entity.operation);

                // Result
                ps.setObject(8, entity.resultValue);
                ps.setString(9, entity.resultUnit);
                ps.setString(10, entity.resultMeasurementType);
                ps.setString(11, entity.resultString);

                // Error Info
                ps.setBoolean(12, entity.isError);
                ps.setString(13, entity.errorMessage);

                ps.executeUpdate();
            }

            conn.commit(); // ← commit transaction
            logger.info("[DB] Saved | operation: " + entity.operation);

        } catch (SQLException e) {
            // Rollback on failure
            rollback(conn);
            logger.severe("[DB] Save failed: " + e.getMessage());
            throw new DatabaseException(
                    "Failed to save entity",
                    e,
                    DatabaseException.SAVE_ERROR
            );
        } finally {
            resetAutoCommit(conn);
            connectionPool.releaseConnection(conn);
        }
    }

    // ── GET ALL ───────────────────────────────────────────────────────────────

    /**
     * Retrieves all measurements from the database.
     */
    @Override
    public List<QuantityMeasurementEntity> getAllMeasurements() {

        List<QuantityMeasurementEntity> results = new ArrayList<>();
        Connection conn = null;

        try {
            conn = connectionPool.getConnection();

            try (PreparedStatement ps =
                         conn.prepareStatement(SELECT_ALL_SQL);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    results.add(mapRow(rs));
                }
            }

            logger.info("[DB] Fetched all | count: " + results.size());

        } catch (SQLException e) {
            logger.severe("[DB] Fetch all failed: " + e.getMessage());
            throw new DatabaseException(
                    "Failed to fetch all measurements",
                    e,
                    DatabaseException.FETCH_ERROR
            );
        } finally {
            connectionPool.releaseConnection(conn);
        }

        return results;
    }

    // ── GET BY OPERATION ──────────────────────────────────────────────────────

    /**
     * Retrieves measurements filtered by operation type.
     * e.g. findByOperation("ADD") → all ADD records
     */
    @Override
    public List<QuantityMeasurementEntity> findByOperation(String operation) {

        if (operation == null || operation.isEmpty())
            throw new IllegalArgumentException("Operation cannot be null");

        List<QuantityMeasurementEntity> results = new ArrayList<>();
        Connection conn = null;

        try {
            conn = connectionPool.getConnection();

            try (PreparedStatement ps =
                         conn.prepareStatement(SELECT_BY_OPERATION_SQL)) {

                ps.setString(1, operation.toUpperCase());

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        results.add(mapRow(rs));
                    }
                }
            }

            logger.info("[DB] Fetched by operation: " + operation
                    + " | count: " + results.size());

        } catch (SQLException e) {
            logger.severe("[DB] Fetch by operation failed: " + e.getMessage());
            throw new DatabaseException(
                    "Failed to fetch by operation: " + operation,
                    e,
                    DatabaseException.FETCH_ERROR
            );
        } finally {
            connectionPool.releaseConnection(conn);
        }

        return results;
    }

    // ── GET BY MEASUREMENT TYPE ───────────────────────────────────────────────

    /**
     * Retrieves measurements filtered by measurement type.
     * e.g. findByMeasurementType("LengthUnit") → all length records
     */
    @Override
    public List<QuantityMeasurementEntity> findByMeasurementType(
            String measurementType) {

        if (measurementType == null || measurementType.isEmpty())
            throw new IllegalArgumentException(
                    "Measurement type cannot be null"
            );

        List<QuantityMeasurementEntity> results = new ArrayList<>();
        Connection conn = null;

        try {
            conn = connectionPool.getConnection();

            try (PreparedStatement ps =
                         conn.prepareStatement(
                                 SELECT_BY_MEASUREMENT_TYPE_SQL)) {

                ps.setString(1, measurementType);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        results.add(mapRow(rs));
                    }
                }
            }

            logger.info("[DB] Fetched by type: " + measurementType
                    + " | count: " + results.size());

        } catch (SQLException e) {
            logger.severe("[DB] Fetch by type failed: " + e.getMessage());
            throw new DatabaseException(
                    "Failed to fetch by measurement type: " + measurementType,
                    e,
                    DatabaseException.FETCH_ERROR
            );
        } finally {
            connectionPool.releaseConnection(conn);
        }

        return results;
    }

    // ── COUNT ─────────────────────────────────────────────────────────────────

    /**
     * Returns total count of measurements in the database.
     */
    @Override
    public int getCount() {

        Connection conn = null;

        try {
            conn = connectionPool.getConnection();

            try (PreparedStatement ps =
                         conn.prepareStatement(COUNT_SQL);
                 ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    int count = rs.getInt(1);
                    logger.info("[DB] Total count: " + count);
                    return count;
                }

            }

        } catch (SQLException e) {
            logger.severe("[DB] Count failed: " + e.getMessage());
            throw new DatabaseException(
                    "Failed to get count",
                    e,
                    DatabaseException.FETCH_ERROR
            );
        } finally {
            connectionPool.releaseConnection(conn);
        }

        return 0;
    }

    // ── DELETE ALL ────────────────────────────────────────────────────────────

    /**
     * Deletes all measurements from the database.
     * Useful for testing and resetting application state.
     */
    @Override
    public void deleteAll() {

        Connection conn = null;

        try {
            conn = connectionPool.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement ps =
                         conn.prepareStatement(DELETE_ALL_SQL)) {
                int deleted = ps.executeUpdate();
                conn.commit();
                logger.info("[DB] Deleted all | count: " + deleted);
            }

        } catch (SQLException e) {
            rollback(conn);
            logger.severe("[DB] Delete all failed: " + e.getMessage());
            throw new DatabaseException(
                    "Failed to delete all measurements",
                    e,
                    DatabaseException.DELETE_ERROR
            );
        } finally {
            resetAutoCommit(conn);
            connectionPool.releaseConnection(conn);
        }
    }

    // ── POOL STATISTICS ───────────────────────────────────────────────────────

    /**
     * Returns connection pool statistics.
     */
    @Override
    public String getPoolStatistics() {
        return connectionPool.getPoolStatistics();
    }

    // ── RELEASE RESOURCES ─────────────────────────────────────────────────────

    /**
     * Shuts down connection pool and releases all resources.
     */
    @Override
    public void releaseResources() {
        connectionPool.shutdown();
        logger.info("[DB] Resources released. Connection pool shutdown.");
    }

    // ── PRIVATE HELPERS ───────────────────────────────────────────────────────

    /**
     * Maps a ResultSet row to a QuantityMeasurementEntity.
     */
    private QuantityMeasurementEntity mapRow(ResultSet rs) throws SQLException {

        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();

        entity.thisValue           = rs.getDouble("this_value");
        entity.thisUnit            = rs.getString("this_unit");
        entity.thisMeasurementType = rs.getString("this_measurement_type");

        entity.thatValue           = rs.getDouble("that_value");
        entity.thatUnit            = rs.getString("that_unit");
        entity.thatMeasurementType = rs.getString("that_measurement_type");

        entity.operation           = rs.getString("operation");

        entity.resultValue         = rs.getDouble("result_value");
        entity.resultUnit          = rs.getString("result_unit");
        entity.resultMeasurementType = rs.getString("result_measurement_type");

        entity.resultString        = rs.getString("result_string");

        entity.isError             = rs.getBoolean("is_error");
        entity.errorMessage        = rs.getString("error_message");

        return entity;
    }

    /**
     * Rolls back a transaction safely.
     */
    private void rollback(Connection conn) {
        try {
            if (conn != null) {
                conn.rollback();
                logger.warning("[DB] Transaction rolled back.");
            }
        } catch (SQLException e) {
            logger.severe("[DB] Rollback failed: " + e.getMessage());
        }
    }

    /**
     * Resets auto-commit to true after transaction.
     */
    private void resetAutoCommit(Connection conn) {
        try {
            if (conn != null) {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            logger.warning("[DB] Reset auto-commit failed: " + e.getMessage());
        }
    }
}