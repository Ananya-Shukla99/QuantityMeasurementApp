package com.apps.quantitymeasurement.entity;

import com.apps.quantitymeasurement.unit.IMeasurable;

public class QuantityMeasurementEntity {

	//private static final long serialVersionUID = 1L;

	// First Operand
	public double thisValue;
	public String thisUnit;
	public String thisMeasurementType;

	// Second Operand
	public double thatValue;
	public String thatUnit;
	public String thatMeasurementType;

	// Operation type: "COMPARE", "CONVERT", "ADD", "SUBTRACT", "DIVIDE"
	public String operation;

	// Result fields
	public double resultValue;
	public String resultUnit;
	public String resultMeasurementType;

	// For comparison results like "Equal" or "Not Equal"
	public String resultString;

	// Error handling
	// Flag to indicate if an error occurred during the operation
	public boolean isError;

	// For capturing any error messages during operations
	public String errorMessage;

	// PRIVATE BASE CONSTRUCTOR — shared initialisation for operands + operation

	private QuantityMeasurementEntity(QuantityModel<IMeasurable> thisQuantity, QuantityModel<IMeasurable> thatQuantity,
			String operation) {
		// First operand
		if (thisQuantity != null) {
			this.thisValue = thisQuantity.value;
			this.thisUnit = thisQuantity.unit.getUnitName();
			this.thisMeasurementType = thisQuantity.unit.getMeasurementType();
		}

		// Second operand
		if (thatQuantity != null) {
			this.thatValue = thatQuantity.value;
			this.thatUnit = thatQuantity.unit.getUnitName();
			this.thatMeasurementType = thatQuantity.unit.getMeasurementType();
		}

		this.operation = operation;
	}

	// PUBLIC CONSTRUCTORS

	public QuantityMeasurementEntity(QuantityModel<IMeasurable> thisQuantity, QuantityModel<IMeasurable> thatQuantity,
			String operation, String result) {
		this(thisQuantity, thatQuantity, operation);
		this.resultString = result;
	}

	public QuantityMeasurementEntity(QuantityModel<IMeasurable> thisQuantity, QuantityModel<IMeasurable> thatQuantity,
			String operation, QuantityModel<IMeasurable> result) {
		this(thisQuantity, thatQuantity, operation);
		this.resultValue = result.value;
		this.resultUnit = result.unit.getUnitName();
		this.resultMeasurementType = result.unit.getMeasurementType();
	}

	public QuantityMeasurementEntity(QuantityModel<IMeasurable> thisQuantity, QuantityModel<IMeasurable> thatQuantity,
			String operation, String errorMessage, boolean isError) {
		this(thisQuantity, thatQuantity, operation);
		this.errorMessage = errorMessage;
		this.isError = isError;
	}

	// toString — useful for logging and debugging

	@Override
	public String toString() {
		if (isError) {
			return "QuantityMeasurementEntity [operation=" + operation + ", ERROR: " + errorMessage + "]";
		}
		if (resultString != null) {
			return "QuantityMeasurementEntity [operation=" + operation + ", " + thisValue + " " + thisUnit + " vs "
					+ thatValue + " " + thatUnit + " → " + resultString + "]";
		}
		return "QuantityMeasurementEntity [operation=" + operation + ", " + thisValue + " " + thisUnit + " + "
				+ thatValue + " " + thatUnit + " → " + resultValue + " " + resultUnit + "]";
	}
}