package com.example.fuelprices.repository;

import com.example.fuelprices.model.FuelPrice;
import com.example.fuelprices.model.FuelStationBrand;
import com.example.fuelprices.model.FuelType;

import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface FuelPriceRepository extends Repository<FuelPrice, Long> {
    
    FuelPrice findById(Long id);

    @PreAuthorize("hasAuthority('ADMIN')")
    List<FuelPrice> findAll();

    @PreAuthorize("hasAuthrity('ADMIN')")
    void deleteById(Long id);

    @RestResource(exported = false)
    FuelPrice save(FuelPrice fuelPrice);

    List<FuelPrice> findByFuelStationCityOrderByPrice(String city);

    List<FuelPrice> findByFuelType(FuelType fuelType);

    List<FuelPrice> findByFuelStationCityAndFuelTypeOrderByPrice(String city, FuelType fuelType);

    List<FuelPrice> findByFuelStationBrand(FuelStationBrand brand);

}
