package com.app.quantitymeasurement.util;

import com.app.quantitymeasurement.exception.DatabaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * ConnectionPool manages a pool of reusable JDBC database connections.
 * Reduces overhead of creating and closing connections on every operation.
 */
public class ConnectionPool {

    private static final Logger logger = Logger.getLogger(
            ConnectionPool.class.getName()
    );

    // ── Singleton ─────────────────────────────────────────────────────────────
    private static ConnectionPool instance;

    // ── Configuration ─────────────────────────────────────────────────────────
    private final DatabaseConfig config;

    // ── Pool Storage ──────────────────────────────────────────────────────────
    private final List<Connection> availableConnections;
    private final List<Connection> usedConnections;

    // ── Pool Settings ─────────────────────────────────────────────────────────
    private final int maxPoolSize;
    private final int initialPoolSize;

    // ── Private Constructor ───────────────────────────────────────────────────
    private ConnectionPool() {
        this.config               = DatabaseConfig.getInstance();
        this.maxPoolSize          = config.getPoolMaxSize();
        this.initialPoolSize      = config.getPoolInitialSize();
        this.availableConnections = new ArrayList<>();
        this.usedConnections      = new ArrayList<>();

        initializePool();

        logger.info("ConnectionPool created | initial: " + initialPoolSize
                + " | max: " + maxPoolSize);
    }

    // ── getInstance ───────────────────────────────────────────────────────────
    public static ConnectionPool getInstance() {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }

    // ── Initialize Pool ───────────────────────────────────────────────────────

    /**
     * Creates initial connections on startup and loads the JDBC driver.
     */
    private void initializePool() {
        try {
            // Load JDBC driver
            Class.forName(config.getDriver());
            logger.info("JDBC Driver loaded: " + config.getDriver());

            // Create initial connections
            for (int i = 0; i < initialPoolSize; i++) {
                availableConnections.add(createNewConnection());
            }

            logger.info("Connection pool initialized with "
                    + initialPoolSize + " connections.");

        } catch (ClassNotFoundException e) {
            logger.severe("JDBC Driver not found: " + e.getMessage());
            throw new DatabaseException(
                    "JDBC Driver not found: " + config.getDriver(), e
            );
        }
    }

    // ── Create Connection ─────────────────────────────────────────────────────

    /**
     * Creates a single new JDBC connection using config properties.
     */
    private Connection createNewConnection() {
        try {
            Connection conn = DriverManager.getConnection(
                    config.getUrl(),
                    config.getUsername(),
                    config.getPassword()
            );
            logger.fine("New JDBC connection created.");
            return conn;

        } catch (SQLException e) {
            logger.severe("Failed to create connection: " + e.getMessage());
            throw new DatabaseException(
                    "Failed to create database connection", e
            );
        }
    }

    // ── Get Connection ────────────────────────────────────────────────────────

    /**
     * Acquires a connection from the pool.
     * Creates a new one if pool has capacity.
     * Throws DatabaseException if pool is exhausted.
     */
    public synchronized Connection getConnection() {

        if (availableConnections.isEmpty()) {

            if (usedConnections.size() < maxPoolSize) {
                // Pool not at max — create new connection
                availableConnections.add(createNewConnection());
                logger.info("Pool expanded | used: "
                        + usedConnections.size()
                        + " | max: " + maxPoolSize);
            } else {
                // Pool exhausted — cannot create more
                logger.severe("Connection pool exhausted! max: " + maxPoolSize);
                throw new DatabaseException(
                        "Connection pool exhausted. Max pool size: " + maxPoolSize
                );
            }
        }

        // Move from available → used
        Connection conn = availableConnections
                .remove(availableConnections.size() - 1);
        usedConnections.add(conn);

        logger.fine("Connection acquired | available: "
                + availableConnections.size()
                + " | used: " + usedConnections.size());

        return conn;
    }

    // ── Release Connection ────────────────────────────────────────────────────

    /**
     * Returns a connection back to the available pool.
     */
    public synchronized void releaseConnection(Connection conn) {
        if (conn != null) {
            usedConnections.remove(conn);
            availableConnections.add(conn);

            logger.fine("Connection released | available: "
                    + availableConnections.size()
                    + " | used: " + usedConnections.size());
        }
    }

    // ── Shutdown ──────────────────────────────────────────────────────────────

    /**
     * Closes all connections and shuts down the pool completely.
     */
    public synchronized void shutdown() {
        closeAll(availableConnections);
        closeAll(usedConnections);
        logger.info("ConnectionPool shutdown. All connections closed.");
    }

    /**
     * Closes all connections in a given list.
     */
    private void closeAll(List<Connection> connections) {
        for (Connection conn : connections) {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                logger.warning("Error closing connection: " + e.getMessage());
            }
        }
        connections.clear();
    }

    // ── Pool Statistics ───────────────────────────────────────────────────────

    /**
     * Returns pool statistics as formatted string.
     */
    public synchronized String getPoolStatistics() {
        return "ConnectionPool Stats {" +
                " available: " + availableConnections.size() +
                " | used: "      + usedConnections.size() +
                " | total: "     + getTotalConnections() +
                " | max: "       + maxPoolSize +
                " }";
    }

    public synchronized int getTotalConnections() {
        return availableConnections.size() + usedConnections.size();
    }

    public synchronized int getAvailableConnectionCount() {
        return availableConnections.size();
    }

    public synchronized int getUsedConnectionCount() {
        return usedConnections.size();
    }
}