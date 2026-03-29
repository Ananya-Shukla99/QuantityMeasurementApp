package com.app.quantitymeasurement.enums;

public enum OperationType {
    ADD,
    SUBTRACT,
    MULTIPLY,
    DIVIDE,
    COMPARE,
    CONVERT;

    // Convert String to OperationType safely
    public static OperationType fromString(String operation) {
        for (OperationType type : OperationType.values()) {
            if (type.name().equalsIgnoreCase(operation)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown operation type: " + operation);
    }
}
