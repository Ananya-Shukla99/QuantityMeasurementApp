
CREATE TABLE IF NOT EXISTS quantity_measurement (
    id                    BIGINT AUTO_INCREMENT PRIMARY KEY,
    operation             VARCHAR(20)    NOT NULL,

    -- First operand
    this_value            DOUBLE,
    this_unit             VARCHAR(50),
    this_measurement_type VARCHAR(50),

    -- Second operand (nullable for CONVERT)
    that_value            DOUBLE,
    that_unit             VARCHAR(50),
    that_measurement_type VARCHAR(50),

    -- Result: numeric (ADD, SUBTRACT, CONVERT, DIVIDE)
    result_value          DOUBLE,
    result_unit           VARCHAR(50),
    result_measurement_type VARCHAR(50),

    -- Result: string (COMPARE → "Equal" / "Not Equal", or scalar DIVIDE)
    result_string         VARCHAR(100),

    -- Error tracking
    is_error              BOOLEAN        DEFAULT FALSE,
    error_message         VARCHAR(255),

    created_at            TIMESTAMP      DEFAULT CURRENT_TIMESTAMP
);