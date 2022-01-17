package com.example.fuelprices.service;

import java.time.LocalDateTime;
import java.util.Optional;

import com.example.fuelprices.dto.AddOrEditFuelPriceDTO;
import com.example.fuelprices.model.FuelPrice;
import com.example.fuelprices.model.FuelStation;
import com.example.fuelprices.model.User;
import com.example.fuelprices.repository.FuelPriceRepository;
import com.example.fuelprices.repository.FuelStationRepository;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FuelPriceService {
    
    private final FuelPriceRepository repository;
    private final FuelStationRepository fuelStationRepository;

    public FuelPriceService(FuelPriceRepository repository, 
                            FuelStationRepository fuelStationRepository) {
        this.repository = repository;
        this.fuelStationRepository = fuelStationRepository;
    }

    public Optional<FuelPrice> addOrEditFuelPrice(AddOrEditFuelPriceDTO dto) {
        return dto.id() == null ? addFuelPrice(dto) : editFuelPrice(dto);
    }

    private Optional<FuelPrice> addFuelPrice(AddOrEditFuelPriceDTO dto) {
        User author = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        FuelStation station = fuelStationRepository.findById(dto.stationId());

        if(author == null || station == null)
            return Optional.empty();

        FuelPrice fuelPrice = new FuelPrice();
        fuelPrice.setFuelType(dto.type());
        fuelPrice.setPrice(dto.price());
        fuelPrice.setAuthor(author);
        fuelPrice.setFuelStation(station);
        fuelPrice.setDateTime(LocalDateTime.now());

        return Optional.of(repository.save(fuelPrice));
    }

    private Optional<FuelPrice> editFuelPrice(AddOrEditFuelPriceDTO dto) {
        FuelPrice fuelPrice = repository.findById(dto.id());

        if(fuelPrice == null) 
            return Optional.empty();

        fuelPrice.setFuelType(dto.type());
        fuelPrice.setPrice(dto.price());

        return Optional.of(repository.save(fuelPrice));
    }

}
