package com.example.fuelprices.repository;

import java.math.BigDecimal;
import java.util.List;

import com.example.fuelprices.model.FuelStation;

import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

public interface FuelStationRepository extends Repository<FuelStation, Long> {
    
    FuelStation findById(Long id);

    @PreAuthorize("hasAuthority('ADMIN')")
    List<FuelStation> findAll();

    @RestResource(exported = false)
    FuelStation save(FuelStation fuelStation);

    @RestResource(path = "area", rel = "area")
    List<FuelStation> findByLatitudeBetweenAndLongitudeBetween(BigDecimal bottom, BigDecimal top, BigDecimal left, BigDecimal right);

    @RestResource(path = "city", rel = "city")
    List<FuelStation> findByCity(String city);

    @RestResource(path = "street", rel = "street")
    List<FuelStation> findByCityAndStreet(String city, String street);

}
