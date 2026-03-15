-- =============================================================================
-- Quantity Measurement App — Production Schema
-- UC16 | H2 Database
-- =============================================================================

-- Clean start
DROP TABLE IF EXISTS quantity_measurement_history;
DROP TABLE IF EXISTS quantity_measurement_entity;

-- =============================================================================
-- Main Table
-- =============================================================================
CREATE TABLE IF NOT EXISTS quantity_measurement_entity (

    id                       BIGINT AUTO_INCREMENT PRIMARY KEY,

    -- First Operand
    this_value               DOUBLE       NOT NULL,
    this_unit                VARCHAR(50)  NOT NULL,
    this_measurement_type    VARCHAR(50)  NOT NULL,

    -- Second Operand (nullable for CONVERT)
    that_value               DOUBLE,
    that_unit                VARCHAR(50),
    that_measurement_type    VARCHAR(50),

    -- Operation: ADD, SUBTRACT, DIVIDE, CONVERT, COMPARE
    operation                VARCHAR(20)  NOT NULL,

    -- Numeric Result
    result_value             DOUBLE,
    result_unit              VARCHAR(50),
    result_measurement_type  VARCHAR(50),

    -- String Result (COMPARE → "Equal" / "Not Equal")
    result_string            VARCHAR(255),

    -- Error Info
    is_error                 BOOLEAN      DEFAULT FALSE,
    error_message            VARCHAR(500),

    -- Timestamps
    created_at               TIMESTAMP    DEFAULT CURRENT_TIMESTAMP

);

-- =============================================================================
-- History Table — audit trail
-- =============================================================================
CREATE TABLE IF NOT EXISTS quantity_measurement_history (

    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    entity_id        BIGINT  NOT NULL,
    operation_count  INT     DEFAULT 1,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (entity_id)
        REFERENCES quantity_measurement_entity(id)
            ON DELETE CASCADE
);

-- =============================================================================
-- Indexes
-- =============================================================================
CREATE INDEX IF NOT EXISTS idx_operation
    ON quantity_measurement_entity(operation);

CREATE INDEX IF NOT EXISTS idx_measurement_type
    ON quantity_measurement_entity(this_measurement_type);

CREATE INDEX IF NOT EXISTS idx_created_at
    ON quantity_measurement_entity(created_at);