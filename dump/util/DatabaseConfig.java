package com.app.quantitymeasurement.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * DatabaseConfig loads database configuration from application.properties.
 * Provides centralized access to all DB settings throughout the application.
 */
public class DatabaseConfig {

	private static final Logger logger = Logger.getLogger(DatabaseConfig.class.getName());

	// ── Singleton ─────────────────────────────────────────────────────────────
	private static DatabaseConfig instance;

	// ── Properties ────────────────────────────────────────────────────────────
	private final Properties properties;

	// ── Property Keys ─────────────────────────────────────────────────────────
	private static final String PROPS_FILE = "application.properties";
	private static final String KEY_DRIVER = "db.driver";
	private static final String KEY_URL = "db.url";
	private static final String KEY_USERNAME = "db.username";
	private static final String KEY_PASSWORD = "db.password";
	private static final String KEY_POOL_INIT = "db.pool.initialSize";
	private static final String KEY_POOL_MAX = "db.pool.maxSize";
	private static final String KEY_POOL_MIN_IDLE = "db.pool.minIdle";
	private static final String KEY_POOL_MAX_IDLE = "db.pool.maxIdle";
	private static final String KEY_CONN_TIMEOUT = "db.pool.connectionTimeout";
	private static final String KEY_IDLE_TIMEOUT = "db.pool.idleTimeout";
	private static final String KEY_REPO_TYPE = "app.repository.type";

	// ── Private Constructor ───────────────────────────────────────────────────
	private DatabaseConfig() {
		properties = new Properties();
		loadProperties();
		logger.info("DatabaseConfig initialized from: " + PROPS_FILE);
	}

	// ── getInstance ───────────────────────────────────────────────────────────
	public static DatabaseConfig getInstance() {
		if (instance == null) {
			instance = new DatabaseConfig();
		}
		return instance;
	}

	// ── Load Properties ───────────────────────────────────────────────────────
	private void loadProperties() {
	    // Strategy 1: Load from classpath root
	    InputStream input = getClass()
	            .getClassLoader()
	            .getResourceAsStream(PROPS_FILE);

	    // Strategy 2: Load from current thread classloader
	    if (input == null) {
	        input = Thread.currentThread()
	                .getContextClassLoader()
	                .getResourceAsStream(PROPS_FILE);
	    }

	    // Strategy 3: Load from class resource
	    if (input == null) {
	        input = getClass().getResourceAsStream("/" + PROPS_FILE);
	    }

	    // Strategy 4: Load directly from file system
	    if (input == null) {
	        try {
	            java.io.File file = new java.io.File(
	                    "src/main/resources/" + PROPS_FILE
	            );
	            if (file.exists()) {
	                input = new java.io.FileInputStream(file);
	                logger.info("Loaded properties from file system.");
	            }
	        } catch (java.io.FileNotFoundException e) {
	            logger.warning("File system load failed: " + e.getMessage());
	        }
	    }

	    // If still null — use defaults
	    if (input == null) {
	        logger.warning("application.properties not found!"
	                + " Using default values.");
	        loadDefaults();
	        return;
	    }

	    try {
	        properties.load(input);
	        input.close();
	        logger.info("Properties loaded successfully.");
	    } catch (IOException e) {
	        logger.severe("Failed to load properties: " + e.getMessage());
	        loadDefaults();
	    }
	}

	/**
	 * Loads default values when properties file is not found.
	 */
	private void loadDefaults() {
	    properties.setProperty("db.driver",
	            "org.h2.Driver");
	    properties.setProperty("db.url",
	            "jdbc:h2:./data/quantity_measurement;AUTO_SERVER=TRUE");
	    properties.setProperty("db.username",
	            "Ananya");
	    properties.setProperty("db.password",
	            "1234");
	    properties.setProperty("db.pool.initialSize",  "5");
	    properties.setProperty("db.pool.maxSize",      "10");
	    properties.setProperty("db.pool.minIdle",      "2");
	    properties.setProperty("db.pool.maxIdle",      "5");
	    properties.setProperty("db.pool.connectionTimeout", "30000");
	    properties.setProperty("db.pool.idleTimeout",  "600000");
	    properties.setProperty("app.repository.type",  "database");

	    logger.info("Default properties loaded successfully.");
	}
	// ── Getters ───────────────────────────────────────────────────────────────

	public String getDriver() {
		return properties.getProperty(KEY_DRIVER, "org.h2.Driver");
	}

	public String getUrl() {
		return properties.getProperty(KEY_URL, "jdbc:h2:./data/quantity_measurement;AUTO_SERVER=TRUE");
	}

	public String getUsername() {
		return properties.getProperty(KEY_USERNAME, "sa");
	}

	public String getPassword() {
		return properties.getProperty(KEY_PASSWORD, "");
	}

	public int getPoolInitialSize() {
		return Integer.parseInt(properties.getProperty(KEY_POOL_INIT, "5"));
	}

	public int getPoolMaxSize() {
		return Integer.parseInt(properties.getProperty(KEY_POOL_MAX, "10"));
	}

	public int getPoolMinIdle() {
		return Integer.parseInt(properties.getProperty(KEY_POOL_MIN_IDLE, "2"));
	}

	public int getPoolMaxIdle() {
		return Integer.parseInt(properties.getProperty(KEY_POOL_MAX_IDLE, "5"));
	}

	public long getConnectionTimeout() {
		return Long.parseLong(properties.getProperty(KEY_CONN_TIMEOUT, "30000"));
	}

	public long getIdleTimeout() {
		return Long.parseLong(properties.getProperty(KEY_IDLE_TIMEOUT, "600000"));
	}

	public String getRepositoryType() {
		return properties.getProperty(KEY_REPO_TYPE, "database");
	}

	public boolean isDatabaseRepository() {
		return "database".equalsIgnoreCase(getRepositoryType());
	}

	// ── toString ──────────────────────────────────────────────────────────────
	@Override
	public String toString() {
		return "DatabaseConfig {" + "\n  driver      = " + getDriver() + "\n  url         = " + getUrl()
				+ "\n  username    = " + getUsername() + "\n  poolInitial = " + getPoolInitialSize()
				+ "\n  poolMax     = " + getPoolMaxSize() + "\n  repoType    = " + getRepositoryType() + "\n}";
	}
}