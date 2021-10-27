package com.example.fuelprices.dto;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.util.Set;

import com.example.fuelprices.model.FuelStationBrand;
import com.example.fuelprices.model.FuelStationServices;

import java.net.URL;

public record AddOrEditFuelStationDTO(
    Long id,
    @NotNull @DecimalMin("-90.0") @DecimalMax("90.0") @Digits(integer = 2, fraction = 8) 
    BigDecimal latitude,
    @NotNull @DecimalMin("-180.0") @DecimalMax("180.0") @Digits(integer = 2, fraction = 8)
    BigDecimal longitude,
    @NotBlank String name,
    @NotNull FuelStationBrand brand,
    Long ownerId,
    @NotBlank String city,
    String street,
    @NotBlank String plotNumber,
    Set<FuelStationServices> services,
    URL logoUrl
) {
    
}
