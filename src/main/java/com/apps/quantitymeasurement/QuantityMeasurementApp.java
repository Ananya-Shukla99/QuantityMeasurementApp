package com.apps.quantitymeasurement;

import java.sql.SQLException;

import com.apps.quantitymeasurement.controller.QuantityMeasurementController;
import com.apps.quantitymeasurement.entity.QuantityDTO;
import com.apps.quantitymeasurement.repository.H2ConnectionManager;
import com.apps.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.apps.quantitymeasurement.repository.QuantityMeasurementH2Repository;
import com.apps.quantitymeasurement.service.IQuantityMeasurementService;
import com.apps.quantitymeasurement.service.QuantityMeasurementServiceImpl;

public class QuantityMeasurementApp {

	public static void main(String[] args) throws SQLException {

		// ── 1. Initialize Repository (Singleton) ─────────────────────────────
		// IQuantityMeasurementRepository repository =
		// QuantityMeasurementCacheRepository.getInstance();

		IQuantityMeasurementRepository repository = QuantityMeasurementH2Repository.getInstance();

		// ── 2. Initialize Service with injected Repository (Factory + DI) ────
		IQuantityMeasurementService service = new QuantityMeasurementServiceImpl(repository);

		// ── 3. Initialize Controller with injected Service (Factory + DI) ────
		QuantityMeasurementController controller = new QuantityMeasurementController(service);

		// LENGTH OPERATIONS
		System.out.println("***********************************************");
		System.out.println("LENGTH OPERATIONS");
		System.out.println("***********************************************");

		QuantityDTO length1 = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
		QuantityDTO length2 = new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCH);

		controller.performComparison(length1, length2);
		controller.performConversion(length1, "INCH");
		controller.performAddition(length1, length2);
		controller.performAddition(length1, length2, "FEET");
		controller.performSubtraction(length1, length2);
		controller.performDivision(length1, length2);

		// WEIGHT OPERATIONS
		System.out.println("***********************************************");
		System.out.println("WEIGHT OPERATIONS");
		System.out.println("***********************************************");

		QuantityDTO weight1 = new QuantityDTO(1.0, QuantityDTO.WeightUnit.KILOGRAM);
		QuantityDTO weight2 = new QuantityDTO(1000.0, QuantityDTO.WeightUnit.GRAM);
		QuantityDTO weight3 = new QuantityDTO(2.20462, QuantityDTO.WeightUnit.POUND);

		controller.performComparison(weight1, weight2);
		controller.performConversion(weight1, "GRAM");
		controller.performAddition(weight1, weight3);
		controller.performAddition(weight1, weight3, "GRAM");
		controller.performSubtraction(weight1, weight3);
		controller.performDivision(weight1, weight3);

		// VOLUME OPERATIONS
		System.out.println("***********************************************");
		System.out.println("VOLUME OPERATIONS");
		System.out.println("***********************************************");

		QuantityDTO volume1 = new QuantityDTO(1.0, QuantityDTO.VolumeUnit.LITRE);
		QuantityDTO volume2 = new QuantityDTO(1000.0, QuantityDTO.VolumeUnit.MILLILITRE);
		QuantityDTO volume3 = new QuantityDTO(1.0, QuantityDTO.VolumeUnit.GALLON);

		controller.performComparison(volume1, volume2);
		controller.performConversion(volume1, "MILLILITRE");
		controller.performAddition(volume1, volume2);
		controller.performAddition(volume1, volume3, "MILLILITRE");
		controller.performSubtraction(volume1, volume2);
		controller.performDivision(volume1, volume2);

		// TEMPERATURE OPERATIONS
		System.out.println("***********************************************");
		System.out.println("TEMPERATURE OPERATIONS");
		System.out.println("***********************************************");

		QuantityDTO temp1 = new QuantityDTO(0.0, QuantityDTO.TemperatureUnit.CELSIUS);
		QuantityDTO temp2 = new QuantityDTO(32.0, QuantityDTO.TemperatureUnit.FAHRENHEIT);
		QuantityDTO temp3 = new QuantityDTO(273.15, QuantityDTO.TemperatureUnit.KELVIN);

		controller.performComparison(temp1, temp2);
		controller.performConversion(temp1, "FAHRENHEIT");

		// Temperature does not support arithmetic — controller handles gracefully
		controller.performAddition(temp1, temp3, "CELSIUS");
		controller.performSubtraction(temp3, temp1);
		controller.performDivision(temp3, temp1);

		System.out.println("***********************************************");

		// ── 4. Display repository summary ────────────────────────────────────
		System.out.println("Total operations saved to repository: " + repository.getAllMeasurements().size());
		System.out.println("***********************************************");
	}
}
