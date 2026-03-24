package com.app.quantitymeasurement.controller;

import com.app.quantitymeasurement.DTO.QuantityDTO;
import com.app.quantitymeasurement.DTO.QuantityInputDTO;
import com.app.quantitymeasurement.DTO.QuantityMeasurementDTO;
import com.app.quantitymeasurement.service.IQuantityMeasurementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QuantityMeasurementController.class)
@AutoConfigureMockMvc(addFilters = false)
class QuantityMeasurementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IQuantityMeasurementService service;

    @Autowired
    private ObjectMapper objectMapper;

    private QuantityInputDTO compareInput;
    private QuantityInputDTO addInput;
    private QuantityInputDTO convertInput;
    private QuantityInputDTO divideInput;

    private QuantityMeasurementDTO mockCompareResult;
    private QuantityMeasurementDTO mockAddResult;
    private QuantityMeasurementDTO mockConvertResult;

    @BeforeEach
    void setUp() {
        QuantityDTO feetDTO = new QuantityDTO(1.0, "FEET", "LengthUnit");
        QuantityDTO inchDTO = new QuantityDTO(12.0, "INCH", "LengthUnit");

        compareInput = new QuantityInputDTO(feetDTO, inchDTO, null);
        addInput = new QuantityInputDTO(feetDTO, inchDTO, null);
        convertInput = new QuantityInputDTO(feetDTO, null, "INCH");
        divideInput = new QuantityInputDTO(feetDTO, inchDTO, null);

        mockCompareResult = new QuantityMeasurementDTO();
        mockCompareResult.setOperation("COMPARE");
        mockCompareResult.setResultString("Equal");
        mockCompareResult.setError(false);

        mockAddResult = new QuantityMeasurementDTO();
        mockAddResult.setOperation("ADD");
        mockAddResult.setResultValue(2.0);
        mockAddResult.setResultUnit("FEET");
        mockAddResult.setResultMeasurementType("LengthUnit");
        mockAddResult.setError(false);

        mockConvertResult = new QuantityMeasurementDTO();
        mockConvertResult.setOperation("CONVERT");
        mockConvertResult.setResultValue(12.0);
        mockConvertResult.setResultUnit("INCH");
        mockConvertResult.setError(false);
    }

    @Test
    void testCompare_ReturnsEqual() throws Exception {
        when(service.compare(any(), any())).thenReturn(mockCompareResult);

        mockMvc.perform(post("/api/v1/quantities/compare")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(compareInput)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("COMPARE"))
                .andExpect(jsonPath("$.resultString").value("Equal"))
                .andExpect(jsonPath("$.error").value(false));

        verify(service, times(1)).compare(any(), any());
    }

    @Test
    void testCompare_ServiceThrowsException_Returns400() throws Exception {
        when(service.compare(any(), any()))
                .thenThrow(new RuntimeException("Unit must be valid for the specified measurement type"));

        mockMvc.perform(post("/api/v1/quantities/compare")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(compareInput)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAdd_ReturnsSumResult() throws Exception {
        when(service.add(any(), any())).thenReturn(mockAddResult);

        mockMvc.perform(post("/api/v1/quantities/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addInput)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("ADD"))
                .andExpect(jsonPath("$.resultValue").value(2.0))
                .andExpect(jsonPath("$.resultUnit").value("FEET"));

        verify(service, times(1)).add(any(), any());
    }

    @Test
    void testAdd_IncompatibleTypes_Returns400() throws Exception {
        when(service.add(any(), any()))
                .thenThrow(new RuntimeException(
                        "Cannot perform arithmetic between different measurement categories: LengthUnit and WeightUnit"));

        mockMvc.perform(post("/api/v1/quantities/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addInput)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testConvert_ReturnConvertedValue() throws Exception {
        when(service.convert(any(), any())).thenReturn(mockConvertResult);

        mockMvc.perform(post("/api/v1/quantities/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(convertInput)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("CONVERT"))
                .andExpect(jsonPath("$.resultValue").value(12.0))
                .andExpect(jsonPath("$.resultUnit").value("INCH"));
    }

    @Test
    void testDivide_ByZero_ReturnsErrorResponse() throws Exception {
        QuantityMeasurementDTO errorResponse = new QuantityMeasurementDTO();
        errorResponse.setOperation("DIVIDE");
        errorResponse.setError(true);
        errorResponse.setResultString("Divide by zero");

        when(service.divide(any(), any())).thenReturn(errorResponse);

        mockMvc.perform(post("/api/v1/quantities/divide")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(divideInput)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("DIVIDE"))
                .andExpect(jsonPath("$.error").value(true));
    }
    
    @Test
    void testGetOperationHistory_ReturnsHistory() throws Exception {
        List<QuantityMeasurementDTO> history = Arrays.asList(mockAddResult, mockCompareResult);

        when(service.getHistoryByOperation("ADD")).thenReturn(history);

        mockMvc.perform(get("/api/v1/quantities/history/ADD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }


    @Test
    void testGetOperationCount_ReturnsCount() throws Exception {
        when(service.getOperationCount("ADD")).thenReturn(5L);

        mockMvc.perform(get("/api/v1/quantities/count/ADD"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }
}
