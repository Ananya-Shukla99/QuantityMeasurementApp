-- src/test/resources/schema.sql

-- =============================================================================
-- Quantity Measurement App — TEST Schema
-- UC16 | H2 In-Memory Database
-- Wiped and recreated before every test run
-- =============================================================================

-- Clean start
DROP TABLE IF EXISTS quantity_measurement_history;
DROP TABLE IF EXISTS quantity_measurement_entity;
DROP TABLE IF EXISTS users;

-- =============================================================================
-- Users Table
-- =============================================================================
CREATE TABLE IF NOT EXISTS users (

    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    email       VARCHAR(255) NOT NULL UNIQUE,
    name        VARCHAR(100) NOT NULL,
    password    VARCHAR(255) NOT NULL,
    role        VARCHAR(20)  NOT NULL DEFAULT 'ROLE_USER'

);

-- =============================================================================
-- Main Table
-- =============================================================================
CREATE TABLE IF NOT EXISTS quantity_measurement_entity (

    id                       BIGINT AUTO_INCREMENT PRIMARY KEY,

    -- First Operand
    this_value               DOUBLE       NOT NULL,
    this_unit                VARCHAR(50)  NOT NULL,
    this_measurement_type    VARCHAR(50)  NOT NULL,

    -- Second Operand
    that_value               DOUBLE,
    that_unit                VARCHAR(50),
    that_measurement_type    VARCHAR(50),

    -- Operation
    operation                VARCHAR(20)  NOT NULL,

    -- Numeric Result
    result_value             DOUBLE,
    result_unit              VARCHAR(50),
    result_measurement_type  VARCHAR(50),

    -- String Result
    result_string            VARCHAR(255),

    -- Error Info
    is_error                 BOOLEAN      DEFAULT FALSE,
    error_message            VARCHAR(500),

    -- Timestamps                          ← both columns now present
    created_at               TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at               TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP

);

-- =============================================================================
-- History Table
-- =============================================================================
CREATE TABLE IF NOT EXISTS quantity_measurement_history (

    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    entity_id        BIGINT    NOT NULL,
    operation_count  INT       DEFAULT 1,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (entity_id)
        REFERENCES quantity_measurement_entity(id)
            ON DELETE CASCADE

);