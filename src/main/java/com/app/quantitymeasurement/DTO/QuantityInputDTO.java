package com.app.quantitymeasurement.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuantityInputDTO {

    // First quantity input
    private QuantityDTO quantity1;

    // Second quantity input (optional for convert)
    private QuantityDTO quantity2;

    // Target unit (optional - for convert and add with target)
    private String targetUnit;
}