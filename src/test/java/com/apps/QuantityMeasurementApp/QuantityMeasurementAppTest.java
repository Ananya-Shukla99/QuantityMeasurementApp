package com.apps.QuantityMeasurementApp;

import org.junit.jupiter.api.Test;

import com.apps.quantitymeasurement.controller.QuantityMeasurementController;
import com.apps.quantitymeasurement.entity.QuantityDTO;
import com.apps.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.apps.quantitymeasurement.entity.QuantityModel;
import com.apps.quantitymeasurement.exception.QuantityMeasurementException;
import com.apps.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.apps.quantitymeasurement.repository.QuantityMeasurementH2Repository;
import com.apps.quantitymeasurement.service.IQuantityMeasurementService;
import com.apps.quantitymeasurement.service.QuantityMeasurementServiceImpl;
import com.apps.quantitymeasurement.unit.IMeasurable;
import com.apps.quantitymeasurement.unit.LengthUnit;
import com.apps.quantitymeasurement.unit.Quantity;
import com.apps.quantitymeasurement.unit.VolumeUnit;
import com.apps.quantitymeasurement.unit.WeightUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

// Testing Class
class QuantityMeasurementAppTest {

	// UC15 Test Suite — N-Tier Architecture Tests

	// ── Shared test fixtures ──────────────────────────────────────────────────
	private IQuantityMeasurementRepository repository;
	private IQuantityMeasurementService service;
	private QuantityMeasurementController controller;

	// Common QuantityDTOs reused across tests
	private QuantityDTO feet1;
	private QuantityDTO inch12;
	private QuantityDTO kg1;
	private QuantityDTO gram1000;

	private QuantityDTO litre1;
	private QuantityDTO ml1000;
	private QuantityDTO celsius0;
	private QuantityDTO fahrenheit32;
	private QuantityDTO kelvin273;

	@BeforeEach
	void setUp() {
		repository = QuantityMeasurementH2Repository.getInstance();
		service = new QuantityMeasurementServiceImpl(repository);
		controller = new QuantityMeasurementController(service);

		feet1 = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
		inch12 = new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCH);
		kg1 = new QuantityDTO(1.0, QuantityDTO.WeightUnit.KILOGRAM);
		gram1000 = new QuantityDTO(1000.0, QuantityDTO.WeightUnit.GRAM);
		litre1 = new QuantityDTO(1.0, QuantityDTO.VolumeUnit.LITRE);
		ml1000 = new QuantityDTO(1000.0, QuantityDTO.VolumeUnit.MILLILITRE);
		celsius0 = new QuantityDTO(0.0, QuantityDTO.TemperatureUnit.CELSIUS);
		fahrenheit32 = new QuantityDTO(32.0, QuantityDTO.TemperatureUnit.FAHRENHEIT);
		kelvin273 = new QuantityDTO(273.15, QuantityDTO.TemperatureUnit.KELVIN);
	}

	// ─────────────────────────────────────────────────────────────────────────
	// ENTITY LAYER TESTS
	// ─────────────────────────────────────────────────────────────────────────

	@Test
	@DisplayName("Entity: Single operand construction stores conversion data correctly")
	void testQuantityEntity_SingleOperandConstruction() {
		QuantityModel<IMeasurable> thisQ = new QuantityModel<>(1.0, LengthUnit.FEET);
		QuantityModel<IMeasurable> resultQ = new QuantityModel<>(12.0, LengthUnit.INCH);

		QuantityMeasurementEntity entity = new QuantityMeasurementEntity(thisQ, null, "CONVERT", resultQ);

		assertEquals(1.0, entity.thisValue);
		assertEquals("FEET", entity.thisUnit);
		assertEquals("LengthUnit", entity.thisMeasurementType);
		assertEquals(12.0, entity.resultValue);
		assertEquals("INCH", entity.resultUnit);
		assertEquals("CONVERT", entity.operation);
		assertFalse(entity.isError);
	}

	@Test
	@DisplayName("Entity: Binary operand construction stores addition data correctly")
	void testQuantityEntity_BinaryOperandConstruction() {
		QuantityModel<IMeasurable> thisQ = new QuantityModel<>(1.0, LengthUnit.FEET);
		QuantityModel<IMeasurable> thatQ = new QuantityModel<>(12.0, LengthUnit.INCH);
		QuantityModel<IMeasurable> resultQ = new QuantityModel<>(24.0, LengthUnit.INCH);

		QuantityMeasurementEntity entity = new QuantityMeasurementEntity(thisQ, thatQ, "ADD", resultQ);

		assertEquals(1.0, entity.thisValue);
		assertEquals(12.0, entity.thatValue);
		assertEquals("ADD", entity.operation);
		assertEquals(24.0, entity.resultValue);
		assertFalse(entity.isError);
	}

	@Test
	@DisplayName("Entity: Error construction stores error data correctly")
	void testQuantityEntity_ErrorConstruction() {
		QuantityModel<IMeasurable> thisQ = new QuantityModel<>(1.0, LengthUnit.FEET);
		QuantityModel<IMeasurable> thatQ = new QuantityModel<>(1.0, WeightUnit.KILOGRAM);

		QuantityMeasurementEntity entity = new QuantityMeasurementEntity(thisQ, thatQ, "COMPARE",
				"Cannot compare incompatible types", true);

		assertTrue(entity.isError);
		assertEquals("Cannot compare incompatible types", entity.errorMessage);
		assertEquals("COMPARE", entity.operation);
	}

	@Test
	@DisplayName("Entity: toString() formats successful results clearly")
	void testQuantityEntity_ToString_Success() {
		QuantityModel<IMeasurable> thisQ = new QuantityModel<>(1.0, LengthUnit.FEET);
		QuantityModel<IMeasurable> thatQ = new QuantityModel<>(12.0, LengthUnit.INCH);
		QuantityMeasurementEntity entity = new QuantityMeasurementEntity(thisQ, thatQ, "COMPARE", "Equal");

		String result = entity.toString();
		assertTrue(result.contains("COMPARE"));
		assertTrue(result.contains("Equal"));
	}

	@Test
	@DisplayName("Entity: toString() formats errors clearly")
	void testQuantityEntity_ToString_Error() {
		QuantityModel<IMeasurable> thisQ = new QuantityModel<>(1.0, LengthUnit.FEET);
		QuantityMeasurementEntity entity = new QuantityMeasurementEntity(thisQ, null, "ADD", "Unsupported operation",
				true);

		String result = entity.toString();
		assertTrue(result.contains("ERROR"));
		assertTrue(result.contains("Unsupported operation"));
	}

	// ─────────────────────────────────────────────────────────────────────────
	// SERVICE LAYER TESTS
	// ─────────────────────────────────────────────────────────────────────────

	@Test
	@DisplayName("Service: Compare equal quantities in same unit")
	void testService_CompareEquality_SameUnit_Success() {
		QuantityDTO q1 = new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCH);
		QuantityDTO q2 = new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCH);

		assertTrue(service.compare(q1, q2));
	}

	@Test
	@DisplayName("Service: Compare equal quantities in different units")
	void testService_CompareEquality_DifferentUnit_Success() {
		// 1 FEET == 12 INCHES
		assertTrue(service.compare(feet1, inch12));
	}

	@Test
	@DisplayName("Service: Reject cross-category comparison")
	void testService_CompareEquality_CrossCategory_Error() {
		assertThrows(QuantityMeasurementException.class, () -> service.compare(feet1, kg1));
	}

	@Test
	@DisplayName("Service: Convert FEET to INCHES correctly")
	void testService_Convert_Success() {
		QuantityDTO result = service.convert(feet1, "INCH");

		assertNotNull(result);
		assertEquals(12.0, result.value);
		assertEquals("INCH", result.unit.getUnitName());
	}

	@Test
	@DisplayName("Service: Add two length quantities")
	void testService_Add_Success() {
		QuantityDTO result = service.add(feet1, inch12);

		assertNotNull(result);
		// 1 FEET + 12 INCHES = 2 FEET
		assertEquals(2.0, result.value);
	}

	@Test
	@DisplayName("Service: Addition throws exception for temperature")
	void testService_Add_UnsupportedOperation_Error() {
		assertThrows(QuantityMeasurementException.class, () -> service.add(celsius0, fahrenheit32));
	}

	@Test
	@DisplayName("Service: Subtract two weight quantities")
	void testService_Subtract_Success() {
		// 1 KG - 1000 GRAM = 0 KG
		QuantityDTO result = service.subtract(kg1, gram1000);

		assertNotNull(result);
		assertEquals(0.0, result.value);
	}

	@Test
	@DisplayName("Service: Divide two volume quantities")
	void testService_Divide_Success() {
		// 1 LITRE / 1000 MILLILITRE = 1.0
		double result = service.divide(litre1, ml1000);

		assertEquals(1.0, result);
	}

	@Test
	@DisplayName("Service: Division by zero throws exception")
	void testService_Divide_ByZero_Error() {
		QuantityDTO zeroLitre = new QuantityDTO(0.0, QuantityDTO.VolumeUnit.LITRE);

		assertThrows(QuantityMeasurementException.class, () -> service.divide(litre1, zeroLitre));
	}

	// ─────────────────────────────────────────────────────────────────────────
	// CONTROLLER LAYER TESTS
	// ─────────────────────────────────────────────────────────────────────────

	@Test
	@DisplayName("Controller: performComparison returns correct equality result")
	void testController_DemonstrateEquality_Success() {
		// 1 KG == 1000 GRAM
		assertTrue(controller.performComparison(kg1, gram1000));
	}

	@Test
	@DisplayName("Controller: performConversion returns correct converted DTO")
	void testController_DemonstrateConversion_Success() {
		QuantityDTO result = controller.performConversion(litre1, "MILLILITRE");

		assertNotNull(result);
		assertEquals(1000.0, result.value);
	}

	@Test
	@DisplayName("Controller: performAddition returns correct sum")
	void testController_DemonstrateAddition_Success() {
		QuantityDTO result = controller.performAddition(kg1, gram1000);

		assertNotNull(result);
		assertEquals(2.0, result.value);
	}

	@Test
	@DisplayName("Controller: performAddition handles error gracefully — returns null")
	void testController_DemonstrateAddition_Error() {
		// Temperature addition is unsupported — controller should not throw
		QuantityDTO result = controller.performAddition(celsius0, kelvin273);

		assertNull(result); // controller catches exception and returns null
	}

	@Test
	@DisplayName("Controller: successful operations return non-null results")
	void testController_DisplayResult_Success() {
		QuantityDTO result = controller.performAddition(feet1, inch12);
		assertNotNull(result);
	}

	@Test
	@DisplayName("Controller: error operations return null without throwing")
	void testController_DisplayResult_Error() {
		// Cross-category subtraction — controller handles gracefully
		QuantityDTO result = controller.performSubtraction(feet1, kg1);
		assertNull(result);
	}

	// ─────────────────────────────────────────────────────────────────────────
	// LAYER SEPARATION TESTS
	// ─────────────────────────────────────────────────────────────────────────

	@Test
	@DisplayName("Layer: Service can be tested independently without controller")
	void testLayerSeparation_ServiceIndependence() {
		// Create a fresh service with its own repo — no controller needed
		IQuantityMeasurementRepository freshRepo = QuantityMeasurementH2Repository.getInstance();
		IQuantityMeasurementService freshService = new QuantityMeasurementServiceImpl(freshRepo);

		assertTrue(freshService.compare(feet1, inch12));
	}

	@Test
	@DisplayName("Layer: Controller works with any IQuantityMeasurementService implementation")
	void testLayerSeparation_ControllerIndependence() {
		// Inline anonymous mock service — simulates a mock
		IQuantityMeasurementService mockService = new IQuantityMeasurementService() {
			@Override
			public boolean compare(QuantityDTO q1, QuantityDTO q2) {
				return true;
			}

			@Override
			public QuantityDTO convert(QuantityDTO q, String t) {
				return new QuantityDTO(99.0, QuantityDTO.LengthUnit.INCH);
			}

			@Override
			public QuantityDTO add(QuantityDTO q1, QuantityDTO q2) {
				return new QuantityDTO(2.0, QuantityDTO.LengthUnit.FEET);
			}

			@Override
			public QuantityDTO add(QuantityDTO q1, QuantityDTO q2, String t) {
				return new QuantityDTO(2.0, QuantityDTO.LengthUnit.FEET);
			}

			@Override
			public QuantityDTO subtract(QuantityDTO q1, QuantityDTO q2) {
				return new QuantityDTO(0.0, QuantityDTO.LengthUnit.FEET);
			}

			@Override
			public double divide(QuantityDTO q1, QuantityDTO q2) {
				return 1.0;
			}
		};

		QuantityMeasurementController mockController = new QuantityMeasurementController(mockService);

		// Controller works correctly with the mock
		assertTrue(mockController.performComparison(feet1, inch12));
		assertEquals(99.0, mockController.performConversion(feet1, "INCH").value);
	}

	@Test
	@DisplayName("Data Flow: QuantityDTO correctly flows from controller to service")
	void testDataFlow_ControllerToService() {
		// Verify the DTO passed in produces the correct result at service level
		QuantityDTO result = service.convert(new QuantityDTO(1.0, QuantityDTO.WeightUnit.KILOGRAM), "GRAM");

		assertNotNull(result);
		assertEquals(1000.0, result.value);
		assertEquals("GRAM", result.unit.getUnitName());
	}

	@Test
	@DisplayName("Data Flow: Results correctly flow from service back to controller")
	void testDataFlow_ServiceToController() {
		QuantityDTO result = controller.performConversion(kg1, "GRAM");

		assertNotNull(result);
		assertEquals(1000.0, result.value);
		assertEquals("GRAM", result.unit.getUnitName());
	}

	// ─────────────────────────────────────────────────────────────────────────
	// BACKWARD COMPATIBILITY — UC1 to UC14
	// ─────────────────────────────────────────────────────────────────────────

	@Test
	@DisplayName("Backward Compatibility: All UC1-UC14 behaviors preserved")
	void testBackwardCompatibility_AllUC1_UC14_Tests() {
		// LENGTH
		assertTrue(service.compare(feet1, inch12));
		assertEquals(12.0, service.convert(feet1, "INCH").value);
		assertEquals(2.0, service.add(feet1, inch12).value);
		assertEquals(0.0, service.subtract(feet1, inch12).value);
		assertEquals(1.0, service.divide(feet1, inch12));

		// WEIGHT
		assertTrue(service.compare(kg1, gram1000));
		assertEquals(1000.0, service.convert(kg1, "GRAM").value);

		// VOLUME
		assertTrue(service.compare(litre1, ml1000));
		assertEquals(1000.0, service.convert(litre1, "MILLILITRE").value);

		// TEMPERATURE — compare and convert only
		assertTrue(service.compare(celsius0, fahrenheit32));
		assertEquals(32.0, service.convert(celsius0, "FAHRENHEIT").value);

		// Temperature arithmetic must still throw
		assertThrows(QuantityMeasurementException.class, () -> service.add(celsius0, kelvin273));
		assertThrows(QuantityMeasurementException.class, () -> service.subtract(celsius0, kelvin273));
		assertThrows(QuantityMeasurementException.class, () -> service.divide(celsius0, kelvin273));
	}

	@Test
	@DisplayName("Service: Works correctly across all measurement categories")
	void testService_AllMeasurementCategories() {
		// Length
		assertNotNull(service.convert(feet1, "INCH"));
		// Weight
		assertNotNull(service.convert(kg1, "GRAM"));
		// Volume
		assertNotNull(service.convert(litre1, "MILLILITRE"));
		// Temperature
		assertNotNull(service.convert(celsius0, "FAHRENHEIT"));
	}

	@Test
	@DisplayName("Controller: All operations covered — add, subtract, divide, compare, convert")
	void testController_AllOperations() {
		assertNotNull(controller.performAddition(feet1, inch12));
		assertNotNull(controller.performSubtraction(feet1, inch12));
		assertTrue(controller.performDivision(feet1, inch12) > 0);
		assertTrue(controller.performComparison(feet1, inch12));
		assertNotNull(controller.performConversion(feet1, "INCH"));
	}

	@Test
	@DisplayName("Service: Validation errors are consistent across all operations")
	void testService_ValidationConsistency() {
		// All operations should throw same exception type for cross-category input
		assertThrows(QuantityMeasurementException.class, () -> service.compare(feet1, kg1));
		assertThrows(QuantityMeasurementException.class, () -> service.add(feet1, kg1));
		assertThrows(QuantityMeasurementException.class, () -> service.subtract(feet1, kg1));
		assertThrows(QuantityMeasurementException.class, () -> service.divide(feet1, kg1));
	}

	@Test
	@DisplayName("Entity: Fields set only through constructors — effectively immutable")
	void testEntity_Immutability() {
		QuantityModel<IMeasurable> thisQ = new QuantityModel<>(1.0, LengthUnit.FEET);
		QuantityModel<IMeasurable> thatQ = new QuantityModel<>(12.0, LengthUnit.INCH);
		QuantityMeasurementEntity entity = new QuantityMeasurementEntity(thisQ, thatQ, "COMPARE", "Equal");

		// Values set in constructor remain unchanged
		assertEquals(1.0, entity.thisValue);
		assertEquals(12.0, entity.thatValue);
		assertEquals("COMPARE", entity.operation);
		assertEquals("Equal", entity.resultString);
	}

	@Test
	@DisplayName("Service: All operations convert exceptions to QuantityMeasurementException")
	void testService_ExceptionHandling_AllOperations() {
		QuantityDTO invalid = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
		QuantityDTO wrongType = new QuantityDTO(1.0, QuantityDTO.WeightUnit.GRAM);

		assertThrows(QuantityMeasurementException.class, () -> service.compare(invalid, wrongType));
		assertThrows(QuantityMeasurementException.class, () -> service.add(invalid, wrongType));
		assertThrows(QuantityMeasurementException.class, () -> service.subtract(invalid, wrongType));
		assertThrows(QuantityMeasurementException.class, () -> service.divide(invalid, wrongType));
	}

	@Test
	@DisplayName("Service: Rejects null QuantityDTO input")
	void testService_NullEntity_Rejection() {
		assertThrows(QuantityMeasurementException.class, () -> service.compare(null, feet1));
		assertThrows(QuantityMeasurementException.class, () -> service.convert(null, "INCH"));
		assertThrows(QuantityMeasurementException.class, () -> service.add(null, feet1));
	}

	@Test
	@DisplayName("Controller: Requires non-null service — throws on null")
	void testController_NullService_Prevention() {
		// Controller with null service should throw NullPointerException
		// when a method is invoked
		QuantityMeasurementController nullController = new QuantityMeasurementController(null);

		assertThrows(Exception.class, () -> nullController.performComparison(feet1, inch12));
	}

	@Test
	@DisplayName("Service: Polymorphic behavior — works with any IMeasurable unit")
	void testService_AllUnitImplementations() {
		// LengthUnit
		assertDoesNotThrow(() -> service.compare(feet1, inch12));
		// WeightUnit
		assertDoesNotThrow(() -> service.compare(kg1, gram1000));
		// VolumeUnit
		assertDoesNotThrow(() -> service.compare(litre1, ml1000));
		// TemperatureUnit
		assertDoesNotThrow(() -> service.compare(celsius0, fahrenheit32));
	}

	@Test
	@DisplayName("Entity: Operation type correctly recorded in entity")
	void testEntity_OperationType_Tracking() {
		QuantityModel<IMeasurable> q = new QuantityModel<>(1.0, LengthUnit.FEET);
		QuantityModel<IMeasurable> r = new QuantityModel<>(12.0, LengthUnit.INCH);

		QuantityMeasurementEntity convertEntity = new QuantityMeasurementEntity(q, null, "CONVERT", r);
		QuantityMeasurementEntity addEntity = new QuantityMeasurementEntity(q, q, "ADD", r);
		QuantityMeasurementEntity compareEntity = new QuantityMeasurementEntity(q, q, "COMPARE", "Equal");

		assertEquals("CONVERT", convertEntity.operation);
		assertEquals("ADD", addEntity.operation);
		assertEquals("COMPARE", compareEntity.operation);
	}

	@Test
	@DisplayName("Layer Decoupling: Changing service implementation doesn't affect controller")
	void testLayerDecoupling_ServiceChange() {
		// Swap service implementation — controller behavior unchanged
		IQuantityMeasurementService altService = new QuantityMeasurementServiceImpl(
				QuantityMeasurementH2Repository.getInstance());
		QuantityMeasurementController altController = new QuantityMeasurementController(altService);

		// Same result regardless of which service instance is used
		assertTrue(altController.performComparison(feet1, inch12));
	}

	@Test
	@DisplayName("Layer Decoupling: Adding entity fields doesn't break other layers")
	void testLayerDecoupling_EntityChange() {
		// Entity is used as a stable data contract between layers
		// Service creates it, repo stores it — no layer needs to change
		QuantityDTO result = service.add(feet1, inch12);
		assertNotNull(result);

		// Repository stored the entity without issue
		assertFalse(repository.getAllMeasurements().isEmpty());
	}

	// ─────────────────────────────────────────────────────────────────────────
	// INTEGRATION TESTS — End to End
	// ─────────────────────────────────────────────────────────────────────────

	@Test
	@DisplayName("Integration: End-to-end length addition — full layer cooperation")
	void testIntegration_EndToEnd_LengthAddition() {
		// Simulates: User provides input → App → Controller → Service → Core → Result
		QuantityDTO q1 = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
		QuantityDTO q2 = new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCH);

		QuantityDTO result = controller.performAddition(q1, q2, "FEET");

		assertNotNull(result);
		assertEquals(2.0, result.value);
		assertEquals("FEET", result.unit.getUnitName());
	}

	@Test
	@DisplayName("Integration: End-to-end temperature error handling across all layers")
	void testIntegration_EndToEnd_TemperatureUnsupported() {
		// Controller catches the exception — returns null, doesn't throw
		QuantityDTO result = controller.performAddition(celsius0, kelvin273);
		assertNull(result);

		// Service throws the exception directly
		assertThrows(QuantityMeasurementException.class, () -> service.add(celsius0, kelvin273));
	}

	@Test
	@DisplayName("Scalability: Adding new operation (modulo-style divide) doesn't break layers")
	void testScalability_NewOperation_Addition() {
		// Verifies existing operations still work correctly when system grows
		// New operations can be added to service without touching controller or entity
		assertDoesNotThrow(() -> {
			service.add(feet1, inch12);
			service.subtract(feet1, inch12);
			service.divide(feet1, inch12);
			service.compare(feet1, inch12);
			service.convert(feet1, "INCH");
		});
	}

	private static final double EPSILON = 1e-5;

	// EQUALITY TESTS

	@Test
	void testEquality_LitreToLitre_SameValue() {
		assertEquals(new Quantity<>(1.0, VolumeUnit.LITRE), new Quantity<>(1.0, VolumeUnit.LITRE));
	}

	@Test
	void testEquality_LitreToLitre_DifferentValue() {
		assertNotEquals(new Quantity<>(1.0, VolumeUnit.LITRE), new Quantity<>(2.0, VolumeUnit.LITRE));
	}

	@Test
	void testEquality_LitreToMillilitre_EquivalentValue() {
		assertEquals(new Quantity<>(1.0, VolumeUnit.LITRE), new Quantity<>(1000.0, VolumeUnit.MILLILITRE));
	}

	@Test
	void testEquality_MillilitreToLitre_EquivalentValue() {
		assertEquals(new Quantity<>(1000.0, VolumeUnit.MILLILITRE), new Quantity<>(1.0, VolumeUnit.LITRE));
	}

	@Test
	void testEquality_LitreToGallon_EquivalentValue() {
		Quantity<VolumeUnit> litre = new Quantity<>(1.0, VolumeUnit.LITRE);
		Quantity<VolumeUnit> gallon = new Quantity<>(0.264172, VolumeUnit.GALLON);
		assertTrue(litre.equals(gallon));
	}

	@Test
	void testEquality_GallonToLitre_EquivalentValue() {
		Quantity<VolumeUnit> gallon = new Quantity<>(1.0, VolumeUnit.GALLON);
		Quantity<VolumeUnit> litre = new Quantity<>(3.78541, VolumeUnit.LITRE);
		assertTrue(gallon.equals(litre));
	}

	@Test
	void testEquality_VolumeVsLength_Incompatible() {
		assertNotEquals(new Quantity<>(1.0, VolumeUnit.LITRE), new Quantity<>(1.0, LengthUnit.FEET));
	}

	@Test
	void testEquality_VolumeVsWeight_Incompatible() {
		assertNotEquals(new Quantity<>(1.0, VolumeUnit.LITRE), new Quantity<>(1.0, WeightUnit.KILOGRAM));
	}

	@Test
	void testEquality_NullComparison() {
		assertFalse(new Quantity<>(1.0, VolumeUnit.LITRE).equals(null));
	}

	@Test
	void testEquality_SameReference() {
		Quantity<VolumeUnit> volume = new Quantity<>(1.0, VolumeUnit.LITRE);
		assertEquals(volume, volume);
	}

	@Test
	void testEquality_NullUnit() {
		assertThrows(IllegalArgumentException.class, () -> new Quantity<>(1.0, null));
	}

	@Test
	void testEquality_TransitiveProperty() {
		Quantity<VolumeUnit> a = new Quantity<>(1.0, VolumeUnit.LITRE);
		Quantity<VolumeUnit> b = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
		Quantity<VolumeUnit> c = new Quantity<>(1.0, VolumeUnit.LITRE);

		assertTrue(a.equals(b) && b.equals(c) && a.equals(c));
	}

	@Test
	void testEquality_ZeroValue() {
		assertEquals(new Quantity<>(0.0, VolumeUnit.LITRE), new Quantity<>(0.0, VolumeUnit.MILLILITRE));
	}

	@Test
	void testEquality_NegativeVolume() {
		assertEquals(new Quantity<>(-1.0, VolumeUnit.LITRE), new Quantity<>(-1000.0, VolumeUnit.MILLILITRE));
	}

	@Test
	void testEquality_LargeVolumeValue() {
		assertEquals(new Quantity<>(1000000.0, VolumeUnit.MILLILITRE), new Quantity<>(1000.0, VolumeUnit.LITRE));
	}

	@Test
	void testEquality_SmallVolumeValue() {
		assertEquals(new Quantity<>(0.001, VolumeUnit.LITRE), new Quantity<>(1.0, VolumeUnit.MILLILITRE));
	}

	// CONVERSION TESTS

	@Test
	void testConversion_LitreToMillilitre() {
		Quantity<VolumeUnit> result = new Quantity<>(1.0, VolumeUnit.LITRE).convertTo(VolumeUnit.MILLILITRE);

		assertEquals(1000.0, result.getValue(), EPSILON);
	}

	@Test
	void testConversion_MillilitreToLitre() {
		Quantity<VolumeUnit> result = new Quantity<>(1000.0, VolumeUnit.MILLILITRE).convertTo(VolumeUnit.LITRE);

		assertEquals(1.0, result.getValue(), EPSILON);
	}

	@Test
	void testConversion_LitreToGallon() {
		Quantity<VolumeUnit> result = new Quantity<>(3.78541, VolumeUnit.LITRE).convertTo(VolumeUnit.GALLON);

		assertEquals(1.0, result.getValue(), EPSILON);
	}

	@Test
	void testConversion_SameUnit() {
		Quantity<VolumeUnit> result = new Quantity<>(5.0, VolumeUnit.LITRE).convertTo(VolumeUnit.LITRE);

		assertEquals(5.0, result.getValue(), EPSILON);
	}

	@Test
	void testConversion_ZeroValue() {
		Quantity<VolumeUnit> result = new Quantity<>(0.0, VolumeUnit.LITRE).convertTo(VolumeUnit.MILLILITRE);

		assertEquals(0.0, result.getValue(), EPSILON);
	}

	@Test
	void testConversion_NegativeValue() {
		Quantity<VolumeUnit> result = new Quantity<>(-1.0, VolumeUnit.LITRE).convertTo(VolumeUnit.MILLILITRE);

		assertEquals(-1000.0, result.getValue(), EPSILON);
	}

	@Test
	void testConversion_RoundTrip() {
		Quantity<VolumeUnit> result = new Quantity<>(1.5, VolumeUnit.LITRE).convertTo(VolumeUnit.MILLILITRE)
				.convertTo(VolumeUnit.LITRE);

		assertEquals(1.5, result.getValue(), EPSILON);
	}

	// ADDITION TESTS

	@Test
	void testAddition_SameUnit_LitrePlusLitre() {
		Quantity<VolumeUnit> result = new Quantity<>(1.0, VolumeUnit.LITRE).add(new Quantity<>(2.0, VolumeUnit.LITRE));

		assertEquals(3.0, result.getValue(), EPSILON);
	}

	@Test
	void testAddition_SameUnit_MillilitrePlusMillilitre() {
		Quantity<VolumeUnit> result = new Quantity<>(500.0, VolumeUnit.MILLILITRE)
				.add(new Quantity<>(500.0, VolumeUnit.MILLILITRE));

		assertEquals(1000.0, result.getValue(), EPSILON);
	}

	@Test
	void testAddition_CrossUnit_LitrePlusMillilitre() {
		Quantity<VolumeUnit> result = new Quantity<>(1.0, VolumeUnit.LITRE)
				.add(new Quantity<>(1000.0, VolumeUnit.MILLILITRE));

		assertEquals(2.0, result.getValue(), EPSILON);
	}

	@Test
	void testAddition_CrossUnit_MillilitrePlusLitre() {
		Quantity<VolumeUnit> result = new Quantity<>(1000.0, VolumeUnit.MILLILITRE)
				.add(new Quantity<>(1.0, VolumeUnit.LITRE));

		assertEquals(2000.0, result.getValue(), EPSILON);
	}

	@Test
	void testAddition_WithZero() {
		Quantity<VolumeUnit> result = new Quantity<>(5.0, VolumeUnit.LITRE)
				.add(new Quantity<>(0.0, VolumeUnit.MILLILITRE));

		assertEquals(5.0, result.getValue(), EPSILON);
	}

	@Test
	void testAddition_NegativeValues() {
		Quantity<VolumeUnit> result = new Quantity<>(5.0, VolumeUnit.LITRE)
				.add(new Quantity<>(-2000.0, VolumeUnit.MILLILITRE));

		assertEquals(3.0, result.getValue(), EPSILON);
	}

	@Test
	void testAddition_LargeValues() {
		Quantity<VolumeUnit> result = new Quantity<>(1e6, VolumeUnit.LITRE).add(new Quantity<>(1e6, VolumeUnit.LITRE));

		assertEquals(2e6, result.getValue(), EPSILON);
	}

	// ENUM TESTS

	@Test
	void testVolumeUnitEnum_LitreConstant() {
		assertEquals(1.0, VolumeUnit.LITRE.getConversionFactor(), EPSILON);
	}

	@Test
	void testVolumeUnitEnum_MillilitreConstant() {
		assertEquals(0.001, VolumeUnit.MILLILITRE.getConversionFactor(), EPSILON);
	}

	@Test
	void testVolumeUnitEnum_GallonConstant() {
		assertEquals(3.78541, VolumeUnit.GALLON.getConversionFactor(), EPSILON);
	}

	@Test
	void testConvertToBaseUnit_GallonToLitre() {
		assertEquals(3.78541, VolumeUnit.GALLON.convertToBaseUnit(1.0), EPSILON);
	}

	@Test
	void testConvertFromBaseUnit_LitreToGallon() {
		assertEquals(1.0, VolumeUnit.GALLON.convertFromBaseUnit(3.78541), EPSILON);
	}

	// ARCHITECTURE TESTS

	@Test
	void testGenericQuantity_VolumeOperations_Consistency() {
		Quantity<VolumeUnit> volume = new Quantity<>(1.0, VolumeUnit.LITRE);

		Quantity<LengthUnit> length = new Quantity<>(1.0, LengthUnit.FEET);

		assertNotEquals(volume, length);
	}

	@Test
	void testScalability_VolumeIntegration() {
		Quantity<VolumeUnit> v1 = new Quantity<>(1.0, VolumeUnit.LITRE);

		Quantity<VolumeUnit> v2 = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);

		assertEquals(v1, v2);
	}

	// SUBTRACTION TESTS

	@Test
	void testSubtraction_SameUnit_FeetMinusFeet() {
		Quantity<LengthUnit> result = new Quantity<>(10.0, LengthUnit.FEET)
				.subtract(new Quantity<>(5.0, LengthUnit.FEET));

		assertEquals(new Quantity<>(5.0, LengthUnit.FEET), result);
	}

	@Test
	void testSubtraction_SameUnit_LitreMinusLitre() {
		Quantity<VolumeUnit> result = new Quantity<>(10.0, VolumeUnit.LITRE)
				.subtract(new Quantity<>(3.0, VolumeUnit.LITRE));

		assertEquals(new Quantity<>(7.0, VolumeUnit.LITRE), result);
	}

	@Test
	void testSubtraction_CrossUnit_FeetMinusInches() {
		Quantity<LengthUnit> result = new Quantity<>(10.0, LengthUnit.FEET)
				.subtract(new Quantity<>(6.0, LengthUnit.INCH));

		assertEquals(new Quantity<>(9.5, LengthUnit.FEET), result);
	}

	@Test
	void testSubtraction_CrossUnit_InchesMinusFeet() {
		Quantity<LengthUnit> result = new Quantity<>(120.0, LengthUnit.INCH)
				.subtract(new Quantity<>(5.0, LengthUnit.FEET));

		assertEquals(new Quantity<>(60.0, LengthUnit.INCH), result);
	}

	@Test
	void testSubtraction_ExplicitTargetUnit_Feet() {
		Quantity<LengthUnit> result = new Quantity<>(10.0, LengthUnit.FEET)
				.subtract(new Quantity<>(6.0, LengthUnit.INCH), LengthUnit.FEET);

		assertEquals(new Quantity<>(9.5, LengthUnit.FEET), result);
	}

	@Test
	void testSubtraction_ExplicitTargetUnit_Inches() {
		Quantity<LengthUnit> result = new Quantity<>(10.0, LengthUnit.FEET)
				.subtract(new Quantity<>(6.0, LengthUnit.INCH), LengthUnit.INCH);

		assertEquals(new Quantity<>(114.0, LengthUnit.INCH), result);
	}

	@Test
	void testSubtraction_ExplicitTargetUnit_Millilitre() {
		Quantity<VolumeUnit> result = new Quantity<>(5.0, VolumeUnit.LITRE)
				.subtract(new Quantity<>(2.0, VolumeUnit.LITRE), VolumeUnit.MILLILITRE);

		assertEquals(new Quantity<>(3000.0, VolumeUnit.MILLILITRE), result);
	}

	@Test
	void testSubtraction_ResultingInNegative() {
		Quantity<LengthUnit> result = new Quantity<>(5.0, LengthUnit.FEET)
				.subtract(new Quantity<>(10.0, LengthUnit.FEET));

		assertEquals(new Quantity<>(-5.0, LengthUnit.FEET), result);
	}

	@Test
	void testSubtraction_ResultingInZero() {
		Quantity<LengthUnit> result = new Quantity<>(10.0, LengthUnit.FEET)
				.subtract(new Quantity<>(120.0, LengthUnit.INCH));

		assertEquals(new Quantity<>(0.0, LengthUnit.FEET), result);
	}

	@Test
	void testSubtraction_WithZeroOperand() {
		Quantity<LengthUnit> result = new Quantity<>(5.0, LengthUnit.FEET)
				.subtract(new Quantity<>(0.0, LengthUnit.INCH));

		assertEquals(new Quantity<>(5.0, LengthUnit.FEET), result);
	}

	@Test
	void testSubtraction_WithNegativeValues() {
		Quantity<LengthUnit> result = new Quantity<>(5.0, LengthUnit.FEET)
				.subtract(new Quantity<>(-2.0, LengthUnit.FEET));

		assertEquals(new Quantity<>(7.0, LengthUnit.FEET), result);
	}

	@Test
	void testSubtraction_NonCommutative() {
		Quantity<LengthUnit> a = new Quantity<>(10.0, LengthUnit.FEET);

		Quantity<LengthUnit> b = new Quantity<>(5.0, LengthUnit.FEET);

		assertNotEquals(a.subtract(b), b.subtract(a));
	}

	@Test
	void testSubtraction_WithLargeValues() {
		Quantity<WeightUnit> result = new Quantity<>(1e6, WeightUnit.KILOGRAM)
				.subtract(new Quantity<>(5e5, WeightUnit.KILOGRAM));

		assertEquals(new Quantity<>(5e5, WeightUnit.KILOGRAM), result);
	}

	@Test
	void testSubtraction_NullOperand() {
		assertThrows(IllegalArgumentException.class, () -> new Quantity<>(10.0, LengthUnit.FEET).subtract(null));
	}

	@Test
	void testSubtraction_NullTargetUnit() {
		assertThrows(IllegalArgumentException.class,
				() -> new Quantity<>(10.0, LengthUnit.FEET).subtract(new Quantity<>(5.0, LengthUnit.FEET), null));
	}

	@Test
	void testSubtraction_ChainedOperations() {
		Quantity<LengthUnit> result = new Quantity<>(10.0, LengthUnit.FEET)
				.subtract(new Quantity<>(2.0, LengthUnit.FEET)).subtract(new Quantity<>(1.0, LengthUnit.FEET));

		assertEquals(new Quantity<>(7.0, LengthUnit.FEET), result);
	}

	// DIVISION TESTS

	@Test
	void testDivision_SameUnit_FeetDividedByFeet() {
		double result = new Quantity<>(10.0, LengthUnit.FEET).divide(new Quantity<>(2.0, LengthUnit.FEET));

		assertEquals(5.0, result, EPSILON);
	}

	@Test
	void testDivision_CrossUnit_FeetDividedByInches() {
		double result = new Quantity<>(24.0, LengthUnit.INCH).divide(new Quantity<>(2.0, LengthUnit.FEET));

		assertEquals(1.0, result, EPSILON);
	}

	@Test
	void testDivision_RatioLessThanOne() {
		double result = new Quantity<>(5.0, LengthUnit.FEET).divide(new Quantity<>(10.0, LengthUnit.FEET));

		assertEquals(0.5, result, EPSILON);
	}

	@Test
	void testDivision_ByZero() {
		assertThrows(ArithmeticException.class,
				() -> new Quantity<>(10.0, LengthUnit.FEET).divide(new Quantity<>(0.0, LengthUnit.FEET)));
	}

	@Test
	void testDivision_NullOperand() {
		assertThrows(IllegalArgumentException.class, () -> new Quantity<>(10.0, LengthUnit.FEET).divide(null));
	}

	@Test
	void testSubtraction_Immutability() {
		Quantity<LengthUnit> original = new Quantity<>(10.0, LengthUnit.FEET);

		original.subtract(new Quantity<>(5.0, LengthUnit.FEET));

		assertEquals(new Quantity<>(10.0, LengthUnit.FEET), original);
	}

	@Test
	void testDivision_Immutability() {
		Quantity<LengthUnit> original = new Quantity<>(10.0, LengthUnit.FEET);

		original.divide(new Quantity<>(5.0, LengthUnit.FEET));

		assertEquals(new Quantity<>(10.0, LengthUnit.FEET), original);
	}

	// ================= UC12 REFACTOR VALIDATION =================

	@Test
	void testAdd_UsesBaseConversionCorrectly() {
		Quantity<LengthUnit> feet = new Quantity<>(1.0, LengthUnit.FEET);
		Quantity<LengthUnit> inches = new Quantity<>(12.0, LengthUnit.INCH);

		assertEquals(new Quantity<>(2.0, LengthUnit.FEET), feet.add(inches));
	}

	@Test
	void testSubtract_UsesBaseConversionCorrectly() {
		Quantity<LengthUnit> feet = new Quantity<>(2.0, LengthUnit.FEET);
		Quantity<LengthUnit> inches = new Quantity<>(12.0, LengthUnit.INCH);

		assertEquals(new Quantity<>(1.0, LengthUnit.FEET), feet.subtract(inches));
	}

	@Test
	void testDivide_UsesBaseConversionCorrectly() {
		Quantity<LengthUnit> feet = new Quantity<>(2.0, LengthUnit.FEET);
		Quantity<LengthUnit> inches = new Quantity<>(24.0, LengthUnit.INCH);

		assertEquals(1.0, feet.divide(inches), EPSILON);
	}

	// VALIDATION CONSISTENCY

	@Test
	void testOperations_NullOperand_ConsistentMessage() {
		Quantity<LengthUnit> q = new Quantity<>(10.0, LengthUnit.FEET);

		assertThrows(IllegalArgumentException.class, () -> q.add(null));
		assertThrows(IllegalArgumentException.class, () -> q.subtract(null));
		assertThrows(IllegalArgumentException.class, () -> q.divide(null));
	}

	// ROUNDING BEHAVIOR

	@Test
	void testAddition_RoundsToTwoDecimals() {
		Quantity<LengthUnit> a = new Quantity<>(1.333, LengthUnit.FEET);
		Quantity<LengthUnit> b = new Quantity<>(1.333, LengthUnit.FEET);

		Quantity<LengthUnit> result = a.add(b);

		assertEquals(2.67, result.getValue(), EPSILON);
	}

	@Test
	void testSubtraction_RoundsToTwoDecimals() {
		Quantity<LengthUnit> a = new Quantity<>(5.555, LengthUnit.FEET);
		Quantity<LengthUnit> b = new Quantity<>(2.222, LengthUnit.FEET);

		Quantity<LengthUnit> result = a.subtract(b);

		assertEquals(3.33, result.getValue(), EPSILON);
	}

	// ENUM OPERATION VALIDATION

	@Test
	void testDivide_ByZero_ThrowsException() {
		Quantity<LengthUnit> a = new Quantity<>(10.0, LengthUnit.FEET);
		Quantity<LengthUnit> b = new Quantity<>(0.0, LengthUnit.FEET);

		assertThrows(ArithmeticException.class, () -> a.divide(b));
	}

	// IMMUTABILITY CHECK

	@Test
	void testAdd_DoesNotModifyOriginalObjects() {
		Quantity<LengthUnit> original = new Quantity<>(5.0, LengthUnit.FEET);
		original.add(new Quantity<>(5.0, LengthUnit.FEET));

		assertEquals(new Quantity<>(5.0, LengthUnit.FEET), original);
	}

	// CHAIN OPERATIONS

	@Test
	void testChainOperations_WorkCorrectly() {
		Quantity<LengthUnit> result = new Quantity<>(10.0, LengthUnit.FEET).add(new Quantity<>(2.0, LengthUnit.FEET))
				.subtract(new Quantity<>(12.0, LengthUnit.INCH));

		assertEquals(new Quantity<>(11.0, LengthUnit.FEET), result);
	}
}