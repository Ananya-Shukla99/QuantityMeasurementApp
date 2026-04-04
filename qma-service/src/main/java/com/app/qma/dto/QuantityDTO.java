package com.app.qma.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class QuantityDTO {

    @NotNull
    private Double value;

    @NotNull
    private String unit;

    @NotNull
    @Pattern(regexp = "LengthUnit|VolumeUnit|WeightUnit|TemperatureUnit")
    private String measurementType;

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getMeasurementType() {
        return measurementType;
    }

    public void setMeasurementType(String measurementType) {
        this.measurementType = measurementType;
    }
}

