package com.app.qma.dto;

import jakarta.validation.Valid;

public class QuantityInputDTO {

    @Valid
    private QuantityDTO quantity1;

    @Valid
    private QuantityDTO quantity2;

    private String targetUnit;

    public QuantityDTO getQuantity1() {
        return quantity1;
    }

    public void setQuantity1(QuantityDTO quantity1) {
        this.quantity1 = quantity1;
    }

    public QuantityDTO getQuantity2() {
        return quantity2;
    }

    public void setQuantity2(QuantityDTO quantity2) {
        this.quantity2 = quantity2;
    }

    public String getTargetUnit() {
        return targetUnit;
    }

    public void setTargetUnit(String targetUnit) {
        this.targetUnit = targetUnit;
    }
}

