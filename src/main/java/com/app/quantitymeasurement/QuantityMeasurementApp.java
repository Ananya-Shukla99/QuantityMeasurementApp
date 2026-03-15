package com.app.quantitymeasurement;

import com.app.quantitymeasurement.controller.QuantityMeasurementController;
import com.app.quantitymeasurement.entity.QuantityDTO;
import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.app.quantitymeasurement.repository.QuantityMeasurementCacheRepository;
import com.app.quantitymeasurement.repository.QuantityMeasurementDatabaseRepository;
import com.app.quantitymeasurement.service.IQuantityMeasurementService;
import com.app.quantitymeasurement.service.QuantityMeasurementServiceImpl;
import com.app.quantitymeasurement.util.DatabaseConfig;

import java.util.List;
import java.util.logging.Logger;

/**
 * QuantityMeasurementApp is the main entry point of the application.
 * Initializes repository, service and controller based on configuration.
 * Supports both Cache and Database repositories via dependency injection.
 */
public class QuantityMeasurementApp {

    private static final Logger logger = Logger.getLogger(
            QuantityMeasurementApp.class.getName()
    );

    // ── Singleton ─────────────────────────────────────────────────────────────
    private static QuantityMeasurementApp instance;

    // ── Dependencies ──────────────────────────────────────────────────────────
    private final IQuantityMeasurementRepository repository;
    private final QuantityMeasurementController controller;

    // ── Private Constructor ───────────────────────────────────────────────────
    private QuantityMeasurementApp() {

        // Load config and determine repository type
        DatabaseConfig config = DatabaseConfig.getInstance();

        if (config.isDatabaseRepository()) {
            this.repository = QuantityMeasurementDatabaseRepository
                    .getInstance();
            logger.info("Initialized with Database Repository.");
        } else {
            this.repository = QuantityMeasurementCacheRepository
                    .getInstance();
            logger.info("Initialized with Cache Repository.");
        }

        // Initialize service with injected repository
        IQuantityMeasurementService service =
                new QuantityMeasurementServiceImpl(repository);

        // Initialize controller with injected service
        this.controller = new QuantityMeasurementController(service);

        logger.info("QuantityMeasurementApp initialized.");
    }

    // ── getInstance ───────────────────────────────────────────────────────────
    public static QuantityMeasurementApp getInstance() {
        if (instance == null) {
            instance = new QuantityMeasurementApp();
        }
        return instance;
    }

    // ── Delete All Measurements ───────────────────────────────────────────────

    /**
     * Deletes all measurements from the repository.
     * Useful for testing and resetting application state.
     */
    public void deleteAllMeasurements() {
        repository.deleteAll();
        logger.info("All measurements deleted from repository.");
    }

    // ── Close Resources ───────────────────────────────────────────────────────

    /**
     * Releases all resources held by the repository.
     * Should be called on application shutdown.
     */
    public void closeResources() {
        repository.releaseResources();
        logger.info("Resources released. Application shutting down.");
    }

    // ── Report All Measurements ───────────────────────────────────────────────

    /**
     * Prints all stored measurements to console.
     */
    private void reportAllMeasurements() {
        List<QuantityMeasurementEntity> all =
                repository.getAllMeasurements();

        System.out.println("***********************************************");
        System.out.println("STORED MEASUREMENTS — Total: " + all.size());
        System.out.println("***********************************************");

        all.forEach(e -> System.out.println(e));

        System.out.println("***********************************************");
        System.out.println("Pool Stats: " + repository.getPoolStatistics());
        System.out.println("***********************************************");
    }

    // ── MAIN ──────────────────────────────────────────────────────────────────
    public static void main(String[] args) {

        // ── 1. Get App Instance ───────────────────────────────────────────────
        QuantityMeasurementApp app = getInstance();
        QuantityMeasurementController controller = app.controller;

        // ── LENGTH OPERATIONS ─────────────────────────────────────────────────
        System.out.println("***********************************************");
        System.out.println("LENGTH OPERATIONS");
        System.out.println("***********************************************");

        QuantityDTO length1 = new QuantityDTO(
                1.0,
                QuantityDTO.LengthUnit.FEET.getUnitName(),
                QuantityDTO.LengthUnit.FEET.getMeasurementType()
        );
        QuantityDTO length2 = new QuantityDTO(
                12.0,
                QuantityDTO.LengthUnit.INCH.getUnitName(),
                QuantityDTO.LengthUnit.INCH.getMeasurementType()
        );

        controller.performComparison(length1, length2);
        controller.performConversion(length1, "INCH");
        controller.performAddition(length1, length2);
        controller.performAddition(length1, length2, "FEET");
        controller.performSubtraction(length1, length2);
        controller.performDivision(length1, length2);

        // ── WEIGHT OPERATIONS ─────────────────────────────────────────────────
        System.out.println("***********************************************");
        System.out.println("WEIGHT OPERATIONS");
        System.out.println("***********************************************");

        QuantityDTO weight1 = new QuantityDTO(
                1.0,
                QuantityDTO.WeightUnit.KILOGRAM.getUnitName(),
                QuantityDTO.WeightUnit.KILOGRAM.getMeasurementType()
        );
        QuantityDTO weight2 = new QuantityDTO(
                1000.0,
                QuantityDTO.WeightUnit.GRAM.getUnitName(),
                QuantityDTO.WeightUnit.GRAM.getMeasurementType()
        );
        QuantityDTO weight3 = new QuantityDTO(
                2.20462,
                QuantityDTO.WeightUnit.POUND.getUnitName(),
                QuantityDTO.WeightUnit.POUND.getMeasurementType()
        );

        controller.performComparison(weight1, weight2);
        controller.performConversion(weight1, "GRAM");
        controller.performAddition(weight1, weight3);
        controller.performAddition(weight1, weight3, "GRAM");
        controller.performSubtraction(weight1, weight3);
        controller.performDivision(weight1, weight3);

        // ── VOLUME OPERATIONS ─────────────────────────────────────────────────
        System.out.println("***********************************************");
        System.out.println("VOLUME OPERATIONS");
        System.out.println("***********************************************");

        QuantityDTO volume1 = new QuantityDTO(
                1.0,
                QuantityDTO.VolumeUnit.LITRE.getUnitName(),
                QuantityDTO.VolumeUnit.LITRE.getMeasurementType()
        );
        QuantityDTO volume2 = new QuantityDTO(
                1000.0,
                QuantityDTO.VolumeUnit.MILLILITRE.getUnitName(),
                QuantityDTO.VolumeUnit.MILLILITRE.getMeasurementType()
        );
        QuantityDTO volume3 = new QuantityDTO(
                1.0,
                QuantityDTO.VolumeUnit.GALLON.getUnitName(),
                QuantityDTO.VolumeUnit.GALLON.getMeasurementType()
        );

        controller.performComparison(volume1, volume2);
        controller.performConversion(volume1, "MILLILITRE");
        controller.performAddition(volume1, volume2);
        controller.performAddition(volume1, volume3, "MILLILITRE");
        controller.performSubtraction(volume1, volume2);
        controller.performDivision(volume1, volume2);

        // ── TEMPERATURE OPERATIONS ────────────────────────────────────────────
        System.out.println("***********************************************");
        System.out.println("TEMPERATURE OPERATIONS");
        System.out.println("***********************************************");

        QuantityDTO temp1 = new QuantityDTO(
                0.0,
                QuantityDTO.TemperatureUnit.CELSIUS.getUnitName(),
                QuantityDTO.TemperatureUnit.CELSIUS.getMeasurementType()
        );
        QuantityDTO temp2 = new QuantityDTO(
                32.0,
                QuantityDTO.TemperatureUnit.FAHRENHEIT.getUnitName(),
                QuantityDTO.TemperatureUnit.FAHRENHEIT.getMeasurementType()
        );
        QuantityDTO temp3 = new QuantityDTO(
                273.15,
                QuantityDTO.TemperatureUnit.KELVIN.getUnitName(),
                QuantityDTO.TemperatureUnit.KELVIN.getMeasurementType()
        );

        controller.performComparison(temp1, temp2);
        controller.performConversion(temp1, "FAHRENHEIT");

        // Temperature does not support arithmetic
        controller.performAddition(temp1, temp3, "CELSIUS");
        controller.performSubtraction(temp3, temp1);
        controller.performDivision(temp3, temp1);

        // ── 2. Report All Measurements ────────────────────────────────────────
        app.reportAllMeasurements();

        // ── 3. Delete All Measurements ────────────────────────────────────────
        app.deleteAllMeasurements();
        logger.info("Total after delete: "
                + app.repository.getCount());

        // ── 4. Close Resources ────────────────────────────────────────────────
        app.closeResources();
    }
}