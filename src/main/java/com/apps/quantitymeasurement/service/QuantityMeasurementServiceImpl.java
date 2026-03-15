package com.apps.quantitymeasurement.service;

import com.apps.quantitymeasurement.entity.QuantityDTO;
import com.apps.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.apps.quantitymeasurement.entity.QuantityModel;
import com.apps.quantitymeasurement.exception.QuantityMeasurementException;
import com.apps.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.apps.quantitymeasurement.unit.IMeasurable;
import com.apps.quantitymeasurement.unit.Quantity;

public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

	// Injected repository dependency — uses interface for loose coupling (DIP)
	private final IQuantityMeasurementRepository repository;

	/**
	 * Constructor injection of the repository dependency.
	 *
	 */
	public QuantityMeasurementServiceImpl(IQuantityMeasurementRepository repository) {
		this.repository = repository;
	}

	// CONVERT

	/**
	 * Converts a quantity from its current unit to the specified target unit.
	 */
	@Override

	public QuantityDTO convert(QuantityDTO quantityDTO, String targetUnit) {
		try {
			// Step 1 & 2: Extract QuantityModel from DTO
			QuantityModel<IMeasurable> model = toQuantityModel(quantityDTO);

			// Step 3: Resolve target unit instance
			IMeasurable targetUnitInstance = model.unit.getUnitInstance(targetUnit);

			// Step 4: Validate same measurement type
			validateSameType(model.unit, targetUnitInstance, "CONVERT");

			// Step 5: Perform conversion using core Quantity<U>
			Quantity<IMeasurable> quantity = new Quantity<>(model.value, model.unit);
			Quantity<IMeasurable> result = quantity.convertTo(targetUnitInstance);

			// Step 6: Build result model
			QuantityModel<IMeasurable> resultModel = new QuantityModel<>(result.getValue(), result.getUnit());

			// Step 7: Save entity to repository
			QuantityMeasurementEntity entity = new QuantityMeasurementEntity(model, null, "CONVERT", resultModel);
			repository.save(entity);

			// Step 8: Return QuantityDTO result
			return toQuantityDTO(resultModel);

		} catch (QuantityMeasurementException e) {
			throw e;
		} catch (Exception e) {
			throw new QuantityMeasurementException("Conversion failed: " + e.getMessage(), e);
		}
	}

	// COMPARE

	/**
	 * Compares two quantities for equality after converting to a common base unit.
	 */
	@Override

	public boolean compare(QuantityDTO quantityDTO1, QuantityDTO quantityDTO2) {
		try {
			// Step 1 & 2: Extract QuantityModels from DTOs
			QuantityModel<IMeasurable> model1 = toQuantityModel(quantityDTO1);
			QuantityModel<IMeasurable> model2 = toQuantityModel(quantityDTO2);

			// Step 3: Validate both quantities are of the same measurement type
			validateSameType(model1.unit, model2.unit, "COMPARE");

			// Step 4: Perform comparison using core Quantity<U>
			Quantity<IMeasurable> q1 = new Quantity<>(model1.value, model1.unit);
			Quantity<IMeasurable> q2 = new Quantity<>(model2.value, model2.unit);
			boolean result = q1.equals(q2);

			// Step 5: Save entity to repository
			QuantityMeasurementEntity entity = new QuantityMeasurementEntity(model1, model2, "COMPARE",
					result ? "Equal" : "Not Equal");
			repository.save(entity);

			return result;

		} catch (QuantityMeasurementException e) {
			throw e;
		} catch (Exception e) {
			throw new QuantityMeasurementException("Comparison failed: " + e.getMessage(), e);
		}
	}

	// ADD

	/**
	 * Adds two quantities and returns the result in the unit of the first operand.
	 * 
	 */
	@Override

	public QuantityDTO add(QuantityDTO quantityDTO1, QuantityDTO quantityDTO2) {
		try {
			QuantityModel<IMeasurable> model1 = toQuantityModel(quantityDTO1);
			QuantityModel<IMeasurable> model2 = toQuantityModel(quantityDTO2);

			validateSameType(model1.unit, model2.unit, "ADD");

			Quantity<IMeasurable> q1 = new Quantity<>(model1.value, model1.unit);
			Quantity<IMeasurable> q2 = new Quantity<>(model2.value, model2.unit);
			Quantity<IMeasurable> result = q1.add(q2);

			QuantityModel<IMeasurable> resultModel = new QuantityModel<>(result.getValue(), result.getUnit());

			QuantityMeasurementEntity entity = new QuantityMeasurementEntity(model1, model2, "ADD", resultModel);
			repository.save(entity);

			return toQuantityDTO(resultModel);

		} catch (QuantityMeasurementException e) {
			throw e;
		} catch (Exception e) {
			throw new QuantityMeasurementException("Addition failed: " + e.getMessage(), e);
		}
	}

	/**
	 * Adds two quantities and returns the result in the specified target unit.
	 * 
	 */
	@Override

	public QuantityDTO add(QuantityDTO quantityDTO1, QuantityDTO quantityDTO2, String targetUnit) {
		try {
			QuantityModel<IMeasurable> model1 = toQuantityModel(quantityDTO1);
			QuantityModel<IMeasurable> model2 = toQuantityModel(quantityDTO2);

			validateSameType(model1.unit, model2.unit, "ADD");

			IMeasurable targetUnitInstance = model1.unit.getUnitInstance(targetUnit);

			Quantity<IMeasurable> q1 = new Quantity<>(model1.value, model1.unit);
			Quantity<IMeasurable> q2 = new Quantity<>(model2.value, model2.unit);
			Quantity<IMeasurable> result = q1.add(q2, targetUnitInstance);

			QuantityModel<IMeasurable> resultModel = new QuantityModel<>(result.getValue(), result.getUnit());

			QuantityMeasurementEntity entity = new QuantityMeasurementEntity(model1, model2, "ADD", resultModel);
			repository.save(entity);

			return toQuantityDTO(resultModel);

		} catch (QuantityMeasurementException e) {
			throw e;
		} catch (Exception e) {
			throw new QuantityMeasurementException("Addition failed: " + e.getMessage(), e);
		}
	}

	// SUBTRACT

	/**
	 * Subtracts the second quantity from the first and returns the result in the
	 * 
	 */
	@Override

	public QuantityDTO subtract(QuantityDTO quantityDTO1, QuantityDTO quantityDTO2) {
		try {
			QuantityModel<IMeasurable> model1 = toQuantityModel(quantityDTO1);
			QuantityModel<IMeasurable> model2 = toQuantityModel(quantityDTO2);

			validateSameType(model1.unit, model2.unit, "SUBTRACT");

			Quantity<IMeasurable> q1 = new Quantity<>(model1.value, model1.unit);
			Quantity<IMeasurable> q2 = new Quantity<>(model2.value, model2.unit);
			Quantity<IMeasurable> result = q1.subtract(q2);

			QuantityModel<IMeasurable> resultModel = new QuantityModel<>(result.getValue(), result.getUnit());

			QuantityMeasurementEntity entity = new QuantityMeasurementEntity(model1, model2, "SUBTRACT", resultModel);
			repository.save(entity);

			return toQuantityDTO(resultModel);

		} catch (QuantityMeasurementException e) {
			throw e;
		} catch (Exception e) {
			throw new QuantityMeasurementException("Subtraction failed: " + e.getMessage(), e);
		}
	}

	// DIVIDE

	/**
	 * Divides the first quantity by the second and returns the scalar result.
	 * 
	 */
	@Override
	public double divide(QuantityDTO quantityDTO1, QuantityDTO quantityDTO2) {
		try {
			QuantityModel<IMeasurable> model1 = toQuantityModel(quantityDTO1);
			QuantityModel<IMeasurable> model2 = toQuantityModel(quantityDTO2);

			validateSameType(model1.unit, model2.unit, "DIVIDE");

			Quantity<IMeasurable> q1 = new Quantity<>(model1.value, model1.unit);
			Quantity<IMeasurable> q2 = new Quantity<>(model2.value, model2.unit);
			double result = q1.divide(q2);

			QuantityMeasurementEntity entity = new QuantityMeasurementEntity(model1, model2, "DIVIDE",
					String.valueOf(result));
			repository.save(entity);

			return result;

		} catch (QuantityMeasurementException e) {
			throw e;
		} catch (Exception e) {
			throw new QuantityMeasurementException("Division failed: " + e.getMessage(), e);
		}
	}

	// PRIVATE HELPER METHODS

	/**
	 * Converts a QuantityDTO to an internal QuantityModel<IMeasurable>. Resolves
	 * 
	 */
	private QuantityModel<IMeasurable> toQuantityModel(QuantityDTO dto) {

		if (dto == null)
			throw new QuantityMeasurementException("QuantityDTO cannot be null");

		String unitName = dto.unit.getUnitName();
		String measurementType = dto.unit.getMeasurementType();

		// Resolve the correct IMeasurable unit instance based on measurement type
		IMeasurable resolvedUnit = resolveUnit(measurementType, unitName);

		return new QuantityModel<>(dto.value, resolvedUnit);
	}

	/*
	 * Resolves a unit name to its concrete IMeasurable enum instance based on the
	 * 
	 */
	private IMeasurable resolveUnit(String measurementType, String unitName) {
		switch (measurementType) {
		case "LengthUnit":
			return com.apps.quantitymeasurement.unit.LengthUnit.INCH.getUnitInstance(unitName);
		case "WeightUnit":
			return com.apps.quantitymeasurement.unit.WeightUnit.GRAM.getUnitInstance(unitName);
		case "VolumeUnit":
			return com.apps.quantitymeasurement.unit.VolumeUnit.LITRE.getUnitInstance(unitName);
		case "TemperatureUnit":
			return com.apps.quantitymeasurement.unit.TemperatureUnit.CELSIUS.getUnitInstance(unitName);
		default:
			throw new QuantityMeasurementException("Unknown measurement type: " + measurementType);
		}
	}

	/*
	 * Validates that two IMeasurable units belong to the same measurement type.
	 * 
	 */
	private void validateSameType(IMeasurable unit1, IMeasurable unit2, String operation) {
		if (!unit1.getMeasurementType().equals(unit2.getMeasurementType())) {
			throw new QuantityMeasurementException("Cannot perform " + operation + " on incompatible types: "
					+ unit1.getMeasurementType() + " and " + unit2.getMeasurementType());
		}
	}

	/*
	 * Converts an internal QuantityModel back to a QuantityDTO for output. Maps the
	 * IMeasurable unit back to the corresponding DTO inner enum.
	 * 
	 */
	private QuantityDTO toQuantityDTO(QuantityModel<IMeasurable> model) {
		String measurementType = model.unit.getMeasurementType();
		String unitName = model.unit.getUnitName();

		QuantityDTO.IMeasurableUnit dtoUnit;

		switch (measurementType) {
		case "LengthUnit":
			dtoUnit = QuantityDTO.LengthUnit.valueOf(unitName);
			break;
		case "WeightUnit":
			dtoUnit = QuantityDTO.WeightUnit.valueOf(unitName);
			break;
		case "VolumeUnit":
			dtoUnit = QuantityDTO.VolumeUnit.valueOf(unitName);
			break;
		case "TemperatureUnit":
			dtoUnit = QuantityDTO.TemperatureUnit.valueOf(unitName);
			break;
		default:
			throw new QuantityMeasurementException("Unknown measurement type for DTO conversion: " + measurementType);
		}

		return new QuantityDTO(model.value, dtoUnit);
	}
}