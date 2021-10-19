package com.example.fuelprices.dto;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

import com.example.fuelprices.model.FuelStationBrand;
import java.net.URL;

public record AddOrEditFuelStationDTO(
    Long id,
    @NotNull @DecimalMin("-90.0") @DecimalMax("90.0") 
    BigDecimal latitude,
    @NotNull @DecimalMin("-180.0") @DecimalMax("180.0")
    BigDecimal longitude,
    @NotBlank String name,
    @NotNull FuelStationBrand brand,
    Long ownerId,
    @NotBlank String city,
    String street,
    @NotBlank String plotNumber,
    Map<String, Boolean> services,
    URL logoUrl
) {
    
}
