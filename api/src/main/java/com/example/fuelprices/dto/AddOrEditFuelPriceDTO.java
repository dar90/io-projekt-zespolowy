package com.example.fuelprices.dto;

import com.example.fuelprices.model.FuelType;
import java.math.BigDecimal;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

public record AddOrEditFuelPriceDTO(
    Long id,
    @NotNull FuelType type,
    Long stationId,
    @NotNull @DecimalMin("0.01") @DecimalMax("9.99") @Digits(integer = 1, fraction = 2)
    BigDecimal price
) {
    
}
